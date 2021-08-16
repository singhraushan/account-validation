package com.rau.jp.service;

import com.rau.jp.dto.RequestDto;
import com.rau.jp.dto.ResponseDto;
import com.rau.jp.exception.CustomException;


public interface AccountValidationService {
     ResponseDto getProvidersResponse(RequestDto requestDto) throws CustomException;
}
