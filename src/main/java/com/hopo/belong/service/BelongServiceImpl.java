package com.hopo.belong.service;

import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hopo._global.exception.HttpCodeHandleException;
import com.hopo._global.service.HopoService;
import com.hopo.belong.dto.request.SaveBelongRequest;
import com.hopo.belong.dto.request.UpdateBelongRequest;
import com.hopo.belong.dto.response.CodeResponse;
import com.hopo.belong.dto.response.FamilyNameResponse;
import com.hopo.belong.dto.response.SaveBelongResponse;
import com.hopo.belong.entity.Belong;
import com.hopo.belong.repository.BelongRepository;

@Service
@Transactional
public class BelongServiceImpl extends HopoService<Belong, String> implements BelongService {

	private final BelongRepository belongRepository;

	public BelongServiceImpl(BelongRepository belongRepository) {
		super(belongRepository);
		this.belongRepository = belongRepository;
	}

	@Override
	public SaveBelongResponse save(SaveBelongRequest saveBelongRequest) {
		checkDuplicate("code", saveBelongRequest.getCode());

		Belong belong = belongRepository.save(new SaveBelongRequest().map(saveBelongRequest));

		return new SaveBelongResponse().of(belong);
	}

	@Override
	public Belong findByCode(String code) {
		return belongRepository.findByCode(code).orElseThrow(() -> new HttpCodeHandleException("NOT_EXIST_CODE"));
	}

	@Override
	public void update(UpdateBelongRequest updateBelongRequest) {
		Belong belong = findByCode(updateBelongRequest.getCode());

		belong.setAddress(updateBelongRequest.getAddress());

		belongRepository.save(belong);
	}

	@Override
	public CodeResponse makeCode(String address) {
		StringBuilder newCode = new StringBuilder();
		String specialChar = "!@#$%=";

		while (newCode.length() <= 9) {
			double random = Math.random();

			if (random < 0.25) newCode.append(new Random().nextInt(10));
			else if (random < 0.5) newCode.append((char) (new Random().nextInt(26) + 'a'));
			else if (random < 0.75) newCode.append((char) (new Random().nextInt(26) + 'A'));
			else newCode.append(specialChar.charAt(new Random().nextInt(specialChar.length())));
		}

		belongRepository.findByCode(newCode.toString()).ifPresent(a -> makeCode(address));

		return CodeResponse.builder()
			.code(newCode.toString())
			.build();
	}

	@Override
	public FamilyNameResponse findFamilyName(String address) {
		Belong belong = belongRepository.findByParam("address", address).orElseThrow(() -> new HttpCodeHandleException("NOT_EXIST_BELONG"));
		return FamilyNameResponse.builder().familyNames(belong.getSpaces().stream().map(a -> a.getMember().getMaskData("name")).toList()).build();
	}
}
