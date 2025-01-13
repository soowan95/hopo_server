package com.hopo.space.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hopo.space.repository.SpaceRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SpaceServiceImpl implements SpaceService{

	private final SpaceRepository spaceRepository;
}
