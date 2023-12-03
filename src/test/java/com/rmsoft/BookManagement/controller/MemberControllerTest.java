package com.rmsoft.BookManagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rmsoft.BookManagement.dto.MemberRequest;
import com.rmsoft.BookManagement.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("MemberController-Test")
@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private MemberService memberService;

    @Nested
    @DisplayName("회원가입 컨트롤러 테스트")
    class Signup{
        @DisplayName("200")
        @Test
        void signupTest_Success() throws Exception {
            MemberRequest memberRequest = new MemberRequest("qwer123","qwer123!","박연준","01011112222");

            mvc.perform(post("/api/auth/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(memberRequest)))
                    .andExpect(status().is(201))
                    .andExpect(jsonPath("$.code", is(201)))
                    .andExpect(jsonPath("$.status", is("Created")))
                    .andExpect(jsonPath("$.message", is("회원가입이 성공적으로 진행되었습니다.")));
        }

        @DisplayName("400")
        @Test
        void signupTest_Fail() throws Exception {
            MemberRequest memberRequest = new MemberRequest("","qwer123!","박연준","01011112222");

            mvc.perform(post("/api/auth/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(memberRequest)))
                    .andExpect(status().is(400))
                    .andExpect(jsonPath("$.code", is(400)))
                    .andExpect(jsonPath("$.status", is("Bad Request")))
                    .andExpect(jsonPath("$.message", is("잘못 입력하셨습니다.")));

        }


    }

}