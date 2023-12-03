package com.rmsoft.BookManagement.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Api<T> {

    private int code;
    private String status;
    private String message;
    private T data;
}
