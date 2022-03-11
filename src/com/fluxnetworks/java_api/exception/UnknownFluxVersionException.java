package com.fluxnetworks.java_api.exception;

public class UnknownFluxVersionException extends Exception {

	private static final long serialVersionUID = 1L;

	public UnknownFluxVersionException(final String versionString) {
		super("Cannot parse version string '" + versionString + "'. Try updating the API or the software using it.");
	}

}
