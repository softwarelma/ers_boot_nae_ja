package com.softwarelma.ers_boot.pojo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd;

import com.softwarelma.ers_boot.dto.NaeAnnotationDto;

public class NaeDocPojoWriter {

	public String retrieveBase64Docx(List<String> listParagraph, List<NaeAnnotationDto> listAnnotation) throws IOException {
		XWPFDocument doc = new XWPFDocument();
		for (int i = 0; i < listParagraph.size(); i++) {
			List<NaeAnnotationDto> listAnnotationByParagraph = this.retrieveListAnnotationByParagraph(i, listAnnotation);
			this.addParagraphWithAnnotations(i, doc, listParagraph.get(i), listAnnotationByParagraph);
		}
		// doc.write(new FileOutputStream("C:\\Users\\guillermo.ellison\\Downloads\\WordRunWithBGColor.docx"));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		doc.write(baos);
		return baos.toString();
	}

	private void addParagraphWithAnnotations(int paragInd, XWPFDocument doc, String paragraph, List<NaeAnnotationDto> listAnnotation) {
		this.validateParagraph(paragInd, paragraph, listAnnotation);
		if (paragraph == null || paragraph.trim().isEmpty())
			return;
		XWPFParagraph docParagraph = doc.createParagraph();
		int insertInd = 0;
		for (NaeAnnotationDto annotation : listAnnotation)
			insertInd = this.addAnnotation(docParagraph, paragInd, insertInd, paragraph, annotation);
	}

	private void validateParagraph(int paragInd, String paragraph, List<NaeAnnotationDto> listAnnotation) {
		if ((paragraph == null || paragraph.trim().isEmpty()) && !listAnnotation.isEmpty())
			throw new RuntimeException("Non trovato il testo del paragrafo numero " + (paragInd + 1) + ", contenente delle annotazioni");
	}

	private void validateAnnotation(int paragInd, int insertInd, String paragraph, NaeAnnotationDto annotation) {
		if (insertInd < 0 || insertInd > paragraph.length())
			throw new RuntimeException("Indice d'inserimento non valido (" + insertInd + ") nel paragrafo numero " + (paragInd + 1));
		if (annotation == null)
			throw new RuntimeException("Non trovata l'annotazione nel paragrafo numero " + (paragInd + 1));
		if (annotation.getBegin() < insertInd || annotation.getBegin() >= annotation.getEnd())
			throw new RuntimeException(
					"Indici d'annotazione non validi (" + annotation.getBegin() + ", " + annotation.getEnd() + ") nel paragrafo numero " + (paragInd + 1));
		if (annotation.getEnd() > paragraph.length())
			throw new RuntimeException("Indice finale d'annotazione non valido (" + annotation.getEnd() + ") nel paragrafo numero " + (paragInd + 1));
	}

	private int addAnnotation(XWPFParagraph docParagraph, int paragInd, int insertInd, String paragraph, NaeAnnotationDto annotation) {
		this.validateAnnotation(paragInd, insertInd, paragraph, annotation);
		XWPFRun run;
		if (insertInd < annotation.getBegin()) {
			run = docParagraph.createRun();
			run.setText(paragraph.substring(insertInd, annotation.getBegin()));
			insertInd = annotation.getBegin();
		}
		run = docParagraph.createRun();
		run.setText(paragraph.substring(insertInd, annotation.getEnd()));
		insertInd = annotation.getEnd();
		CTShd cTShd = run.getCTR().addNewRPr().addNewShd();
		cTShd.setVal(STShd.CLEAR);
		cTShd.setColor("auto");
		cTShd.setFill(annotation.getMark());
		return insertInd;
	}

	private List<NaeAnnotationDto> retrieveListAnnotationByParagraph(int parIndex, List<NaeAnnotationDto> listAnnotation) {
		List<NaeAnnotationDto> listAnnotationByParagraph = new ArrayList<NaeAnnotationDto>();
		for (NaeAnnotationDto annotation : listAnnotation)
			if (annotation.getParag() == parIndex)
				this.addOrdered(listAnnotationByParagraph, annotation);
		return listAnnotationByParagraph;
	}

	private void addOrdered(List<NaeAnnotationDto> listAnnotationByParagraph, NaeAnnotationDto annotation) {
		if (listAnnotationByParagraph.isEmpty()) {
			listAnnotationByParagraph.add(annotation);
			return;
		}
		int insertInd = listAnnotationByParagraph.size();
		for (int i = 0; i < listAnnotationByParagraph.size(); i++) {
			NaeAnnotationDto annotationI = listAnnotationByParagraph.get(i);
			if (annotationI.getBegin() > annotation.getBegin()) {
				insertInd = i;
				break;
			}
		}
		listAnnotationByParagraph.add(insertInd, annotation);
	}

}
