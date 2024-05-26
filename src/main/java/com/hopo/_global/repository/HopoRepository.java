package com.hopo._global.repository;

import java.util.Optional;

public interface HopoRepository<E, K, V> {
	Optional<E> findByParam(K k, V v);
}
