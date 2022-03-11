package com.fluxnetworks.java_api.exception;

import com.fluxnetworks.java_api.FluxException;

public class ApiDisabledException extends FluxException {

	public ApiDisabledException() {
		super("API is disabled, please enable it in StaffCP > Configuration > API");
	}

}
