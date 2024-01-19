package com.jbaacount.repository;

import com.jbaacount.model.Post;
import com.jbaacount.payload.response.PostResponseForProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom
{

    Page<PostResponseForProfile> getPostsByMemberId(Long memberId, Pageable pageable);

    Page<Post> getPostsByBoardId(Long boardId, String keyword, Pageable pageable);

}
