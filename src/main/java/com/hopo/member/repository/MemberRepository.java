package com.hopo.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hopo._global.repository.HopoRepository;
import com.hopo.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, String>, HopoRepository<Member> {
}
