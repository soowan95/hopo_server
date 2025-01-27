package com.hopo.service.space;

import org.mockito.Mock;

import com.hopo._config.annotation.ServiceTest;
import com.hopo.space.repository.SpaceRepository;

@ServiceTest
public class SpaceServiceTest {

	@Mock
	private SpaceRepository spaceRepository;
}
