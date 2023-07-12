package com.jbaacount.post.service;

import com.jbaacount.global.service.AuthorizationService;
import com.jbaacount.member.entity.Member;
import com.jbaacount.member.repository.MemberRepository;
import com.jbaacount.member.service.MemberService;
import com.jbaacount.post.entity.Post;
import com.jbaacount.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PostService
{
    private final PostRepository postRepository;
    private final AuthorizationService authorizationService;
    private final MemberService memberService;

    public Post createPost(Post request, Member currentMember)
    {
        Post savedPost = postRepository.save(request);
        savedPost.addMember(currentMember);

        return savedPost;
    }
}
