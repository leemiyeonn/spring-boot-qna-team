package com.codestates.helper.event;

import com.codestates.helper.email.EmailSender;
import com.codestates.member.entity.Member;
import com.codestates.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.mail.MailSendException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

/**
 * <h3>트랜잭션(Transaction) 실습 Solution 코드 포함</h3>
 * MemberRegistrationEventListener는
 * publish된 {@link com.codestates.helper.event.MemberRegistrationEvent}를 리스닝 한 뒤
 * 해당 이벤트가 발생하면 처리하는 리스너 역할을 합니다.
 * <p>&nbsp;</p>
 * <h4>실습 과제에 대한 추가 설명</h4>
 * <ul>
 *     <li>
 *          전달받은 이벤트를 비동기로 처리하기 위해
 *          <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/scheduling/annotation/EnableAsync.html" target="_blank">
 *              {@literal @}EnableAsync
 *          </a> 애너테이션으로 비동기 모드를 활성화합니다.
 *     </li>
 *     <li>
 *         <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/scheduling/annotation/EnableAsync.html" target="_blank">
 *          {@literal @}EnableAsync
 *         </a> 애너테이션을 추가해 비동기 모드가 활성한 후,
 *         <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/scheduling/annotation/Async.html" target="_blank">
 *          {@literal @}Async
 *         </a>애너테이션을 실제 비동기로 처리하고자 하는 메서드에 추가함으로써 비동기 처리를 할 수 있습니다.
 *     </li>
 * </ul>
 * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/scheduling/annotation/EnableAsync.html" target="_blank">{@literal @}EnableAsync</a>
 * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/scheduling/annotation/Async.html" target="_blank">{@literal @}Async</a>
 */
@Slf4j
@EnableAsync
@Component
public class MemberRegistrationEventListener {
    private final EmailSender emailSender;
    private final MemberService memberService;

    public MemberRegistrationEventListener(EmailSender emailSender, MemberService memberService) {
        this.emailSender = emailSender;
        this.memberService = memberService;
    }

    /**
     * 회원 정보 등록 후, publish되는 {@link com.codestates.helper.event.MemberRegistrationEvent}를 비동기로 처리하는 코드입니다.
     * <p>
     *     <b>Solution 키 포인트</b>
     * </p>
     * <ul>
     *     <li>
     *          <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/event/EventListener.html" target="_blank">
     *              {@literal @}EventListener
     *          </a> 애너테이션을 추가해서 {@link com.codestates.helper.event.MemberRegistrationEvent}를 리스닝합니다.
     *     </li>
     *     <li>
     *         <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/scheduling/annotation/Async.html" target="_blank">
     *             {@literal @}
     *         Async</a> 애너테이션을 추가해서 이메일 전송 작업을 비동기로 처리합니다.
     *     </li>
     *     <li>
     *         이메일 전송 과정에서 에러가 발생하면 전달 받은 {@link com.codestates.helper.event.MemberRegistrationEvent}에 추가된
     *         Member 객체를 이용해 이미 저장된 회원 정보를 데이터베이스에서 삭제합니다.
     *     </li>
     * </ul>
     * @param event 비동기로 처리하고자 하는 MemberRegistrationEvent
     * @throws Exception 메일 전송 시
     */
    @Async
    @EventListener
    public void listen(MemberRegistrationEvent event) throws Exception {
        try {
            // 전송할 메시지를 생성했다고 가정.
            String message = "any email message";
            emailSender.sendEmail(message);
        } catch (MailSendException e) {
            e.printStackTrace();
            log.error("MailSendException: rollback for Member Registration:");
            Member member = event.getMember();
            memberService.deleteMember(member.getMemberId());
        }
    }
}
