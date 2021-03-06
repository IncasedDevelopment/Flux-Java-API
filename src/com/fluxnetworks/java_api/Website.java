package com.fluxnetworks.java_api;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.jetbrains.annotations.NotNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.fluxnetworks.java_api.exception.UnknownFluxVersionException;
import org.jetbrains.annotations.Nullable;

public class Website {


	private final @NotNull String version;
	private final @Nullable Update update;
	private final @NotNull String@NotNull[] modules;
	private final @NotNull String language;

	Website(@NotNull final JsonObject json) {
		Objects.requireNonNull(json, "Provided json object is null");

		this.version = json.get("flux_version").getAsString();

		this.modules = StreamSupport.stream(json.get("modules").getAsJsonArray().spliterator(), false)
				.map(JsonElement::getAsString)
				.toArray(String[]::new);

		if (json.has("version_update")) {
			final JsonObject updateJson = json.get("version_update").getAsJsonObject();
			final boolean updateAvailable = updateJson.get("update").getAsBoolean();
			if (updateAvailable) {
				final String updateVersion = updateJson.get("version").getAsString();
				final boolean isUrgent = updateJson.get("urgent").getAsBoolean();
				this.update = new Update(isUrgent, updateVersion);
			} else {
				this.update = null;
			}
		} else {
			this.update = null;
		}

		this.language = json.get("language").getAsString();
	}

	@NotNull
	public String getVersion() {
		return this.version;
	}

	@NotNull
	public FluxVersion getParsedVersion() throws UnknownFluxVersionException {
		return FluxVersion.parse(this.version);
	}

	/**
	 * @return Information about an update, or empty if no update is available.
	 */
	public @NotNull Optional<@NotNull Update> getUpdate() {
		return Optional.ofNullable(this.update);
	}

	public @NotNull String@NotNull [] getModules() {
		return this.modules;
	}

	public @NotNull String getLanguage() {
		return this.language;
	}

	public static class Update {

		private final boolean isUrgent;
		private final @NotNull String version;

		Update(final boolean isUrgent, @NotNull final String version) {
			this.isUrgent = isUrgent;
			this.version = version;
		}

		public boolean isUrgent() {
			return this.isUrgent;
		}

		@NotNull
		public String getVersion() {
			return this.version;
		}

		public FluxVersion getParsedVersion() throws UnknownFluxVersionException {
			return FluxVersion.parse(this.version);
		}

	}

}
