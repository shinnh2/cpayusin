package com.jbaacount.service;

import com.jbaacount.global.dto.PageDto;
import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.global.exception.ExceptionMessage;
import com.jbaacount.global.service.AuthorizationService;
import com.jbaacount.model.Board;
import com.jbaacount.model.Category;
import com.jbaacount.model.Member;
import com.jbaacount.model.Post;
import com.jbaacount.payload.request.PostPatchDto;
import com.jbaacount.payload.response.PostMultiResponseDto;
import com.jbaacount.payload.response.PostResponseForProfile;
import com.jbaacount.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PostService
{
    private final PostRepository postRepository;
    private final AuthorizationService authorizationService;
    private final CategoryService categoryService;
    private final BoardService boardService;
    private final VoteService voteService;
    private final FileService fileService;

    public Post createPost(Post request, List<MultipartFile> files, Long categoryId, Long boardId, Member currentMember)
    {
        Board board = boardService.getBoardById(boardId);
        authorizationService.isUserAllowed(board.getIsAdminOnly(), currentMember);

        if(files != null && !files.isEmpty())
        {
            fileService.storeFiles(files, request);
        }

        if(categoryId != null)
        {
            Category category = categoryService.getCategory(categoryId);
            checkBoardHasCategory(board, category);

            authorizationService.isUserAllowed(category.getIsAdminOnly(), currentMember);
            request.addCategory(category);
        }

        log.info("===postService - createPost===");
        log.info("post saved successfully = {}", request.getTitle());

        request.addMember(currentMember);
        request.addBoard(board);
        currentMember.getScoreByPost();
        log.info("member score = {}", request.getMember().getScore());
        Post savedPost = postRepository.save(request);

        return savedPost;
    }

    public Post updatePost(Long postId, PostPatchDto request, List<MultipartFile> files, Member currentMember)
    {
        Post post = getPostById(postId);
        //Only the owner of the post has the authority to update
        authorizationService.isTheSameUser(post.getMember().getId(), currentMember.getId());

        Optional.ofNullable(request.getTitle())
                .ifPresent(title -> post.updateTitle(title));
        Optional.ofNullable(request.getContent())
                .ifPresent(content -> post.updateContent(content));

        Optional.ofNullable(request.getCategoryId())
                .ifPresent(categoryId ->
                {
                    Category category = categoryService.getCategory(categoryId);
                    authorizationService.isUserAllowed(category.getIsAdminOnly(), currentMember);
                    Board board = category.getBoard();

                    post.addCategory(category);
                    post.addBoard(board);
                });

        if(!files.isEmpty())
        {
            fileService.deleteUploadedFile(post);
            fileService.storeFiles(files, post);
        }

        return post;
    }

    @Transactional(readOnly = true)
    public Post getPostById(Long id)
    {
        return postRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(ExceptionMessage.POST_NOT_FOUND));
    }


    @Transactional(readOnly = true)
    public PageDto<PostResponseForProfile> getAllPostsByMemberId(Long memberId, Pageable pageable)
    {
        return postRepository.getAllPostsByMemberId(memberId, pageable);
    }

    @Transactional(readOnly = true)
    public PageDto<PostMultiResponseDto> getAllPostsByCategoryId(Long categoryId, String keyword, Pageable pageable)
    {
        return postRepository.getAllPostsByCategoryId(categoryId, keyword, pageable);
    }

    @Transactional(readOnly = true)
    public PageDto<PostMultiResponseDto> getAllPostsByBoardId(Long boardId, String keyword, Pageable pageable)
    {
        return postRepository.getAllPostsByBoardId(boardId, keyword, pageable);
    }

    public void deletePostById(Long postId, Member currentMember)
    {
        Post post = getPostById(postId);
        authorizationService.checkPermission(post.getMember().getId(), currentMember);

        voteService.deleteVoteByPostId(postId);
        fileService.deleteUploadedFile(post);

        postRepository.deleteById(postId);
    }

    private void checkBoardHasCategory(Board board, Category category)
    {
        if(!board.getCategories().contains(category))
            throw new BusinessLogicException(ExceptionMessage.CATEGORY_NOT_FOUND);
    }
}
