package com.maeumteo.maeumteobackend.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> joinMember(@RequestBody Member member, HttpServletRequest request){
        member.setPassword(passwordEncoder.encode(member.getPassword()));

        if(memberservice.saveMember(member)){
            Member loginMember = memberservice.loginMember(member.getId(), member.getPassword()).get();
            HttpSession session = request.getSession();
            session.setAttribute("loginMember", loginMember);
            return ResponseEntity.ok(loginMember.getUsername());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginMember(@RequestBody LoginRequest loginRequest, HttpServletRequest request){
        String storedPasswordHash = memberservice.getUserPassword(loginRequest.getId());
        boolean passwordMatches = passwordEncoder.matches(loginRequest.getPassword(), storedPasswordHash);
        if(passwordMatches) {
            Member loginMember = memberservice.loginMember(loginRequest.getId(), storedPasswordHash).get();
            HttpSession session = request.getSession();
            session.setAttribute("loginMember", loginMember);
            return ResponseEntity.ok(loginMember.getNickname());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    @GetMapping("/formcheck/{key}/{value}")
    public ResponseEntity<String> formCheck(@PathVariable("key") String key,@PathVariable("value") String value){
        String check = memberservice.formCheck(key, value);
        return ResponseEntity.ok(check);
    }

    @GetMapping("/nickname/save/{nickname}")
    public ResponseEntity<?> nicknameSave(@PathVariable("nickname") String nickname, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Member member = (Member)session.getAttribute("loginMember");
        member.setNickname(nickname);
        if(memberservice.saveMember(member)){
            session.setAttribute("loginMember", member);
            return ResponseEntity.ok(member.getNickname());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
}
