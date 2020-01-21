package com.softwarelma.ers_boot.pojo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
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
		for (int paragInd = 0; paragInd < listParagraph.size(); paragInd++) {
			List<NaeAnnotationDto> listAnnotationByParagraph = retrieveListAnnotationByParagraph(paragInd, listAnnotation);
			this.addParagraphWithAnnotations(paragInd, doc, listParagraph.get(paragInd), listAnnotationByParagraph);
		}
		// doc.write(new FileOutputStream("C:\\Users\\guillermo.ellison\\Downloads\\WordRunWithBGColor.docx"));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		doc.write(baos);
		return new String(Base64.getMimeEncoder().encode(baos.toByteArray()));
	}

	private void addParagraphWithAnnotations(int paragInd, XWPFDocument doc, String paragraph, List<NaeAnnotationDto> listAnnotationByParagraph) {
		this.validateParagraph(paragInd, paragraph, listAnnotationByParagraph);
		XWPFParagraph docParagraph = doc.createParagraph();
		paragraph = NaeDocPojo.clean4WritingParagraph(paragraph, listAnnotationByParagraph);
		if (paragraph == null || paragraph.trim().isEmpty() || listAnnotationByParagraph.isEmpty()) {
			XWPFRun run = docParagraph.createRun();
			run.setText(paragraph == null ? "" : paragraph);
			return;
		}
		int insertInd = 0;
		for (int ann = 0; ann < listAnnotationByParagraph.size(); ann++) {
			NaeAnnotationDto annotation = listAnnotationByParagraph.get(ann);
			insertInd = this.addAnnotation(docParagraph, paragInd, insertInd, paragraph, annotation, ann == listAnnotationByParagraph.size() - 1);
		}
	}

	private void validateParagraph(int paragInd, String paragraph, List<NaeAnnotationDto> listAnnotation) {
		if ((paragraph == null || paragraph.trim().isEmpty()) && !listAnnotation.isEmpty())
			throw new RuntimeException("Non trovato il testo del paragrafo numero " + (paragInd + 1) + ", contenente delle annotazioni");
	}

	private boolean validateAnnotation(int paragInd, int insertInd, String paragraph, NaeAnnotationDto annotation) {
		if (annotation == null)
			throw new RuntimeException("Non trovata l'annotazione nel paragrafo numero " + (paragInd + 1));
		if (annotation.getEnd() > paragraph.length())
			// throw new RuntimeException("Indice finale d'annotazione non valido (" + annotation.getEnd() + ") nel paragrafo numero " + (paragInd + 1));
			return false;// no exception, just avoiding the wrong annotation
		if (insertInd < 0 || insertInd > paragraph.length())
			throw new RuntimeException("Indice d'inserimento non valido (" + insertInd + ") nel paragrafo numero " + (paragInd + 1));
		if (annotation.getBegin() < insertInd || annotation.getBegin() >= annotation.getEnd())
			throw new RuntimeException(
					"Indici d'annotazione non validi (" + annotation.getBegin() + ", " + annotation.getEnd() + ") nel paragrafo numero " + (paragInd + 1));
		return true;
	}

	private int addAnnotation(XWPFParagraph docParagraph, int paragInd, int insertInd, String paragraph, NaeAnnotationDto annotation, boolean lastAnnotation) {
		if (!this.validateAnnotation(paragInd, insertInd, paragraph, annotation))
			return insertInd;
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
		cTShd.setFill(annotation.getMark().substring(1));
		if (lastAnnotation && insertInd < paragraph.length()) {
			run = docParagraph.createRun();
			run.setText(paragraph.substring(insertInd, paragraph.length()));
			insertInd = paragraph.length();
		}
		return insertInd;
	}

	public static List<NaeAnnotationDto> retrieveListAnnotationByParagraph(int parIndex, List<NaeAnnotationDto> listAnnotation) {
		List<NaeAnnotationDto> listAnnotationByParagraph = new ArrayList<NaeAnnotationDto>();
		for (NaeAnnotationDto annotation : listAnnotation)
			if (annotation.getParag() == parIndex)
				addOrdered(listAnnotationByParagraph, annotation);
		return listAnnotationByParagraph;
	}

	private static void addOrdered(List<NaeAnnotationDto> listAnnotationByParagraph, NaeAnnotationDto annotation) {
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
