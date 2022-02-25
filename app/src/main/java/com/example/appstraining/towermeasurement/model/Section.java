package com.example.appstraining.towermeasurement.model;

public class Section {
	private int id;
	private int number;
	private int widthBottom;
	private int widthTop;
	private int height;
	private int level;
	private String name;
	private Long building_id;
	
	public Section() {
		
	}

	public Section(int id, int number, int widthBottom, int widthTop, int height, int level,
				   String name, Long building_id) {
		super();
		this.id = id;
		this.number = number;
		this.widthBottom = widthBottom;
		this.widthTop = widthTop;
		this.height = height;
		this.level = level;
		this.name = name;
		this.building_id = building_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getWidthBottom() {
		return widthBottom;
	}

	public void setWidthBottom(int widthBottom) {
		this.widthBottom = widthBottom;
	}

	public int getWidthTop() {
		return widthTop;
	}

	public void setWidthTop(int widthTop) {
		this.widthTop = widthTop;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getBuilding_id() {
		return building_id;
	}

	public void setBuilding_id(Long building_id) {
		this.building_id = building_id;
	}
	
}
