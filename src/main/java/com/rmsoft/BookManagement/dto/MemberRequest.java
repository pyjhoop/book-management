package com.rmsoft.BookManagement.dto;

import com.rmsoft.BookManagement.domain.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MemberRequest {

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9]{5,11}$", message = "아이디는 영문자 시작 영문자와 숫자 포함 6~12자 입력해주세요")
    private String userId;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{6,12}", message = "비밀번호는 6~12자 영문, 숫자, 특수문자를 사용하세요")
    private String password;

    @NotBlank(message = "회원 이름은 필수 입력 값입니다.")
    private String userName;

    @NotBlank(message = "전화번호는 필수 입력값입니다.")
    @Pattern(regexp = "^[0-9]{11}$", message = "숫자만 11자 입력해주세요")
    private String phoneNumber;


    public Member toEntity(){
        return Member.of(getUserId(), getPassword(), getUserName(), getPhoneNumber());
    }


}
