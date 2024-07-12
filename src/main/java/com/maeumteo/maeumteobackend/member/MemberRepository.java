package com.maeumteo.maeumteobackend.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,String> {
    Optional<Member> findByIdAndPassword(String id, String password);

    boolean existsByNickname(String value);

}
