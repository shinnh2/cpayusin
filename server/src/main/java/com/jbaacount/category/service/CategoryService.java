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
import java.util.Optional;

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

        Category savedCategory = categoryRepository.save(category);

        savedCategory.addBoard(board);
        savedCategory.updateOrderIndex(category.getId());

        return savedCategory;
    }

    public Category updateCategory(Long categoryId, CategoryPatchDto request, Member currentMember)
    {
        authorizationService.isAdmin(currentMember);

        Category category = getCategory(categoryId);
        Optional.ofNullable(request.getBoardId())
                .ifPresent(boardId ->
                {
                    Board board = getBoard(boardId);
                    category.addBoard(board);
                    category.changeCategoryAuthority(request.isAdminOnly());
                });

        Optional.ofNullable(request.getName())
                .ifPresent(name -> category.updateName(name));

        Optional.ofNullable(request.isAdminOnly())
                .ifPresent(isAdminOnly -> category.changeCategoryAuthority(isAdminOnly));

        Optional.ofNullable(request.getOrderIndex())
                .ifPresent(orderIndex ->{
                    Long currentIndex = category.getOrderIndex();

                    if(currentIndex > orderIndex)
                    {
                        List<Category> allCategories = categoryRepository.findAllBetween(orderIndex, currentIndex);
                        for (Category categoryList : allCategories)
                        {
                            categoryList.updateOrderIndex(categoryList.getOrderIndex() + 1);
                        }
                    }

                    else
                    {
                        List<Category> allCategories = categoryRepository.findAllBetween(currentIndex, orderIndex);
                        for (Category categoryList : allCategories)
                        {
                            categoryList.updateOrderIndex(categoryList.getOrderIndex() - 1);
                        }
                    }

                    category.updateOrderIndex(orderIndex);

                });


        return category;
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

    private Board getBoard(Long boardId)
    {
        return boardRepository.findById(boardId).orElseThrow();
    }
}
