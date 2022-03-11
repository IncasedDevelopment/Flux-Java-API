package com.fluxnetworks.java_api;

public enum CustomProfileFieldType {

	TEXT,
	TEXT_AREA,
	DATE;

	private static final CustomProfileFieldType[] VALUES = CustomProfileFieldType.values();

	public static CustomProfileFieldType fromNamelessTypeInt(int namelessTypeInt) {
		return VALUES[namelessTypeInt - 1];
	}

}
