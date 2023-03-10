package com.codestates.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class MemberDto {
    @Getter
    @AllArgsConstructor
    public static class Post{
        @NotBlank
        @Email
        private String email;

        @NotBlank(message = "이름을 입력하세요.")
        private String nickname;

        @NotBlank(message = "비밀번호를 입력하세요.")
        private String password;
    }
}
