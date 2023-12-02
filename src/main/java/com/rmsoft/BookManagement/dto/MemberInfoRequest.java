package com.rmsoft.BookManagement.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MemberInfoRequest {
    private String userId;
    private String password;
}
