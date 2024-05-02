package com.jbaacount.mapper;

import com.jbaacount.model.File;
import com.jbaacount.model.Member;
import com.jbaacount.model.type.Role;
import com.jbaacount.payload.request.member.MemberRegisterRequest;
import com.jbaacount.payload.response.member.MemberCreateResponse;
import com.jbaacount.payload.response.member.MemberDetailResponse;
import com.jbaacount.payload.response.member.MemberSimpleResponse;
import com.jbaacount.payload.response.member.MemberUpdateResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MemberMapper
{
    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    MemberCreateResponse toMemberCreateResponse(Member member);

    MemberUpdateResponse toMemberUpdateResponse(Member member);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "score", ignore = true)
    @Mapping(target = "platform", ignore = true)
    @Mapping(target = "posts", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "file", ignore = true)
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
        memberDetailResponse.setRole(member.getRole());
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
