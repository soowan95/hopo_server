package com.hopo._global.service;

import com.hopo._global.repository.HopoRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HopoService<R extends HopoRepository<E, V>, E, V> {

	private final R repository;

	protected boolean checkDuplicate(String property, V v) {
		return repository.findByParam(property, v).isPresent();
	}
}
