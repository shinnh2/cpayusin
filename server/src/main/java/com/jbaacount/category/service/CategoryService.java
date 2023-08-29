package com.jbaacount.category.service;

import com.jbaacount.board.entity.Board;
import com.jbaacount.board.repository.BoardRepository;
import com.jbaacount.category.dto.request.CategoryPatchDto;
import com.jbaacount.category.dto.response.CategoryResponseDto;
import com.jbaacount.category.entity.Category;
import com.jbaacount.category.repository.CategoryRepository;
import com.jbaacount.file.service.FileService;
import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.global.exception.ExceptionMessage;
import com.jbaacount.global.service.AuthorizationService;
import com.jbaacount.member.entity.Member;
import com.jbaacount.post.entity.Post;
import com.jbaacount.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Transactional
@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryService
{
    private final CategoryRepository categoryRepository;
    private final BoardRepository boardRepository;
    private final AuthorizationService authorizationService;
    private final VoteRepository voteRepository;
    private final FileService fileService;

    public Category createCategory(Category category, Long boardId, Member currentMember)
    {
        authorizationService.isAdmin(currentMember);
        Board board = getBoard(boardId);

        long orderIndex = categoryRepository.countCategory(boardId);
        category.updateOrderIndex(orderIndex + 1);

        Category savedCategory = categoryRepository.save(category);
        savedCategory.addBoard(board);

        return savedCategory;
    }

    public void categoryBulkUpdate(List<CategoryPatchDto> requests, long boardId, Member currentMember)
    {
        log.info("bulk update");
        authorizationService.isAdmin(currentMember);

        for(CategoryPatchDto request : requests)
        {
            if(Boolean.TRUE.equals(request.getIsDeleted()))
            {
                Category category = getCategory(request.getCategoryId());
                processDelete(request, category, boardId, currentMember);
            }
        }

        List<Long> categoryIds = requests.stream()
                .filter(request -> request.getIsDeleted() == null || !request.getIsDeleted())
                .map(CategoryPatchDto::getCategoryId)
                .collect(Collectors.toList());

        List<Category> categoryList = categoryRepository.findAllById(categoryIds);
        Map<Long, CategoryPatchDto> categoryMap = requests.stream()
                .collect(toMap(CategoryPatchDto::getCategoryId, Function.identity()));


        for(Category category : categoryList)
        {
            CategoryPatchDto request = categoryMap.get(category.getId());
            processUpdateCategory(request, category);
            processOrderIndexChange(request, category, boardId);
        }
        
    }


    @Transactional(readOnly = true)
    public Category getCategory(Long categoryId)
    {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new BusinessLogicException(ExceptionMessage.CATEGORY_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getAllCategories(Long boardId)
    {
        return categoryRepository.findAllCategories(boardId);
    }

    public void deleteCategory(Long categoryId, Member currentMember)
    {
        authorizationService.isAdmin(currentMember);
        Category category = getCategory(categoryId);
        List<Post> posts = category.getPosts();
        for (Post post : posts)
        {
            log.info("post removed = {}", post.getTitle());
            log.info("file removed in category service");

            voteRepository.deleteByPostId(post.getId());
            fileService.deleteUploadedFile(post);
        }

        categoryRepository.deleteById(categoryId);
    }

    private void processUpdateCategory(CategoryPatchDto request, Category category)
    {
        //board 를 변경 한다면
        //변경 전 board 안에 있는 category 의 orderIndex 를 변경 해준다(삭제 프로세스와 같음)
        Optional.ofNullable(request.getName())
                .ifPresent(name -> category.updateName(name));

        Optional.ofNullable(request.getBoardId())
                .ifPresent(boardId ->
                {
                    Board board = getBoard(boardId);
                    long previousBoardId = category.getBoard().getId();
                    changeOrderIndexForDeletionOrBoardChange(category.getOrderIndex(), previousBoardId);

                    category.addBoard(board);

                    long orderIndex = categoryRepository.countCategory(boardId);
                    category.updateOrderIndex(orderIndex + 1);
                });

        Optional.ofNullable(request.getIsAdminOnly())
                .ifPresent(isAdminOnly -> category.changeCategoryAuthority(isAdminOnly));
    }

    private void processOrderIndexChange(CategoryPatchDto request, Category category, long boardId)
    {
        Optional.ofNullable(request.getOrderIndex())
                .ifPresent(orderIndex -> {
                    Long currentIndex = category.getOrderIndex();

                    if(currentIndex > orderIndex)
                        categoryRepository.bulkUpdateOrderIndex(orderIndex, currentIndex, +1, boardId);

                    else
                        categoryRepository.bulkUpdateOrderIndex(currentIndex, orderIndex, -1, boardId);

                    category.updateOrderIndex(orderIndex);
                });
    }

    private void processDelete(CategoryPatchDto request, Category category, long boardId, Member currentMember)
    {
        Optional.ofNullable(request.getIsDeleted())
                .ifPresent(isDeleted -> {
                    changeOrderIndexForDeletionOrBoardChange(category.getOrderIndex(), boardId);

                    deleteCategory(category.getId(), currentMember);
                });
    }

    private void changeOrderIndexForDeletionOrBoardChange(long startOrderIndex, long boardId)
    {
        Long lastOrderIndex = categoryRepository.findTheBiggestOrderIndex(boardId);

        if(startOrderIndex < lastOrderIndex)
            categoryRepository.bulkUpdateOrderIndex(startOrderIndex, lastOrderIndex, -1, boardId);
    }

    private Board getBoard(Long boardId)
    {
        return boardRepository.findById(boardId).orElseThrow();
    }
}
