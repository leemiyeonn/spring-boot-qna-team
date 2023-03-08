package com.codestates.answer;

import com.codestates.member.Member;
import com.codestates.question.Question;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Answer { // Answer 엔티티 클래스
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long answerId;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // 답변 등록 시간

    @Column(nullable = false)
    private LocalDateTime modifiedAt = LocalDateTime.now(); // 답변 수정 시간

    @Column(nullable = false)
    private String content; // 답변 내용

    @ManyToOne
    @JoinColumn(name = "member") // Answer - Member 다대일 매핑
    private Member member;

    @ManyToOne
    @JoinColumn(name = "question") // Answer - Question 다대일 매핑
    private Question question;

}
