package com.maeumteo.maeumteobackend.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberservice;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/checkid")
    public Boolean checkid(@RequestBody Member member){
        return memberservice.checkid(member.getId());
    }

    @PostMapping("/join")
    public ResponseEntity<Member> joinMember(@RequestBody Member member, HttpServletRequest request){
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        memberservice.joinMember(member);
        Member loginMember = memberservice.loginMember(member.getId(), member.getPassword()).get();
        loginMember.setPassword(null);

        HttpSession session = request.getSession();
        session.setAttribute("loginMember", loginMember);

        return ResponseEntity.ok(loginMember);
    }

    @PostMapping("/login")
    public String loginMember(@RequestBody Member member, HttpServletRequest request){
        System.out.println("=========================================");
        System.out.println(member.toString());
        System.out.println("=========================================");
        String storedPasswordHash = memberservice.getUserPassword(member.getId());
        boolean passwordMatches = passwordEncoder.matches(member.getPassword(), storedPasswordHash);

        if(passwordMatches) {
            Member loginMember = memberservice.loginMember(member.getId(), storedPasswordHash).get();
            loginMember.setPassword(null);

            HttpSession session = request.getSession();
            session.setAttribute("loginMember", loginMember);

            return member.getId();
        } else {
            return null;
        }
    }
}
