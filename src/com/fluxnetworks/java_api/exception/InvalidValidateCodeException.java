package com.fluxnetworks.java_api.exception;

import com.fluxnetworks.java_api.ApiError;

public class InvalidValidateCodeException extends ApiErrorException {

	private static final long serialVersionUID = 1L;

	public InvalidValidateCodeException() {
		super(ApiError.INVALID_VALIDATE_CODE);
	}

}
