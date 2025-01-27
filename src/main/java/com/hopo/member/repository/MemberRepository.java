package com.hopo.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hopo._global.repository.HopoRepository;
import com.hopo.member.entity.Member;

public interface MemberRepository extends HopoRepository<Member>, JpaRepository<Member, Long> {
}
