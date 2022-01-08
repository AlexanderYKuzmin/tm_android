package com.example.appstraining.towermeasurement.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.appstraining.towermeasurement.model.BaseOrTop;
import com.example.appstraining.towermeasurement.model.CircleTheo;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class Measurement implements Parcelable {
	private int id;
	//private String objName;
	private Date date;
	private String contractor;

	private int number;
	private int side;
	private CircleTheo circle;
	private double leftAngle, rightAngle;
	private int theoHeight;
	private int distance;
	private int sectionNumber;
	private BaseOrTop baseOrTop;
	private int sectionId, buildingId;
	private int azimuth;

	SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	
	public Measurement() {
		super();
	}

	public Measurement(int id, int number, int side, CircleTheo circle, double leftangle,
			double rightangle, int theoHeight, int distance, int sectionNumber, BaseOrTop baseOrTop, int sectionId,
			int buildingId, Date date, String contractor, int azimuth) {
		super();
		this.id = id;
		//this.objName = name;
		this.number = number;
		this.side = side;
		this.circle = circle;
		this.leftAngle = leftangle;
		this.rightAngle = rightangle;
		this.theoHeight = theoHeight;
		this.distance = distance;
		this.sectionNumber = sectionNumber;
		this.baseOrTop = baseOrTop;
		this.sectionId = sectionId;
		this.buildingId = buildingId;
		this.date = date;
		this.contractor = contractor;
		this.azimuth = azimuth;
	}

	protected Measurement(Parcel in) {
		id = in.readInt();
		contractor = in.readString();
		number = in.readInt();
		side = in.readInt();
		leftAngle = in.readDouble();
		rightAngle = in.readDouble();
		theoHeight = in.readInt();
		distance = in.readInt();
		sectionNumber = in.readInt();
		sectionId = in.readInt();
		buildingId = in.readInt();
		azimuth = in.readInt();
		circle = CircleTheo.valueOf(in.readString());
		try {
			date = new Date(dateFormat.parse(in.readString()).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		baseOrTop = BaseOrTop.valueOf(in.readString());

	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(contractor);
		dest.writeInt(number);
		dest.writeInt(side);
		dest.writeDouble(leftAngle);
		dest.writeDouble(rightAngle);
		dest.writeInt(theoHeight);
		dest.writeInt(distance);
		dest.writeInt(sectionNumber);
		dest.writeInt(sectionId);
		dest.writeInt(buildingId);
		dest.writeInt(azimuth);
		dest.writeString(String.valueOf(circle));
		dest.writeString(dateFormat.format(date));
		dest.writeString(String.valueOf(baseOrTop));
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<Measurement> CREATOR = new Creator<Measurement>() {
		@Override
		public Measurement createFromParcel(Parcel in) {
			return new Measurement(in);
		}

		@Override
		public Measurement[] newArray(int size) {
			return new Measurement[size];
		}
	};

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

	public int getSide() {
		return side;
	}

	public void setSide(int side) {
		this.side = side;
	}

	public CircleTheo getCircle() {
		return circle;
	}

	public void setCircle(CircleTheo circle) {
		this.circle = circle;
	}

	public double getLeftAngle() {
		return leftAngle;
	}

	public void setLeftAngle(double leftangle) {

		this.leftAngle = (double)Math.round(leftangle*1000) / 1000;
	}

	public double getRightAngle() {
		return rightAngle;
	}

	public void setRightAngle(double rightangle) {

		this.rightAngle = (double)Math.round(rightangle*1000) / 1000;
	}

	public int getTheoHeight() {
		return theoHeight;
	}

	public void setTheoHeight(int theoHeight) {
		this.theoHeight = theoHeight;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getSectionNumber() {
		return sectionNumber;
	}

	public void setSectionNumber(int sectionNumber) {
		this.sectionNumber = sectionNumber;
	}

	public BaseOrTop getBaseOrTop() {
		return baseOrTop;
	}

	public void setBaseOrTop(BaseOrTop baseOrTop) {
		this.baseOrTop = baseOrTop;
	}

	public int getSectionId() {
		return sectionId;
	}

	public void setSectionId(int sectionId) {
		this.sectionId = sectionId;
	}

	public int getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(int buildingId) {
		this.buildingId = buildingId;
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getContractor() {
		return contractor;
	}

	public void setContractor(String contractor) {
		this.contractor = contractor;
	}

	public int getAzimuth() {
		return azimuth;
	}

	public void setAzimuth(int azimuth) {
		this.azimuth = azimuth;
	}
	
	
}
