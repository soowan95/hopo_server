package com.hopo.belong.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hopo._global.repository.HopoRepository;
import com.hopo.belong.entity.Belong;

public interface BelongRepository extends JpaRepository<Belong, String>, HopoRepository<Belong> {

	Optional<Belong> findByCode(String code);
}
