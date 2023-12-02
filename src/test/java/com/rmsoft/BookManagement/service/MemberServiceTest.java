package com.rmsoft.BookManagement.service;

import com.rmsoft.BookManagement.domain.Member;
import com.rmsoft.BookManagement.dto.MemberRequest;
import com.rmsoft.BookManagement.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

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

    @DisplayName("회원가입 서비스-성공")
    @Test
    void givenMemberRequest_whenSignup_ThenSuccessSignup(){
        //Given
        MemberRequest memberRequest = new MemberRequest("qwer123","qwer123!","박연준","01011112222");
        Member member = memberRequest.toEntity();

        given(memberRepository.findByUserId(memberRequest.getUserId())).willReturn(Optional.empty());
        given(memberRepository.save(any(Member.class))).willReturn(member);

        //When
        memberService.signup(memberRequest);

        //Then
        then(memberRepository).should().findByUserId(memberRequest.getUserId());
        then(memberRepository).should().save(any(Member.class));
    }

    @DisplayName("회원가입 서비스-실패")
    @Test
    void givenWrongMemberRequest_whenSignup_thenFailSignup(){
        // Given
        MemberRequest memberRequest = new MemberRequest("qwer123","qwer123!","박연준","01011112222");
        Member member = memberRequest.toEntity();

        given(memberRepository.findByUserId(memberRequest.getUserId())).willReturn(Optional.of(member));

        // When
        Exception exception = assertThrows(RuntimeException.class, () -> {
            memberService.signup(memberRequest);
        });

        // Then
        String expectedMessage = memberRequest.getUserId()+"는 이미 존재합니다.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        then(memberRepository).should().findByUserId(memberRequest.getUserId());
        then(memberRepository).should(never()).save(any(Member.class));

    }


}