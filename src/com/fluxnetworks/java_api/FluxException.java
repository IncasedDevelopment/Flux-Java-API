package com.fluxnetworks.java_api;

import org.jetbrains.annotations.NotNull;

/**
 * Generic exception thrown by many methods in the Flux API
 */
public class FluxException extends Exception {

	private static final long serialVersionUID = -3698433855091611529L;

	public FluxException(@NotNull final String message) {
		super(message);
	}

	public FluxException(@NotNull final String message, @NotNull final Throwable cause) {
		super(message, cause);
	}

	public FluxException(@NotNull final Throwable cause) {
		super(cause);
	}

	public FluxException() {
		super();
	}

}
