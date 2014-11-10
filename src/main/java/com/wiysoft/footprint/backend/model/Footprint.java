package com.wiysoft.footprint.backend.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Footprint {

	private static final Logger logger = Logger.getLogger(Footprint.class);

	@Id
	private String id;

	private String date;
	private Date isoDate;
	private String brief;
	private String content;
	private String[] images;
	private double latitude;
	private double longitude;

	public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy/MM/dd HH:mm");

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;

		try {
			this.isoDate = SDF.parse(this.date);
		} catch (ParseException e) {
			logger.error("Invalid date string: " + this.date, e);
		}
	}

	public Date getIsoDate() {
		return isoDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setIsoDate(Date isoDate) {
		this.isoDate = isoDate;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String[] getImages() {
		return images;
	}

	public void setImages(String[] images) {
		this.images = images;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append("Date: " + date + "\n");
		buff.append("Brief: " + brief + "\n");
		buff.append("Content: " + content + "\n");
		buff.append("Latitude: " + latitude + "\n");
		buff.append("Longitude: " + longitude + "\n");
		if (images != null) {
			for (String image : images) {
				buff.append("Image: " + image + "\n");
			}
		}

		return buff.toString();
	}
}
