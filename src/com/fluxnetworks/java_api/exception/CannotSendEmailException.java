package com.fluxnetworks.java_api.exception;

import com.fluxnetworks.java_api.ApiError;

public class CannotSendEmailException extends ApiErrorException {

	private static final long serialVersionUID = 1L;

	public CannotSendEmailException() {
		super(ApiError.UNABLE_TO_SEND_REGISTRATION_EMAIL);
	}

}
