package com.fluxnetworks.java_api.exception;

import com.fluxnetworks.java_api.ApiError;

public class AlreadyHasOpenReportException extends ApiErrorException {

	private static final long serialVersionUID = 1L;

	public AlreadyHasOpenReportException() {
		super(ApiError.USER_ALREADY_HAS_OPEN_REPORT);
	}

}
