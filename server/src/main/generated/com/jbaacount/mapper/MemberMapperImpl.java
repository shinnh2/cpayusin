package com.jbaacount.mapper;

import com.jbaacount.model.Member;
import com.jbaacount.payload.request.MemberRegisterRequest;
import com.jbaacount.payload.response.MemberDetailResponse;
import com.jbaacount.payload.response.MemberSimpleResponse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-12T17:54:48+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 19.0.2 (Amazon.com Inc.)"
)
@Component
public class MemberMapperImpl implements MemberMapper {

    @Override
    public Member toMemberEntity(MemberRegisterRequest postDto) {
        if ( postDto == null ) {
            return null;
        }

        Member.MemberBuilder member = Member.builder();

        member.nickname( postDto.getNickname() );
        member.email( postDto.getEmail() );
        member.password( postDto.getPassword() );

        return member.build();
    }

    @Override
    public List<MemberDetailResponse> toMemberDetailList(List<Member> members) {
        if ( members == null ) {
            return null;
        }

        List<MemberDetailResponse> list = new ArrayList<MemberDetailResponse>( members.size() );
        for ( Member member : members ) {
            list.add( toMemberDetailResponse( member ) );
        }

        return list;
    }

    @Override
    public MemberSimpleResponse toMemberSimpleInfo(Member member) {
        if ( member == null ) {
            return null;
        }

        MemberSimpleResponse.MemberSimpleResponseBuilder memberSimpleResponse = MemberSimpleResponse.builder();

        memberSimpleResponse.id( member.getId() );
        memberSimpleResponse.nickname( member.getNickname() );

        return memberSimpleResponse.build();
    }
}
