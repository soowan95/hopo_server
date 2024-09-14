package com.hopo.service.space;

import static org.assertj.core.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.hopo._config.annotation.ServiceTest;
import com.hopo.space.repository.SpaceRepository;
import com.hopo.space.service.SpaceServiceImpl;

@ServiceTest
public class SpaceServiceTest {

	@InjectMocks
	private SpaceServiceImpl spaceService;

	@Mock
	private SpaceRepository spaceRepository;
}
