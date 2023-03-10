package com.codestates.like;

import com.codestates.member.Member;
import com.codestates.question.Question;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "likes")
public class Like { // Like 엔티티 클래스
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long likeId;

    @Column
    private long count;

    @ManyToOne
    @JoinColumn(name = "member") // Like - Member 다대일 매핑
    private Member member;

    @ManyToOne
    @JoinColumn(name = "question") // Like - Question 다대일 매핑
    private Question question;
}
