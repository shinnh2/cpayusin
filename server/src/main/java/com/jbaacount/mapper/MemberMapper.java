package com.jbaacount.mapper;

import com.jbaacount.model.File;
import com.jbaacount.model.Member;
import com.jbaacount.payload.request.MemberRegisterRequest;
import com.jbaacount.payload.response.MemberDetailResponse;
import com.jbaacount.payload.response.MemberSimpleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MemberMapper
{
    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    Member toMemberEntity(MemberRegisterRequest postDto);

    default MemberDetailResponse toMemberDetailResponse(Member member)
    {
        if ( member == null ) {
            return null;
        }

        MemberDetailResponse memberDetailResponse = new MemberDetailResponse();

        memberDetailResponse.setId( member.getId() );
        memberDetailResponse.setNickname( member.getNickname() );
        memberDetailResponse.setEmail( member.getEmail() );
        memberDetailResponse.setScore( member.getScore() );
        memberDetailResponse.setUrl(mapFiles(member.getFile()));
        memberDetailResponse.setCreatedAt( member.getCreatedAt() );

        return memberDetailResponse;
    }

    List<MemberDetailResponse> toMemberDetailList(List<Member> members);

    MemberSimpleResponse toMemberSimpleInfo(Member member);

    default String mapFiles(File file)
    {
        if(file == null)
            return null;

        return file.getUrl();
    }
}
