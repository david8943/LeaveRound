package com.ssafy.Dandelion.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpRequestDTO {

    @NotBlank(message = "이름은 필수 입력 항목입니다")
    @Size(max = 20, message = "이름은 20자를 초과할 수 없습니다")
    private String name;

    @NotBlank(message = "이메일은 필수 입력값입니다")
    @Email(message = "유효하지 않은 이메일 형식입니다")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다")
    private String password;
}
