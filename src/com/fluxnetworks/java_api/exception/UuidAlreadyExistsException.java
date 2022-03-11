package com.fluxnetworks.java_api.exception;

import com.fluxnetworks.java_api.ApiError;

public class UuidAlreadyExistsException extends ApiErrorException {

	private static final long serialVersionUID = 1L;

	public UuidAlreadyExistsException() {
		super(ApiError.UUID_ALREADY_EXISTS);
	}

}
