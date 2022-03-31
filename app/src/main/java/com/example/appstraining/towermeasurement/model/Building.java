package com.example.appstraining.towermeasurement.model;

import java.sql.Date;
import java.util.ArrayList;

public class Building {
	
	private Long id;
	private String name;
	private String address;
	private BuildingType type;
	private int config;
	private int numberOfSections;
	private int height;
	private int startLevel;
	private String userName;
	private Date creationDate;
	
	private ArrayList<Section> sections = new ArrayList<Section>();
	private ArrayList<Measurement> measurements = new ArrayList<Measurement>();
	private ArrayList<Result> results = new ArrayList<Result>();
	
	public Building() {
		
	}

	public Building(Long id, String name, String address, BuildingType type, int config,
					int numberOfSections, int height, int startLevel, String userName, Date creationDate) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.type = type;
		this.config = config;
		this.numberOfSections = numberOfSections;
		this.height = height;
		this.startLevel = startLevel;
		this.userName = userName;
		this.creationDate = creationDate;
	}

	public Building(Long id, String name, String address, BuildingType type, int config, int numberOfSections,
					int height, int startLevel, String userName, Date creationDate, ArrayList<Section> sections,
					ArrayList<Measurement> measurements, ArrayList<Result> results) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.type = type;
		this.config = config;
		this.numberOfSections = numberOfSections;
		this.height = height;
		this.startLevel = startLevel;
		this.userName = userName;
		this.sections.addAll(sections);
		this.measurements.addAll(measurements);
		this.results.addAll(results);
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public BuildingType getType() {
		return type;
	}

	public void setType(BuildingType type) {
		this.type = type;
	}

	public int getConfig() {
		return config;
	}

	public void setConfig(int config) {
		this.config = config;
	}

	public int getNumberOfSections() {
		return numberOfSections;
	}

	public void setNumberOfSections(int numberOfSections) {
		this.numberOfSections = numberOfSections;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getStartLevel() {
		return startLevel;
	}

	public void setStartLevel(int startLevel) {
		this.startLevel = startLevel;
	}
	
	public ArrayList<Section> getSections() {
		return sections;
	}

	public void setSections(ArrayList<Section> sections) {
		this.sections = sections;
	}

	public void updateSection(Section updateSection) {
		getSection(updateSection.getNumber()).setHeight(updateSection.getHeight());
		getSection(updateSection.getNumber()).setWidthBottom(updateSection.getWidthBottom());
		getSection(updateSection.getNumber()).setWidthTop(updateSection.getWidthTop());
	}

	public ArrayList<Measurement> getMeasurements() {
		return measurements;
	}

	public void setMeasurements(ArrayList<Measurement> measurements) {
		this.measurements = measurements;
	}
	
	
	public ArrayList<Result> getResults() {
		return results;
	}

	public void setResults(ArrayList<Result> results) {
		this.results = results;
	}

	public void addSection(Section section) {
		sections.add(section);
	}
	
	public Section getSection(int secNumber) {
		return sections.get(secNumber - 1);
	}
	
	public void addMeasurement(Measurement meas) {
		measurements.add(meas);
	}
	
	public Measurement getMeasurement(int sectionNumber, int side, BaseOrTop baseOrTop) {
		//Section section = 
		for(Measurement meas : measurements) {
			if(meas.getSectionNumber() == sectionNumber) {
				if(meas.getSide() == side && meas.getBaseOrTop().equals(baseOrTop)) {
					return meas;
				}
			}
		}
		
		return null;
	}
	
	public Result getResult(int measureId) {
		for(Result result : results) {
			if(result.getMeasureID() == measureId) return result;
		}
		return null;
	}

	public void addResultData(Result currentResult) {
		results.add(currentResult);
	}

	public boolean isNew() {
		return id == 0;
	}

	public int[] getLevels() {
		int[] levels = new int[getNumberOfSections() + 1];
		for (int sectionNum = 1; sectionNum < getNumberOfSections() + 1; sectionNum++) {
			levels[sectionNum - 1] = getSection(sectionNum).getLevel();
			if (sectionNum == getNumberOfSections()) {
				levels[sectionNum] = getSection(sectionNum).getLevel() + getSection(sectionNum).getHeight();
			}
		}
		return levels;
	}

	public int getShiftLimitBySection(int sectionNum) {
		double shiftLimitForBuildingType = 0.0;
		switch (getType()) {
			case TOWER:
				shiftLimitForBuildingType = 1 / 1000.0;
				break;
			case MAST:
				shiftLimitForBuildingType = 1 / 1500.0;
				break;
		}

		if (sectionNum > getNumberOfSections()) return (int)Math.round(getHeight() * shiftLimitForBuildingType);

		return (int)Math.round(getSection(sectionNum).getLevel() * shiftLimitForBuildingType);
	}
}
