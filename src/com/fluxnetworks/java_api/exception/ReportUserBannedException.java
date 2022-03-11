package com.fluxnetworks.java_api.exception;

import com.fluxnetworks.java_api.ApiError;

public class ReportUserBannedException extends ApiErrorException {

	private static final long serialVersionUID = 1L;

	public ReportUserBannedException() {
		super(ApiError.USER_CREATING_REPORT_BANNED);
	}

}
