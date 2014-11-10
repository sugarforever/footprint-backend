package com.wiysoft.footprint.backend.model;

import org.springframework.data.annotation.Id;

public class Footprint {

	@Id
	private String id;

	private String date;
	private String brief;
	private String content;
	private String[] uploadedImages;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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

	public String[] getUploadedImages() {
		return uploadedImages;
	}

	public void setUploadedImages(String[] uploadedImages) {
		this.uploadedImages = uploadedImages;
	}

}
