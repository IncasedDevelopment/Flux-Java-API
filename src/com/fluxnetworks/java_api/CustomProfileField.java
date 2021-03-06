package com.fluxnetworks.java_api;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CustomProfileField {

	private final int id;
	private final @NotNull String name;
	private final @NotNull CustomProfileFieldType type;
	private final boolean isPublic;
	private final boolean isRequired;
	private final @NotNull String description;

	CustomProfileField(final int id,
					   final @NotNull String name,
					   final @NotNull CustomProfileFieldType type,
					   final boolean isPublic,
					   final boolean isRequired,
					   final @NotNull String description) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.isPublic = isPublic;
		this.isRequired = isRequired;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public @NotNull String getName() {
		return name;
	}

	public @NotNull CustomProfileFieldType getType() {
		return type;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public boolean isRequired() {
		return isRequired;
	}

	public @NotNull String getDescription() {
		return description;
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof CustomProfileField &&
				((CustomProfileField) other).id == this.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

}
