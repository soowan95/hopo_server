package com.hopo.belong.service;

import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hopo._global.exception.CustomException;
import com.hopo._global.exception.CustomExceptionEnum;
import com.hopo.belong.Belong;
import com.hopo.belong.BelongRepository;
import com.hopo.belong.dto.request.SaveBelongRequest;
import com.hopo.belong.dto.request.UpdateBelongRequest;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BelongServiceImpl implements BelongService {

	private final BelongRepository belongRepository;

	@Override
	public void save(SaveBelongRequest saveBelongRequest) {
		Belong belong = Belong.builder()
			.code(saveBelongRequest.getCode())
			.address(saveBelongRequest.getAddress())
			.isCompany(saveBelongRequest.isCompany())
			.build();

		belongRepository.save(belong);
	}

	@Override
	public Belong findByCode(String code) {
		return belongRepository.findByCode(code).orElseThrow(() -> new CustomException(CustomExceptionEnum.NOT_EXIST_CODE));
	}

	@Override
	public void update(UpdateBelongRequest updateBelongRequest) {
		Belong belong = findByCode(updateBelongRequest.getCode());

		belong.setAddress(updateBelongRequest.getAddress());

		belongRepository.save(belong);
	}

	@Override
	public String makeCode() {
		StringBuilder code = new StringBuilder();
		String specialChar = "!@#$%=";

		while (code.length() <= 9) {
			double random = Math.random();

			if (random < 0.25) code.append(new Random().nextInt(10));
			else if (random < 0.5) code.append((char) (new Random().nextInt(26) + 'a'));
			else if (random < 0.75) code.append((char) (new Random().nextInt(26) + 'A'));
			else code.append(specialChar.charAt(new Random().nextInt(specialChar.length())));
		}

		belongRepository.findByCode(code.toString()).ifPresent(a -> makeCode());

		return code.toString();
	}
}
