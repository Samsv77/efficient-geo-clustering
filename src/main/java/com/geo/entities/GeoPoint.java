package com.geo.entities;

public class GeoPoint {

	private Double latitude;
	
	private Double longitude;
	
	private Double value = 0d;

	
	public GeoPoint() { }
	
	public GeoPoint(Double latitude, Double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public GeoPoint(Double latitude, Double longitude, Double value) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.value = value;
	}
	
	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
	
}
