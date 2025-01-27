package com.hopo.space.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hopo._global.repository.HopoRepository;
import com.hopo.belong.entity.Belong;
import com.hopo.space.entity.Space;

public interface SpaceRepository extends HopoRepository<Space>, JpaRepository<Space, Long> {
	List<Space> findAllByBelong(Belong belong);
}
