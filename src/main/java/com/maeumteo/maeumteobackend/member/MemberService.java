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

    public void joinMember(Member member) {
        memberRepository.save(member);
    }

    public Optional<Member> loginMember(String id, String password) {
        return memberRepository.findByIdAndPassword(id,password);
    }

    public String getUserPassword(String id) {
        return memberRepository.findById(id).get().getPassword();
    }
}
