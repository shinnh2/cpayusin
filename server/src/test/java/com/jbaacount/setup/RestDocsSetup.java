package com.jbaacount.setup;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbaacount.dummy.DummyObject;
import com.jbaacount.global.security.userdetails.MemberDetails;
import com.jbaacount.model.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;

@MockBean(JpaMetamodelMappingContext.class)
@ExtendWith(RestDocumentationExtension.class)
@WithMockUser(roles = "ADMIN")
@AutoConfigureRestDocs
public abstract class RestDocsSetup extends DummyObject
{
    @MockBean
    protected RedisTemplate<String, String> redisTemplate;

    @MockBean
    protected ValueOperations<String, String> valueOperations;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mvc;

    protected MemberDetails memberDetails;
    protected Member member;

    @BeforeEach
    void setUp()
    {
        member = newMockMember(1L, "test@gmail.com", "관리자", "ADMIN");
        memberDetails = new MemberDetails(member);

        given(redisTemplate.opsForValue()).willReturn(valueOperations);
    }
}
