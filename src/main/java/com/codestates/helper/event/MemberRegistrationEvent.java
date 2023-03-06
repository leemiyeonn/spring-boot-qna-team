package com.codestates.helper.event;

import com.codestates.member.entity.Member;
import lombok.Getter;

/**
 * <h3>트랜잭션(Transaction) 실습 Solution 코드 포함</h3>
 * MemberRegistrationEvent는 회원이 등록되었다는 사실을 알리는 이벤트의 역할을 하며,
 * {@link com.codestates.member.service.MemberService#createMember(Member)}에서 publish됩니다.
 * publish된 MemberRegistrationEvent는 해당 이벤트를 리스닝하고 있는
 * {@link com.codestates.helper.event.MemberRegistrationEventListener}에게 전달됩니다.
 *
 * <p>&nbsp;</p>
 * <h4>추가 설명</h4>
 * <ul>
 *     <li>
 *         <code>
 *              memberService.publish(member)
 *         </code>와 같은 형태로 publish할 수는 있지만 이벤트 모델 기반의 프로그래밍 방식에서는
 *         이벤트 자체를 publish하므로 가독성과 유지 보수 측면에서
 *         <code>
 *             memberService.publish(new MemberRegistrationEvent(member))
 *         </code> 형태로 publish 합니다.
 *     </li>
 * </ul>
 */
@Getter
public class MemberRegistrationEvent {
    private Member member;

    /**
     * MemberRegistrationEvent 객체를 생성 시, 이벤트에 포함될 회원 정보를 파라미터로 넘깁니다.
     *
     * @param member 이벤트에 포함되는 회원 정보
     */
    public MemberRegistrationEvent(Member member) {
        this.member = member;
    }
}
