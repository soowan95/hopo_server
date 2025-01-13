package com.hopo.space.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hopo.belong.entity.Belong;
import com.hopo.space.entity.Space;

public interface SpaceRepository extends JpaRepository<Space, Integer> {
	List<Space> findAllByBelong(Belong belong);
}
