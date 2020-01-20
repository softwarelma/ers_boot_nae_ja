package com.softwarelma.ers_boot.pojo;

import java.util.ArrayList;
import java.util.List;

import com.softwarelma.ers_boot.dto.NaeAnnotationDto;

public class NaeDocPojoAI {

	public List<NaeAnnotationDto> retrieveListAnnotationFake(List<String> listParagraph) {
		List<NaeAnnotationDto> listAnnotation = new ArrayList<NaeAnnotationDto>();
		NaeAnnotationDto annotation = new NaeAnnotationDto();
		annotation.setBegin(112);
		annotation.setEnd(156);
		annotation.setParag(this.retrieveParagraph(listParagraph));
		annotation.setMark("#cfff04");
		listAnnotation.add(annotation);
		return listAnnotation;
	}

	private int retrieveParagraph(List<String> listParagraph) {
		for (int par = 0; par < listParagraph.size(); par++) {
			if (listParagraph.get(par).contains("Mi regreso a casa fue algo nuevo, algo como para observar, con la camiseta llena de basura en mi pecho que"))
				return par;
		}
		throw new RuntimeException("paragraph not found");
	}

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

	/**
	 * colors from: http://hex.wikimix.info/en/color-0203e2
	 */
	private String retrieveMarkByCode(int code) {
		switch (code) {
		case 0:
			return "#bc13fe";// neon purple
		case 1:
			return "#7b0323";// wine red
		case 2:
			return "#fe420f";// orangered
		case 3:
			return "#cfff04";// neon yellow
		case 4:
			return "#089404";// true green
		case 5:
			return "#0203e2";// pure blue
		case 6:
			return "#8b3103";// rust brown
		default:
			throw new RuntimeException("Mark code non valido");
		}
	}

}
