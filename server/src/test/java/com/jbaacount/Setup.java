package com.jbaacount;

import com.jbaacount.dummy.DummyObject;
import com.jbaacount.model.Board;
import com.jbaacount.model.Comment;
import com.jbaacount.model.Member;
import com.jbaacount.model.Post;
import org.junit.jupiter.api.BeforeEach;

public abstract class Setup extends DummyObject
{
    protected Member mockMember;
    protected Board mockBoard;
    protected Post mockPost;
    protected Comment mockComment;

    @BeforeEach
    void setUp()
    {
        mockMember = newMockMember(1L, "test@gmail.com", "test", "ADMIN");
        mockBoard = newBoard("first board", 1);
        mockPost = newMockPost(1L, "first post", "content", mockBoard, mockMember);
        mockComment = newMockComment(1L, "text", mockPost, mockMember);
    }
}
