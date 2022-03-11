package com.fluxnetworks.java_api.exception;

import com.fluxnetworks.java_api.ApiError;

public class CannotReportSelfException extends ApiErrorException {

	private static final long serialVersionUID = 1L;

	public CannotReportSelfException() {
		super(ApiError.CANNOT_REPORT_YOURSELF);
	}

}
