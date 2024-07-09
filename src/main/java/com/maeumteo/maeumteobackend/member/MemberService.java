package com.maeumteo.maeumteobackend.member;

import com.maeumteo.maeumteobackend.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public Member loginMember(Member member) {
        return memberRepository.findByIdAndPassword(member.getId(),member.getPassword()).get();
    }
}
