package com.softwarelma.ers_boot.pojo;

import java.util.ArrayList;
import java.util.List;

import com.softwarelma.ers_boot.dto.NaeAnnotationDto;

public class NaeDocPojoAI {

	public List<NaeAnnotationDto> retrieveListAnnotation(List<String> listParagraph) {
		List<NaeAnnotationDto> listAnnotation = new ArrayList<NaeAnnotationDto>();
		for (int i = 0; i < listParagraph.size(); i++) {
			String paragraph = listParagraph.get(i);
			if (paragraph == null || paragraph.trim().length() < 5)
				continue;
			listAnnotation.addAll(this.retrieveListAnnotationByParagraph(paragraph, i));
		}
		return listAnnotation;
	}

	private List<NaeAnnotationDto> retrieveListAnnotationByParagraph(String paragraph, int parIndex) {
		List<NaeAnnotationDto> listAnnotation = new ArrayList<NaeAnnotationDto>();
		int begin = (int) (Math.random() * (double) paragraph.length());
		int end = (int) (Math.random() * (double) paragraph.length());
		if (begin > end) {
			int ind = begin;
			begin = end;
			end = ind;
		} else if (begin == end) {
			if (begin < paragraph.length() / 2)
				end++;
			else
				begin--;
		}
		NaeAnnotationDto annotation = new NaeAnnotationDto();
		annotation.setBegin(begin);
		annotation.setEnd(end);
		annotation.setMark(this.retrieveMark(paragraph, parIndex));
		annotation.setParag(parIndex);
		listAnnotation.add(annotation);
		return listAnnotation;
	}

	private String retrieveMark(String paragraph, int parIndex) {
		int code = ((int) (Math.random() * 37.0)) % 7;
		return this.retrieveMarkByCode(code);
	}

	private String retrieveMarkByCode(int code) {
		switch (code) {
		case 0:
			return "grey";
		case 1:
			return "red";
		case 2:
			return "orange";
		case 3:
			return "yellow";
		case 4:
			return "green";
		case 5:
			return "blue";
		case 6:
			return "brown";
		default:
			throw new RuntimeException("Mark code non valido");
		}
	}

}
