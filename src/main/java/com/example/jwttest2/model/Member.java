package com.example.jwttest2.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Member {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", nullable=false)
    private String name;
    @Column(name="pwd", nullable=false)
    private String pwd;
}
