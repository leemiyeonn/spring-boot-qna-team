package com.codestates.member;

import com.codestates.exception.BusinessLogicException;
import com.codestates.exception.ExceptionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    /*
    <createMember() - 회원 등록>
    1. 중복 이메일 검증
     */
    public Member createMember(Member member) {
        verifyExistsEmail(member.getEmail()); // 중복 이메일 검증
        return memberRepository.save(member);
    }


    private void verifyExistsEmail(String email) { // 중복 이메일 검증 메서드
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) { // 이미 존재하는 이메일인 경우 예외 발생
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
        }
    }

    public Member findMember(long memberId) {
        return findVerifiedMember(memberId);
    }

    public Member findVerifiedMember(long memberId) {
        Optional<Member> optionalMember =
                memberRepository.findById(memberId);
        Member findMember =
                optionalMember.orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        return findMember;
    }
}
