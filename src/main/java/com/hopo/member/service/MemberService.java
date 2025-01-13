package com.hopo.member.service;

import com.hopo.member.dto.request.SignUpRequest;
import com.hopo.member.dto.response.MemberResponse;

public interface MemberService {
	MemberResponse signUp(SignUpRequest request);
}
