package com.codestates.member.service;

import com.codestates.exception.BusinessLogicException;
import com.codestates.exception.ExceptionCode;
import com.codestates.helper.event.MemberRegistrationEvent;
import com.codestates.member.entity.Member;
import com.codestates.member.repository.MemberRepository;
import com.codestates.utils.CustomBeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * <h3>트랜잭션(Transaction) 실습 Solution 코드 포함</h3>
 * MemberService 클래스는 트랜잭션(Transaction) 실습 과제의 Solution 코드를 포함하고 있습니다.
 * <p>&nbsp;</p>
 * <h4>실습 과제에 대한 추가 설명</h4>
 * <ul>
 *     <li>
 *         회원 정보를 성공적으로 저장하게 되면 회원에게 이메일을 전송하도록 구현되어 있습니다.
 *         이 때 이메일은 실제 전송이 이루어지는 것은 아니고, 이메일 전송에 대한 로그를 출력합니다.
 *         하지만 이메일 발송이 완료되기까지는 일정 시간이 걸리기 때문에 현재 쓰레드가 아닌 별도의 쓰레드에서 전송하도록 하는 것이 바람직합니다.
 *     </li>
 *     <li>
 *         그런데 하나 이상의 쓰레드에서 비동기적으로 동작하는 각각의 기능을 하나의 트랜잭션으로 묶는 것이 기술적으로 쉽지 않습니다.
 *         따라서 이번 실습에서는 Spring에서 지원하는 ApplicationEvent를 이용합니다.
 *     </li>
 *     <li>
 *         실습 과제 Solution 시나리오
 *         <ul>
 *             <li>
 *                 회원 정보 저장 후, ApplicationEventPublisher를 이용해 저장된 회원 정보(Member 객체)를 이벤트 객체인
 *                 {@link com.codestates.helper.event.MemberRegistrationEvent}에 담아서 publish합니다.
 *                 이벤트를 publish하는 동작은 회원 정보를 저장하는 쓰레드에서 동기적으로 일어나지만 실제 시간이 많이 걸리는 작업은
 *                 이메일을 전송하는 작업이므로 성능적으로 크게 문제가 되지 않습니다.
 *             </li>
 *             <li>
 *                 여기서 publish한 이벤트는 Application 내에서 리스닝하고 있는 이벤트 리스너인
 *                 {@link com.codestates.helper.event.MemberRegistrationEventListener}가 전달 받습니다.
 *                 전달받은 이벤트 객체에는 MemberService에서 저장한 회원 정보(Member 객체)가 포함되어 있습니다.
 *             </li>
 *             <li>
 *                 이벤트 객체({@link com.codestates.helper.event.MemberRegistrationEvent})로부터 얻은 회원 정보를
 *                 비동기적으로 이메일을 전송합니다.
 *                 여기서 추가된 또 하나의 쓰레드는 이벤트 리스너인
 *                 {@link com.codestates.helper.event.MemberRegistrationEventListener}의 동작이 실행되는 쓰레드입니다.
 *             </li>
 *             <li>
 *                 만약 이메일 전송 도중 에러가 발생할 경우, 이벤트 리스너인
 *                 {@link com.codestates.helper.event.MemberRegistrationEventListener}에서
 *                 이벤트 객체({@link com.codestates.helper.event.MemberRegistrationEvent})로
 *                 전달 받은 회원 정보(Member 객체)를 이용해 DB에 저장된 해당 회원 정보를 삭제합니다.
 *             </li>
 *         </ul>
 *     </li>
 * </ul>
 */
@Transactional
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher publisher;
    private final CustomBeanUtils<Member> beanUtils;

    public MemberService(MemberRepository memberRepository,
                         ApplicationEventPublisher publisher,
                         CustomBeanUtils<Member> beanUtils) {
        this.memberRepository = memberRepository;
        this.publisher = publisher;

        this.beanUtils = beanUtils;
    }

    /**
     * 회원 정보 저장 후, 회원에게 비동기로 이메일을 전송하기 위해 이벤트를 publish하는 Solution 코드입니다.
     * <p>
     *     <b>Solution 키 포인트</b>
     * </p>
     * <ul>
     *     <li>
     *          회원 정보 저장 후, 비동기로 처리되는 이메일 전송 중 에러가 발생했을 때,
     *          회원 정보 저장 작업과 이메일 전송 작업을 하나의 트랜잭션으로 처리 가능하도록 회원 정보를
     *          이벤트({@link com.codestates.helper.event.MemberRegistrationEvent}에 담아서 publish합니다.
     *     </li>
     *     <li>
     *         publish된 이벤트는 {@link com.codestates.helper.event.MemberRegistrationEventListener}에서 처리합니다.
     *     </li>
     * </ul>
     * @param member 신규로 등록되는 회원 정보
     * @return 데이터베이스에 저장된 Member 객체
     */
    public Member createMember(Member member) {
        verifyExistsEmail(member.getEmail());
        Member savedMember = memberRepository.save(member);

        // 추가된 부분
        publisher.publishEvent(new MemberRegistrationEvent(savedMember));
        return savedMember;
    }

    public Member updateMember(Member member) {
        Member findMember = findVerifiedMember(member.getMemberId());

        // 리팩토링 전 코드
//        Optional.ofNullable(member.getName())
//                .ifPresent(name -> findMember.setName(name));
//        Optional.ofNullable(member.getPhone())
//                .ifPresent(phone -> findMember.setPhone(phone));
//        Optional.ofNullable(member.getMemberStatus())
//                .ifPresent(memberStatus -> findMember.setMemberStatus(memberStatus));

        // 리펙토링 후 코드
        Member updatingMember = beanUtils.copyNonNullProperties(member, findMember);
        return memberRepository.save(updatingMember);
    }

    @Transactional(readOnly = true)
    public Member findMember(long memberId) {
        return findVerifiedMember(memberId);
    }

    public Page<Member> findMembers(int page, int size) {
        return memberRepository.findAll(PageRequest.of(page, size,
                Sort.by("memberId").descending()));
    }

    public void deleteMember(long memberId) {
        Member findMember = findVerifiedMember(memberId);

        memberRepository.delete(findMember);
    }

    @Transactional(readOnly = true)
    public Member findVerifiedMember(long memberId) {
        Optional<Member> optionalMember =
                memberRepository.findById(memberId);
        Member findMember =
                optionalMember.orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        return findMember;
    }

    private void verifyExistsEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent())
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
    }
}
