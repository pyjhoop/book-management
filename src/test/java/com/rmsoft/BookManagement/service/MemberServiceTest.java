package com.rmsoft.BookManagement.service;

import com.rmsoft.BookManagement.domain.Member;
import com.rmsoft.BookManagement.dto.MemberInfoRequest;
import com.rmsoft.BookManagement.dto.MemberRequest;
import com.rmsoft.BookManagement.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@DisplayName("MemberService-Test")
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock private MemberRepository memberRepository;
    @InjectMocks private MemberService memberService;

    MemberRequest memberRequest;
    Member member;

    @BeforeEach
    void init(){
        memberRequest = new MemberRequest("qwer123","qwer123!","박연준","01011112222");
        member = memberRequest.toEntity();
    }

    @Nested
    @DisplayName("회원가입 서비스")
    class SingUP{
        @DisplayName("성공 케이스")
        @Test
        void signupWithMemberRequest_WhenNotExistUserId_ThenSignupSuccessfully(){
            //Given
            given(memberRepository.findByUserId(memberRequest.getUserId())).willReturn(Optional.empty());
            given(memberRepository.save(any(Member.class))).willReturn(member);

            //When
            memberService.signup(memberRequest);

            //Then
            then(memberRepository).should().findByUserId(memberRequest.getUserId());
            then(memberRepository).should().save(any(Member.class));
        }

        @DisplayName("실패 케이스")
        @Test
        void signupWithMemberRequest_WhenExistUserId_ThenSignupFail(){
            // Given
            given(memberRepository.findByUserId(memberRequest.getUserId())).willReturn(Optional.of(member));

            // When
            Exception exception = assertThrows(RuntimeException.class, () -> {
                memberService.signup(memberRequest);
            });

            // Then
            String expectedMessage = memberRequest.getUserId()+"는 이미 존재합니다.";
            String actualMessage = exception.getMessage();

            assertEquals(expectedMessage, actualMessage);
            then(memberRepository).should().findByUserId(memberRequest.getUserId());
            then(memberRepository).should(never()).save(any(Member.class));

        }
    }

    @Nested
    @DisplayName("회원 확인 서비스")
    class CheckMember{

        @DisplayName("성공 케이스")
        @Test
        void checkMemberWithMemberInfoRequest_WhenMemberInfoIsAvailable_ThenReturnTrue(){
            //Given
            member.setPassword(BCrypt.hashpw("qwer123!",BCrypt.gensalt()));
            MemberInfoRequest memberInfoRequest = new MemberInfoRequest("qwer123", "qwer123!");

            given(memberRepository.findByUserId(memberInfoRequest.getUserId())).willReturn(Optional.of(member));

            //When
            boolean result = memberService.checkMember(memberInfoRequest);

            //Then
            assertThat(result).isTrue();
        }

        @DisplayName("실패 케이스1(비밀번호 일치X)")
        @Test
        void checkMemberWithMemberInfoRequest_WhenPasswordIsNotAvailable_ThenThrowException(){
            //Given
            member.setPassword(BCrypt.hashpw("qwer12312!",BCrypt.gensalt()));
            MemberInfoRequest memberInfoRequest = new MemberInfoRequest("qwer123", "qwer123!");

            given(memberRepository.findByUserId(memberInfoRequest.getUserId())).willReturn(Optional.of(member));

            //When
            Exception exception = assertThrows(RuntimeException.class, () -> {
                memberService.checkMember(memberInfoRequest);
            });

            //Then
            String expectedMessage = "비밀번호가 일치하지 않습니다.";
            String actualMessage = exception.getMessage();

            assertEquals(expectedMessage, actualMessage);
        }

        @DisplayName("실패 케이스2(아이디 일치X)")
        @Test
        void checkMemberWithMemberInfoRequest_WhenUserIdIsNotAvailable_ThenThrowException(){
            //Given
            member.setPassword(BCrypt.hashpw("qwer12312!",BCrypt.gensalt()));
            MemberInfoRequest memberInfoRequest = new MemberInfoRequest("qwer123", "qwer123!");

            given(memberRepository.findByUserId(memberInfoRequest.getUserId())).willReturn(Optional.empty());

            //When
            Exception exception = assertThrows(RuntimeException.class, () -> {
                memberService.checkMember(memberInfoRequest);
            });

            //Then
            String expectedMessage = "아이디가 잘못되었습니다.";
            String actualMessage = exception.getMessage();

            assertEquals(expectedMessage, actualMessage);
        }

    }


}