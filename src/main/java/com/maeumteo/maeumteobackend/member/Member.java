package com.maeumteo.maeumteobackend.member;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Entity
@Getter
@Setter
@ToString
public class Member implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    @Column
    private String username;
    @Column(unique = true)
    private String nickname;
    @Column(unique = true)
    private String email;
    @Column
    private String birthday;
    @Column
    private String password;
    @Column
    private String phoneNumber;
}
