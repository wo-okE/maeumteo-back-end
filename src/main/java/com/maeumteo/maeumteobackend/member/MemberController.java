package com.maeumteo.maeumteobackend.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberservice;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/join")
    public ResponseEntity<?> joinMember(@RequestBody Member member, HttpServletRequest request){
        member.setPassword(passwordEncoder.encode(member.getPassword()));

        if(memberservice.saveMember(member)){
            Member loginMember = memberservice.loginMember(member.getId(), member.getPassword()).get();
            return getResponseEntity(request, loginMember);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    @PostMapping("/naverJoin")
    public ResponseEntity<?> naverJoinMember(@RequestBody Member member, HttpServletRequest request){
        if(memberservice.saveMember(member)){
            return getResponseEntity(request, member);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
    @PostMapping("/naverLogin")
    public ResponseEntity<?> naverLoginMember(@RequestBody Member member, HttpServletRequest request) {
        Member loginMember = memberservice.socialLogin(member);
        HttpSession session = request.getSession();
        session.setAttribute("loginMember", member);

        return ResponseEntity.ok(new Member(loginMember.getUsername(),loginMember.getNickname()));
    }

    @PostMapping("/kakao/callback")
    public ResponseEntity<?> kakaoLoginCallback(@RequestBody String code) throws IOException {
        JSONObject jsonObject = new JSONObject(code);
        code = jsonObject.getString("code");
        System.out.println("====================================");
        System.out.println(code);
        System.out.println("====================================");
        String access_token = memberservice.kakaoLogin(code);
        Map<String, Object> userInfo = memberservice.getUserInfo(access_token);
        //값 꺼내오기위한 처리
        if(userInfo != null){
            System.out.println(userInfo.toString());
        }
        return null;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginMember(@RequestBody LoginRequest loginRequest, HttpServletRequest request){
        String storedPasswordHash = memberservice.getUserPassword(loginRequest.getId());
        boolean passwordMatches = passwordEncoder.matches(loginRequest.getPassword(), storedPasswordHash);
        if(passwordMatches) {
            Member loginMember = memberservice.loginMember(loginRequest.getId(), storedPasswordHash).get();
            return getResponseEntity(request, loginMember);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    @GetMapping("/formcheck/{key}/{value}")
    public ResponseEntity<String> formCheck(@PathVariable("key") String key,@PathVariable("value") String value){
        String check = memberservice.formCheck(key, value);
        return ResponseEntity.ok(check);
    }

    @PostMapping("/checkid")
    public Boolean checkid(@RequestBody Member member){
        return memberservice.checkid(member.getId());
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

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        if(session != null) {
            session.invalidate();
            return ResponseEntity.ok("Logged out Successfully");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    @PostMapping("/authentication")
    public ResponseEntity<Member> loginMemberAuthentication(HttpServletRequest request){
        HttpSession session = request.getSession();
        Member loginMember = (Member) session.getAttribute("loginMember");

        if(loginMember == null){
            return ResponseEntity.ok(new Member());
        }
        return ResponseEntity.ok(new Member(loginMember.getUsername(),loginMember.getNickname()));
    }


    private ResponseEntity<?> getResponseEntity(HttpServletRequest request, Member loginMember) {
        HttpSession session = request.getSession();
        session.setAttribute("loginMember", loginMember);
        Map<String, Object> result = new HashMap<>();
        if(loginMember.getNickname() == null){
            result.put("loginUsername",loginMember.getUsername());
        } else {
            result.put("loginNickname",loginMember.getNickname());
        }
        return ResponseEntity.ok(result);
    }
}
