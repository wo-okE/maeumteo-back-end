package com.maeumteo.maeumteobackend.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberservice;

    @PostMapping("/checkid")
    public Boolean checkid(@RequestBody Member member){
        return memberservice.checkid(member.getId());
    }

    @PostMapping("/join")
    public Member joinMember(@RequestBody Member member){
        memberservice.joinMember(member);
        return memberservice.loginMember(member);
    }

    @PostMapping("/login")
    public Member loginMember(@RequestBody Member member){
        return memberservice.loginMember(member);
    }
}
