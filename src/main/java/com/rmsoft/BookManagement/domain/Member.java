package com.rmsoft.BookManagement.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Entity
public class Member extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @Column(columnDefinition = "VARCHAR(36)", nullable = false)
    private String userId;

    @Setter @Column(columnDefinition = "VARCHAR(36)", nullable = false)
    private String password;

    @Setter @Column(columnDefinition = "VARCHAR(100)", nullable = false)
    private String userName;

    @Setter @Column(columnDefinition = "VARCHAR(12)", nullable = false)
    private String phoneNumber;

    @OneToMany(mappedBy = "member")
    private final List<LoanHistory> loanHistories = new ArrayList<>();

    private Member(String userId, String password, String userName, String phoneNumber) {
        this.userId = userId;
        this.password = password;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
    }

    public static Member of(String userId, String password, String userName, String phoneNumber) {
        return new Member(userId, password, userName, phoneNumber);
    }

}