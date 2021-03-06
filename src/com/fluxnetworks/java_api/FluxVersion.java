package com.fluxnetworks.java_api;

import com.fluxnetworks.java_api.exception.UnknownFluxVersionException;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum FluxVersion {

	V2_0_0_PR_7("2.0.0-pr7", "2.0.0 pre-release 7", 2, 0, true),
	V2_0_0_PR_8("2.0.0-pr8", "2.0.0 pre-release 8", 2, 0, true),
	V2_0_0_PR_9("2.0.0-pr9", "2.0.0 pre-release 9", 2, 0, true),
	V2_0_0_PR_10("2.0.0-pr10", "2.0.0 pre-release 10", 2, 0, true),
	V2_0_0_PR_11("2.0.0-pr11", "2.0.0 pre-release 11", 2, 0, true),
	V2_0_0_PR_12("2.0.0-pr12", "2.0.0 pre-release 12", 2, 0, true),
	V2_0_0_PR_13("2.0.0-pr13", "2.0.0 pre-release 13", 2, 0, true),

	;

	private final @NotNull String name;
	private final @NotNull String friendlyName;
	private final int major;
	private final int minor;
	private final boolean isBeta;

	@SuppressWarnings("SameParameterValue")
	FluxVersion(@NotNull final String name, @NotNull String friendlyName, final int major, final int minor, final boolean isBeta) {
		this.name = name;
		this.friendlyName = friendlyName;
		this.major = major;
		this.minor = minor;
		this.isBeta = isBeta;
	}

	public @NotNull String getName() {
		return this.name;
	}

	public @NotNull String getFriendlyName() {
		return this.friendlyName;
	}

	public int getMajor() {
		return this.major;
	}

	public int getMinor() {
		return this.minor;
	}

	/**
	 * @return True if this version is a release candidate, pre-release, beta, alpha.
	 */
	public boolean isBeta() {
		return this.isBeta;
	}

	@Override
	public String toString() {
		return this.friendlyName;
	}

	private static final Map<String, FluxVersion> BY_NAME = new HashMap<>();

	static {
		for (final FluxVersion version : values()) {
			BY_NAME.put(version.getName(), version);
		}
	}

	public static @NotNull FluxVersion parse(@NotNull final String versionName) throws UnknownFluxVersionException {
		Objects.requireNonNull(versionName, "Version name is null");
		final FluxVersion version = BY_NAME.get(versionName);
		if (version == null) {
			throw new UnknownFluxVersionException(versionName);
		}
		return version;
	}

}
