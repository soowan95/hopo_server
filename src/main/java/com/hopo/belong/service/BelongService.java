package com.hopo.belong.service;

import com.hopo.belong.dto.request.SaveBelongRequest;
import com.hopo.belong.dto.request.UpdateBelongRequest;
import com.hopo.belong.dto.response.FamilyNameResponse;
import com.hopo.belong.dto.response.CodeResponse;
import com.hopo.belong.dto.response.SaveBelongResponse;
import com.hopo.belong.entity.Belong;

public interface BelongService {

	SaveBelongResponse save(SaveBelongRequest saveBelongRequest);
	Belong findByCode(String code);
	void update(UpdateBelongRequest updateBelongRequest);
	CodeResponse makeCode(String address);
	FamilyNameResponse findFamilyName(String address);
}
