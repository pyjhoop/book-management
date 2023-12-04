package com.rmsoft.BookManagement.controller;

import com.rmsoft.BookManagement.dto.Api;
import com.rmsoft.BookManagement.dto.MemberRequest;
import com.rmsoft.BookManagement.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<Api<?>> signup(
            @Valid @RequestBody MemberRequest memberRequest, Errors errors)
    {

        if(errors.hasErrors()){
            Map<String, String> validatorResult = memberService.validateHandling(errors);
            return ResponseEntity.status(400)
                    .body(Api.builder()
                            .code(400)
                            .status("Bad Request")
                            .message("잘못 입력하셨습니다.")
                            .data(validatorResult).build());
        }

        memberService.signup(memberRequest);
        log.info("User signed up successfully. Username: {}", memberRequest.getUserName());

        return ResponseEntity.status(201)
                .body(Api.builder()
                        .code(201)
                        .status("Created")
                        .message("회원가입이 성공적으로 진행되었습니다.")
                        .build());
    }
}
