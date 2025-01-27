package com.hopo.belong.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hopo._global.repository.HopoRepository;
import com.hopo.belong.entity.Belong;

public interface BelongRepository extends HopoRepository<Belong>, JpaRepository<Belong, Long> {

	Optional<Belong> findByCode(String code);
}
