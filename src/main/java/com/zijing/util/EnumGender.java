package com.zijing.util;

public enum EnumGender {
	FEMALE("FEMALE", 0),
	MALE("MALE", 1);
	
	private final String type;
	private final int value;
	
	private EnumGender(String type, int value) {
		this.type = type;
		this.value = value;
	}
	
	public static EnumGender getEnumWithType(String type) {
		for(EnumGender gender: EnumGender.values()) {
			if(gender.getType().equals(type)) {
				return gender;
			}
		}
		return null;
	}
	
	public static EnumGender getEnumWithValue(int value) {
		for(EnumGender gender: EnumGender.values()) {
			if(gender.getValue() == value) {
				return gender;
			}
		}
		return null;
	}
	
	public String getType() {
		return type;
	}
	
	public int getValue() {
		return value;
	}
	
}
