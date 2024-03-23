package com.jbaacount.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbaacount.dummy.DummyObject;
import com.jbaacount.mapper.PostMapper;
import com.jbaacount.model.Board;
import com.jbaacount.model.Member;
import com.jbaacount.model.Post;
import com.jbaacount.payload.request.PostCreateRequest;
import com.jbaacount.payload.response.PostSingleResponse;
import com.jbaacount.repository.BoardRepository;
import com.jbaacount.repository.MemberRepository;
import com.jbaacount.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Sql("classpath:db/teardown.sql")
@ExtendWith(MockitoExtension.class)
class PostServiceTest extends DummyObject
{
    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private BoardService boardService;

    @Spy
    private UtilService utilService;

    @Spy
    private ObjectMapper om;

    @BeforeEach
    void setUp()
    {

    }

    @Test
    void createPost_test() throws Exception
    {
        // given

        Board mockBoard = newMockBoard(1L, "게시판", 1);
        Member mockMember = newMockMember(1L, "aa@naver.com", "유저", "ADMIN");

        PostCreateRequest request = new PostCreateRequest();
        request.setBoardId(1L);
        request.setTitle("게시글1");
        request.setContent("게시글 내용");
        request.setBoardId(1L);

        Post postEntity = PostMapper.INSTANCE.toPostEntity(request);

        // stub 1
        when(boardService.getBoardById(any())).thenReturn(mockBoard);

        // stub 2
        utilService.isUserAllowed(mockBoard.getIsAdminOnly(), mockMember);

        // stub 3
        when(postRepository.save(any())).thenReturn(postEntity);

        // stub 4
        postEntity.addMember(mockMember);

        // stub 5
        postEntity.addBoard(mockBoard);


        // when
        PostSingleResponse response = postService.createPost(request, null, mockMember);
        String responseBody = om.writeValueAsString(response);

        // then
        assertThat(response.getContent()).isEqualTo("게시글 내용");
        System.out.println("response body " + responseBody);

    }
}