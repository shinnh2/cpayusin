package com.jbaacount.service;

import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.global.exception.ExceptionMessage;
import com.jbaacount.mapper.PostMapper;
import com.jbaacount.model.Board;
import com.jbaacount.model.Member;
import com.jbaacount.model.Post;
import com.jbaacount.payload.request.PostCreateRequest;
import com.jbaacount.payload.request.PostUpdateRequest;
import com.jbaacount.payload.response.PostMultiResponse;
import com.jbaacount.payload.response.PostResponseForProfile;
import com.jbaacount.payload.response.PostSingleResponse;
import com.jbaacount.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    public PostSingleResponse createPost(PostCreateRequest request, List<MultipartFile> files, Member currentMember)
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

        return PostMapper.INSTANCE.toPostSingleResponse(savedPost, false);
    }

    @Transactional
    public PostSingleResponse updatePost(Long postId, PostUpdateRequest request, List<MultipartFile> files, Member currentMember)
    {
        Post post = getPostById(postId);
        //Only the owner of the post has the authority to update
        utilService.isTheSameUser(post.getMember().getId(), currentMember.getId());

        PostMapper.INSTANCE.updatePostFromUpdateRequest(request, post);

        fileService.deleteUploadedFile(post);

        if(files != null && !files.isEmpty())
        {
            fileService.storeFiles(files, post);
        }

        return PostMapper.INSTANCE.toPostSingleResponse(post, checkIfAlreadyVote(currentMember, post));
    }


    public Post getPostById(Long id)
    {
        return postRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(ExceptionMessage.POST_NOT_FOUND));
    }

    public PostSingleResponse getPostSingleResponse(Long id, Member member)
    {
        Post post = getPostById(id);
        boolean voteStatus = checkIfAlreadyVote(member, post);

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

        var data = posts.map(post -> PostMapper.INSTANCE.toPostMultiResponse(post));

        return data;
    }

    private boolean checkIfAlreadyVote(Member member, Post post)
    {
        boolean voteStatus = false;

        if(member != null)
            voteStatus = voteService.existByMemberAndPost(member, post);

        return voteStatus;
    }

}
