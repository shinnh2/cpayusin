package com.jbaacount.repository;

import com.jbaacount.model.Post;
import com.jbaacount.payload.response.PostResponseForProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepositoryCustom
{

    Page<PostResponseForProfile> getPostsByMemberId(Long memberId, Pageable pageable);

    Page<Post> getPostsByBoardIds(@Param("boardIds") List<Long> boardIds, @Param("keyword") String keyword, Pageable pageable);

}
