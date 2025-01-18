package com.hopo.belong.repository;

import java.util.Optional;

import com.hopo._global.repository.HopoRepository;
import com.hopo.belong.entity.Belong;

public interface BelongRepository extends HopoRepository<Belong, String> {

	Optional<Belong> findByCode(String code);
}
