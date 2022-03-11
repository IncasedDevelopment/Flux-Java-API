package com.fluxnetworks.java_api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ApiError extends FluxException {

	public static final int UNKNOWN_ERROR = 0;
	public static final int INVALID_API_KEY = 1;
	// 2 intentionally missing
	public static final int INVALID_API_METHOD = 3;
	public static final int NO_UNIQUE_SITE_ID_AVAILABLE = 4;
	// 5 intentionally missing
	public static final int INVALID_GET_POST_CONTENTS = 6;
	public static final int INVALID_EMAIL_ADDRESS = 7;
	public static final int INVALID_USERNAME = 8;
	public static final int INVALID_UUID = 9;
	public static final int EMAIL_ALREADY_EXISTS = 10;
	public static final int USERNAME_ALREADY_EXISTS = 11;
	public static final int UUID_ALREADY_EXISTS = 12;
	public static final int UNABLE_TO_CREATE_ACCOUNT = 13;
	public static final int UNABLE_TO_SEND_REGISTRATION_EMAIL = 14;
	// 15 intentionally missing
	public static final int UNABLE_TO_FIND_USER = 16;
	public static final int UNABLE_TO_FIND_GROUP = 17;
	// 18 intentionally missing
	public static final int REPORT_CONTENT_TOO_LARGE = 19;
	// 20 intentionally missing
	public static final int USER_CREATING_REPORT_BANNED = 21;
	public static final int USER_ALREADY_HAS_OPEN_REPORT = 22;
	// 23 intentionally missing
	public static final int UNABLE_TO_UPDATE_USERNAME = 24;
	public static final int UNABLE_TO_UPDATE_SERVER_INFO = 25;
	public static final int CANNOT_REPORT_YOURSELF = 26;
	public static final int INVALID_SERVER_ID = 27;
	public static final int INVALID_VALIDATE_CODE = 28;
	public static final int UNABLE_TO_SET_USER_DISCORD_ID = 29;
	public static final int UNABLE_TO_SET_DISCORD_BOT_URL = 30;
	// 31 intentionally missing
	public static final int ACCOUNT_ALREADY_ACTIVATED = 32;
	public static final int UNABLE_TO_SET_DISCORD_GUILD_ID = 33;
	public static final int DISCORD_INTEGRATION_DISABLED = 34;
	// 35 intentionally missing
	public static final int REQUEST_NOT_AUTHORIZED = 36;
	public static final int INVALID_INTEGRATION = 37;
	// 38 intentionally missing

	private static final long serialVersionUID = 3093028909912281912L;

	private final int code;
	private final @Nullable String meta;

	public ApiError(final int code, @Nullable String meta) {
		super("An unexpected API error occurred with error code " + code + " and " + (meta == null ? "no meta" : "meta " + meta));
		this.code = code;
		this.meta = meta;
	}

	public int getError() {
		return this.code;
	}

	public @NotNull Optional<@NotNull String> getMeta() {
		return Optional.ofNullable(meta);
	}

}
