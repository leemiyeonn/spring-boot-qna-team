package com.codestates.member.mapper;

import com.codestates.member.Member;
import com.codestates.member.MemberDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member memberPostToMember(MemberDto.Post requestBody);
}
