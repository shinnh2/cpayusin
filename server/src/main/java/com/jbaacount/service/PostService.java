package com.jbaacount.service;

import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.global.exception.ExceptionMessage;
import com.jbaacount.mapper.PostMapper;
import com.jbaacount.model.Board;
import com.jbaacount.model.Member;
import com.jbaacount.model.Post;
import com.jbaacount.payload.request.post.PostCreateRequest;
import com.jbaacount.payload.request.post.PostUpdateRequest;
import com.jbaacount.payload.response.post.*;
import com.jbaacount.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostService
{
    private final PostRepository postRepository;
    private final UtilService utilService;
    private final BoardService boardService;
    private final VoteService voteService;
    private final FileService fileService;

    @Transactional
    public PostCreateResponse createPost(PostCreateRequest request, List<MultipartFile> files, Member currentMember)
    {
        Post post = PostMapper.INSTANCE.toPostEntity(request);
        Board board = boardService.getBoardById(request.getBoardId());
        utilService.isUserAllowed(board.getIsAdminOnly(), currentMember);

        post.addMember(currentMember);
        post.addBoard(board);

        Post savedPost = postRepository.save(post);

        if(files != null && !files.isEmpty())
        {
            fileService.storeFiles(files, savedPost);
        }

        currentMember.getScoreByPost();

        return PostMapper.INSTANCE.toPostCreateResponse(savedPost);
    }

    @Transactional
    public PostUpdateResponse updatePost(Long postId, PostUpdateRequest request, List<MultipartFile> files, Member currentMember)
    {
        Post post = getPostById(postId);
        //Only the owner of the post has the authority to update
        utilService.isTheSameUser(post.getMember().getId(), currentMember.getId());

        Optional.ofNullable(request.getBoardId())
                        .ifPresent(newBoardId -> {
                            Board board = boardService.getBoardById(newBoardId);
                            post.addBoard(board);
                        });

        PostMapper.INSTANCE.updatePostFromUpdateRequest(request, post);


        fileService.deleteUploadedFile(post);

        if(files != null && !files.isEmpty())
        {
            fileService.storeFiles(files, post);
        }

        return PostMapper.INSTANCE.toPostUpdateResponse(post);
    }


    public Post getPostById(Long id)
    {
        return postRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(ExceptionMessage.POST_NOT_FOUND));
    }

    public PostSingleResponse getPostSingleResponse(Long id, Member member)
    {
        Post post = getPostById(id);
        boolean voteStatus = voteService.checkIfMemberVotedPost(member.getId(), id);

        return PostMapper.INSTANCE.toPostSingleResponse(post, voteStatus);
    }

    public Page<PostResponseForProfile> getMyPosts(Member member, Pageable pageable)
    {
        return postRepository.getPostsByMemberId(member.getId(), pageable);
    }

    @Transactional
    public void deletePostById(Long postId, Member currentMember)
    {
        Post post = getPostById(postId);
        utilService.checkPermission(post.getMember().getId(), currentMember);

        /*voteService.deleteVoteByPostId(postId);
        fileService.deleteUploadedFile(post);
*/
        postRepository.deleteById(postId);
        log.info("게시글이 삭제되었습니다");
    }

    public Page<PostMultiResponse> getPostsByBoardId(Long boardId, String keyword, Pageable pageable)
    {
        var childrenList = boardService.getBoardIdListByParentId(boardId);
        childrenList.add(boardId);

        Page<Post> posts = postRepository.getPostsByBoardIds(childrenList, keyword, pageable);

        return posts.map(PostMapper.INSTANCE::toPostMultiResponse);
    }

    private boolean checkIfAlreadyVote(Member member, Post post)
    {
        boolean voteStatus = false;

        if(member != null)
            voteStatus = voteService.existByMemberAndPost(member, post);

        return voteStatus;
    }

}
