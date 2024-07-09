package com.maeumteo.maeumteobackend.member;

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
