package com.example.appstraining.towermeasurement.util;

import com.example.appstraining.towermeasurement.model.Building;
import com.example.appstraining.towermeasurement.model.Section;

import java.util.List;

public class BuildingStructureValidation {

    public static boolean isSectionDataConsistent(Section section) {
        if (section.getWidthBottom() > 0 && section.getWidthTop() > 0 && section.getHeight() > 0) {
            return true;
        }
        return false;
    }

    public static boolean isSectionListConsistent(List<Section> sectionList, int buildingHeight) {
        int sumOfSectionHeight = sectionList.stream().mapToInt(Section::getHeight).sum();
        if (buildingHeight == sumOfSectionHeight) {
            return true;
        }
        return false;
    }

    public static boolean isBuildingStructureConsistent(Building building) {
        return false;
    }

    //public static boolean
}
