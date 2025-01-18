package com.hopo._global.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HopoRepository<E,ID> extends CustomHopoRepository<E>, JpaRepository<E, ID> {
}
