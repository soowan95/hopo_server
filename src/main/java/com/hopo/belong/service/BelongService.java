package com.hopo.belong.service;

import com.hopo.belong.Belong;
import com.hopo.belong.dto.request.SaveBelongRequest;
import com.hopo.belong.dto.request.UpdateBelongRequest;

public interface BelongService {

	void save(SaveBelongRequest saveBelongRequest);
	Belong findByCode(String code);
	void update(UpdateBelongRequest updateBelongRequest);
	String makeCode();
}
