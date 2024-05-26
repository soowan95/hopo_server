package com.hopo.belong;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hopo._global.repository.HopoRepository;

public interface BelongRepository extends JpaRepository<Belong, String>, HopoRepository<Belong, String, String> {

	Optional<Belong> findByCode(String code);
}
