package com.example.appstraining.towermeasurement.model;

public enum BuildingType {
	TOWER("Башня"),
	MAST("Мачта"),
	POLE("Столб");

	private static final String[] allTypes = {
			TOWER.buildingTypeRu,
			MAST.buildingTypeRu,
			POLE.buildingTypeRu
	};

	private String buildingTypeRu;
	//private BuildingType buildingType;

	public static BuildingType getBuildingType(String buildingTypeRu) {
		switch (buildingTypeRu) {
			case "Башня": return TOWER;
			case "Мачта": return MAST;
			case "Столб": return POLE;
		}
		return null;
	}

	BuildingType(String buildingTypeRu) {
		this.buildingTypeRu = buildingTypeRu;
	}

	public String getBuildingTypeRu() {
		return buildingTypeRu;
	};

	public static String[] getAllTypes(){
		return allTypes;
	}

}
