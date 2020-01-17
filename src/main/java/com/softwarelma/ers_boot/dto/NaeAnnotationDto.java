package com.softwarelma.ers_boot.dto;

import java.io.Serializable;

public class NaeAnnotationDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private int parag;
	private int begin;
	private int end;
	private String mark;

	public NaeAnnotationDto() {
	}

	public NaeAnnotationDto(int parag, int begin, int end, String mark) {
		super();
		this.parag = parag;
		this.begin = begin;
		this.end = end;
		this.mark = mark;
	}

	public int getParag() {
		return parag;
	}

	public void setParag(int parag) {
		this.parag = parag;
	}

	public int getBegin() {
		return begin;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

}
