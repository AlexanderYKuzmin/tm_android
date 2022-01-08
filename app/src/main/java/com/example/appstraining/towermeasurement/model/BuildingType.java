package com.example.appstraining.towermeasurement.model;

public enum BuildingType {
	TOWER,
	MAST,
	POLE;

	private static final String[] allTypes =
			{String.valueOf(TOWER),
			 String.valueOf(MAST),
			String.valueOf(POLE)};

	public static String[] getAllTypes(){
		return allTypes;
	}
}
