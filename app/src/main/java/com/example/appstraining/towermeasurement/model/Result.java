package com.example.appstraining.towermeasurement.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Result implements Parcelable {
	private int id;
	//private String objName;
	private double averageKL;
	private double averageKR;
	private double averageKLKR;
	private double shiftDegree;
	private int shiftMm;
	private double tanAlfa;
	private int distanceToSec;
	private int distanceDelta;
	private double betaAverage;
	private double betaI;
	private double betaDelta;
	
	private int buildingID;
	private int sectionID;
	private int measureID;
	
	public Result() {
		super();
	}

	public Result(int id, double averageKL, double averageKR, double averageKLKR, double shiftDegree,
			int shiftMm, double tanAlfa, int distanceToSec, int distanceDelta, double betaAverage, double betaI,
			double betaDelta, int buildingID, int sectionID, int measureID) {
		super();
		this.id = id;
		//this.objName = objName;
		this.averageKL = averageKL;
		this.averageKR = averageKR;
		this.averageKLKR = averageKLKR;
		this.shiftDegree = shiftDegree;
		this.shiftMm = shiftMm;
		this.tanAlfa = tanAlfa;
		this.distanceToSec = distanceToSec;
		this.distanceDelta = distanceDelta;
		this.betaAverage = betaAverage;
		this.betaI = betaI;
		this.betaDelta = betaDelta;
		this.buildingID = buildingID;
		this.sectionID = sectionID;
		this.measureID = measureID;
	}

	protected Result(Parcel in) {
		id = in.readInt();
		averageKL = in.readDouble();
		averageKR = in.readDouble();
		averageKLKR = in.readDouble();
		shiftDegree = in.readDouble();
		shiftMm = in.readInt();
		tanAlfa = in.readDouble();
		distanceToSec = in.readInt();
		distanceDelta = in.readInt();
		betaAverage = in.readDouble();
		betaI = in.readDouble();
		betaDelta = in.readDouble();
		buildingID = in.readInt();
		sectionID = in.readInt();
		measureID = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeDouble(averageKL);
		dest.writeDouble(averageKR);
		dest.writeDouble(averageKLKR);
		dest.writeDouble(shiftDegree);
		dest.writeInt(shiftMm);
		dest.writeDouble(tanAlfa);
		dest.writeInt(distanceToSec);
		dest.writeInt(distanceDelta);
		dest.writeDouble(betaAverage);
		dest.writeDouble(betaI);
		dest.writeDouble(betaDelta);
		dest.writeInt(buildingID);
		dest.writeInt(sectionID);
		dest.writeInt(measureID);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<Result> CREATOR = new Creator<Result>() {
		@Override
		public Result createFromParcel(Parcel in) {
			return new Result(in);
		}

		@Override
		public Result[] newArray(int size) {

			return new Result[size];
		}
	};

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getAverageKL() {
		return averageKL;
	}

	public void setAverageKL(double averageKL) {

		this.averageKL = (double)Math.round(averageKL*100) / 100;
	}

	public double getAverageKR() {
		return averageKR;
	}

	public void setAverageKR(double averageKR) {

		this.averageKR = (double)Math.round(averageKR*100) / 100;
	}

	public double getAverageKLKR() {
		return averageKLKR;
	}

	public void setAverageKLKR(double averageKLKR) {

		this.averageKLKR = (double) Math.round(averageKLKR*100) / 100;
	}

	public double getShiftDegree() {
		return shiftDegree;
	}

	public void setShiftDegree(double shiftDegree) {

		this.shiftDegree = (double)Math.round(shiftDegree * 1000) / 1000;
	}

	public int getShiftMm() {
		return shiftMm;
	}

	public void setShiftMm(int shiftMm) {
		this.shiftMm = shiftMm;
	}

	public double getTanAlfa() {
		return tanAlfa;
	}

	public void setTanAlfa(double tanAlfa) {
		this.tanAlfa = tanAlfa;
	}

	public int getDistanceToSec() {
		return distanceToSec;
	}

	public void setDistanceToSec(int distanceToSec) {
		this.distanceToSec = distanceToSec;
	}

	public int getDistanceDelta() {
		return distanceDelta;
	}

	public void setDistanceDelta(int distanceDelta) {
		this.distanceDelta = distanceDelta;
	}

	public double getBetaAverage() {
		return betaAverage;
	}

	public void setBetaAverage(double betaAverage) {
		this.betaAverage = betaAverage;
	}

	public double getBetaI() {
		return betaI;
	}

	public void setBetaI(double betaI) {
		this.betaI = betaI;
	}

	public double getBetaDelta() {
		return betaDelta;
	}

	public void setBetaDelta(double betaDelta) {
		this.betaDelta = (double)Math.round(betaDelta*1000) / 1000;
	}

	public int getBuildingID() {
		return buildingID;
	}

	public void setBuildingID(int buildingID) {
		this.buildingID = buildingID;
	}

	public int getSectionID() {
		return sectionID;
	}

	public void setSectionID(int sectionID) {
		this.sectionID = sectionID;
	}

	public int getMeasureID() {
		return measureID;
	}

	public void setMeasureID(int measureID) {
		this.measureID = measureID;
	}
	
	public void updateResult(double averageKL, double averageKR, double averageKLKR,
							 double shiftDegree, int shiftMm, double tanAlfa,
							 int distanceToSec, int distanceDelta) {
		setAverageKL(averageKL);
		setAverageKR(averageKR);
		setAverageKLKR(averageKLKR);
		setShiftDegree(shiftDegree);
		setShiftMm(shiftMm);
		setTanAlfa(tanAlfa);
		setDistanceToSec(distanceToSec);
		setDistanceDelta(distanceDelta);
	}
	
	
}
