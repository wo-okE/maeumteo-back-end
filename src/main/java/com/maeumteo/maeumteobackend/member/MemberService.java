package com.maeumteo.maeumteobackend.member;

import com.maeumteo.maeumteobackend.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Boolean checkid(String id){
        return !memberRepository.existsById(id);
    }

    public boolean saveMember(Member member) {
        Member saveMember = memberRepository.save(member);

        return saveMember != null;
    }

    public Optional<Member> loginMember(String id, String password) {
        return memberRepository.findByIdAndPassword(id,password);
    }

    public String getUserPassword(String id) {
        System.out.println(memberRepository.findById(id));
        return memberRepository.findById(id).get().getPassword();
    }

    public String formCheck(String key, String value){
        if(key.equals("nickname")){
            return memberRepository.existsByNickname(value) ? null : value;
        } else {
            return memberRepository.existsById(value) ? null : value;
        }
    }
}
