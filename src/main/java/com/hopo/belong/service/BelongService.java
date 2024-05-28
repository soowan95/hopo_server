package com.hopo.belong.service;

import com.hopo._global.service.HopoService;
import com.hopo.belong.Belong;
import com.hopo.belong.BelongRepository;
import com.hopo.belong.dto.request.SaveBelongRequest;
import com.hopo.belong.dto.request.UpdateBelongRequest;

public interface BelongService {

	void save(SaveBelongRequest saveBelongRequest);
	Belong findByCode(String code);
	void update(UpdateBelongRequest updateBelongRequest);
	String makeCode(String address);
}
