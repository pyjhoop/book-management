package com.rmsoft.BookManagement.service;

import com.rmsoft.BookManagement.domain.Member;
import com.rmsoft.BookManagement.dto.MemberRequest;
import com.rmsoft.BookManagement.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void signup(MemberRequest memberRequest){

        Optional<Member> optionalMember = memberRepository.findByUserId(memberRequest.getUserId());

        if(optionalMember.isEmpty()){
            memberRepository.save(memberRequest.toEntity());
        }else{
            throw new RuntimeException(memberRequest.getUserId()+"는 이미 존재합니다.");
        }

    }

    public Map<String, String> validateHandling(Errors errors){

        Map<String, String> validatorResult = new HashMap<>();

        for(FieldError error: errors.getFieldErrors()){
            String validKeyName = String.format("valid_%s",error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }
}
