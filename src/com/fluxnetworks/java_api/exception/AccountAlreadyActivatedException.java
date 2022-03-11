package com.fluxnetworks.java_api.exception;

import com.fluxnetworks.java_api.ApiError;

public class AccountAlreadyActivatedException extends ApiErrorException {

	private static final long serialVersionUID = 1L;

	public AccountAlreadyActivatedException() {
		super(ApiError.ACCOUNT_ALREADY_ACTIVATED);
	}

}
