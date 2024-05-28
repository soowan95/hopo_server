package com.hopo._global.repository;

import java.util.Optional;

public interface HopoRepository<E,  V> {
	Optional<E> findByParam(String property, V v);
}
