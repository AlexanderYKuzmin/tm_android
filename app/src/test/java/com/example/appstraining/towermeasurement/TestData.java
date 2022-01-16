package com.example.appstraining.towermeasurement;

import com.example.appstraining.towermeasurement.model.Building;
import com.example.appstraining.towermeasurement.model.BuildingType;

import java.sql.Date;

public class TestData {

    public static final Building testBuilding = new Building(
            2,
            "KO223_2", // name
            "Республика Коми, Прилузский район, с Вухтым", // address
            BuildingType.TOWER, // type
            3, // configuration
            9,  // quantity sections
            72000, // height
            0,   // startLevel
            null, // Contractor
            null // Creation date
    );
}
