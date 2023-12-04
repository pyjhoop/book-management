package com.rmsoft.BookManagement.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rmsoft.BookManagement.dto.MemberRequest;
import com.rmsoft.BookManagement.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;

@DisplayName("MemberController-Test")
@WebMvcTest(MemberController.class)
@AutoConfigureMockMvc(addFilters = false)
class MemberControllerTest extends ControllerConfig{

    @Autowired private ObjectMapper objectMapper;
    @MockBean private MemberService memberService;

    @Nested
    @DisplayName("회원가입 컨트롤러 테스트")
    class Signup{
        @DisplayName("201")
        @Test
        void signupTest_Success() throws Exception {
            MemberRequest memberRequest = new MemberRequest("qwer123","qwer123!","박연준","01011112222");

            ResultActions perform = mvc.perform(RestDocumentationRequestBuilders.post("/api/auth/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(memberRequest)))
                    .andExpect(status().is(201))
                    .andExpect(jsonPath("$.code", is(201)))
                    .andExpect(jsonPath("$.status", is("Created")))
                    .andExpect(jsonPath("$.message", is("회원가입이 성공적으로 진행되었습니다.")));

            perform.andDo(document("{class-name}/{method-name}",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(
                            ResourceSnippetParameters.builder()
                                    .tag("Member")
                                    .description("회원 가입")
                                    .requestFields(
                                            fieldWithPath("userId").description("회원 아이디"),
                                            fieldWithPath("password").description("비밀번호"),
                                            fieldWithPath("userName").description("회원 이름"),
                                            fieldWithPath("phoneNumber").description("핸드폰 번호")
                                    ).requestSchema(Schema.schema("MemberRequest"))
                                    .responseFields(
                                            fieldWithPath("code").description("응답 코드"),
                                            fieldWithPath("status").description("응답 상태"),
                                            fieldWithPath("message").description("응답 메세지"),
                                            fieldWithPath("data").description("응답 데이터")
                                    ).responseSchema(Schema.schema("Api"))
                                    .build()
                    )));
        }

        @DisplayName("400")
        @Test
        void signupTest_Fail() throws Exception {
            MemberRequest memberRequest = new MemberRequest("","qwer123!","박연준","01011112222");

            ResultActions perform = mvc.perform(RestDocumentationRequestBuilders.post("/api/auth/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(memberRequest)))
                    .andExpect(status().is(400))
                    .andExpect(jsonPath("$.code", is(400)))
                    .andExpect(jsonPath("$.status", is("Bad Request")))
                    .andExpect(jsonPath("$.message", is("잘못 입력하셨습니다.")));

            perform.andDo(document("{class-name}/{method-name}",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(
                            ResourceSnippetParameters.builder()
                                    .tag("Member")
                                    .description("회원 가입")
                                    .requestFields(
                                            fieldWithPath("userId").description("회원 아이디"),
                                            fieldWithPath("password").description("비밀번호"),
                                            fieldWithPath("userName").description("회원 이름"),
                                            fieldWithPath("phoneNumber").description("핸드폰 번호")
                                    ).requestSchema(Schema.schema("MemberRequest"))
                                    .responseFields(
                                            fieldWithPath("code").description("응답 코드"),
                                            fieldWithPath("status").description("응답 상태"),
                                            fieldWithPath("message").description("응답 메세지"),
                                            fieldWithPath("data").description("응답 데이터")
                                    ).responseSchema(Schema.schema("Api"))
                                    .build()
                    )));

        }


    }

}