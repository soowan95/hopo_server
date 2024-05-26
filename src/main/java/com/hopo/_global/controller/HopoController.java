package com.hopo._global.controller;

import com.hopo._global.repository.HopoRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HopoController<R extends HopoRepository<E, K, V>, E, K, V> {

	private final R repository;

	protected boolean checkDuplicate(K k, V v) {
		return repository.findByParam(k, v).isPresent();
	}
}
