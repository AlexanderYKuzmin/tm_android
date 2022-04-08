package com.example.appstraining.towermeasurement.util;

import com.example.appstraining.towermeasurement.model.Building;
import com.example.appstraining.towermeasurement.model.Section;

import java.util.List;

public class BuildingStructureValidation {

    public static boolean isSectionDataConsistent(Section section) {
        return section.getWidthBottom() > 0 && section.getWidthTop() > 0 && section.getHeight() > 0;
    }

    public static boolean isSectionListConsistent(List<Section> sectionList, int buildingHeight) {
        int sumOfSectionHeight = sectionList.stream().mapToInt(Section::getHeight).sum();
        return buildingHeight == sumOfSectionHeight;
    }

    public static void checkAndRepairSectionListSecondaryData(List<Section> sectionList) {
        int level = 0;
        for (int i = 0; i < sectionList.size(); i++) {
            sectionList.get(i).setLevel(level);
            level += sectionList.get(i).getHeight();
        }
    }

    public static boolean isBuildingStructureConsistent(Building building) {
        return false;
    }

    //public static boolean
}
