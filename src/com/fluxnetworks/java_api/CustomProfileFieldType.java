package com.fluxnetworks.java_api;

public enum CustomProfileFieldType {

	TEXT,
	TEXT_AREA,
	DATE;

	private static final CustomProfileFieldType[] VALUES = CustomProfileFieldType.values();

	public static CustomProfileFieldType fromFluxTypeInt(int FluxTypeInt) {
		return VALUES[FluxTypeInt - 1];
	}

}
