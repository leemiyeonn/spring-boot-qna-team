package com.codestates.question;

import com.codestates.answer.Answer;
import com.codestates.member.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Question { // Question 엔티티 클래스
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long questionId; // 질문 식별자

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // 질문 작성 시간

    @Column(nullable = false)
    private LocalDateTime modifiedAt = LocalDateTime.now(); // 질문 수정 시간

    @Column(nullable = false, length = 30) // 제목 :  not null, 길이 30 제한
    private String title; // 질문 제목

    @Column(nullable = false)
    private String content; // 질문 내용

    @Column(nullable = false)
    private long views = 0; // 조회수, 기본값 : 0 -> 조회수 기본 값을 0으로 할지 아니면 null 허용허고 조회수 기본값 null로 할지 정하지 않았음

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private QuestionStatus questionStatus = QuestionStatus.QUESTION_REGISTRATION; // 질문 상태

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private SecretStatus secretStatus = SecretStatus.SECRET; // 질문 공개 여부 -> 기본 값 : 공개글

    @ManyToOne // Question - Member 다대일 매핑
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "question") // Answer - Question 다대일 양방향 매핑
    private List<Answer> answers = new ArrayList<>();


    public enum QuestionStatus{ // 질문 상태
        QUESTION_REGISTRATION("질문 등록 상태"),
        QUESTION_ANSWERED("답변 완료 상태"),
        QUESTION_DELETE("질문 삭제 상태");

        @Getter
        private String status;

        QuestionStatus(String status) {
            this.status = status;
        }
    }

    public enum SecretStatus{
        PUBLIC("공개글 상태"),
        SECRET("비밀글 상태");

        @Getter
        private String status;

        SecretStatus(String status) {
            this.status = status;
        }
    }

}
