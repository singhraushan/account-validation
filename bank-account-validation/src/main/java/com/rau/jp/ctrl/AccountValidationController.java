package com.rau.jp.ctrl;

import com.rau.jp.dto.RequestDto;
import com.rau.jp.dto.ResponseDto;
import com.rau.jp.exception.CustomException;
import com.rau.jp.service.AccountValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountValidationController {

    @Autowired
    AccountValidationService accountValidationService;

    @RequestMapping(value = "/account_validation", method = RequestMethod.GET)
    public ResponseEntity<ResponseDto> getProvidersResponse(@RequestBody RequestDto requestDto) throws CustomException {
        return ResponseEntity.ok(accountValidationService.getProvidersResponse(requestDto));
    }
}
