package com.codestates.member;

import com.codestates.answer.Answer;
import com.codestates.question.Question;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Member { // Member 엔티티 클래스
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberId;

    @Column(nullable = false)
    private String email; // 이메일

    @Column(nullable = false)
    private String nickname; // 닉네임

    @Column(nullable = false)
    private String password; // 비밀번호

    @OneToMany(mappedBy = "member") // Question - Member 다대일 양방향 매핑
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "member") // Answer - Member 다대일 양방향 매핑
    private List<Answer> answers = new ArrayList<>();

    public enum Role{
        MEMBER("일반 회원"),
        MANAGER("관리자");

        @Getter
        private String status;

        Role(String status) {
            this.status = status;
        }
    }

}
