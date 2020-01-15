package com.softwarelma.ers_boot;

import java.io.FileOutputStream;

import org.apache.poi.xwpf.usermodel.*;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHighlightColor;

/*
To
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHighlightColor;
the fully ooxml-schemas-1.3.jar is needed as mentioned in https://poi.apache.org/faq.html#faq-N10025
*/
public class WordRunWithBGColor {

	// from: https://stackoverflow.com/questions/35419619/how-can-i-set-background-colour-of-a-run-a-word-in-line-or-a-paragraph-in-a-do

	public static void main(String[] args) throws Exception {

		XWPFDocument doc = new XWPFDocument();

		XWPFParagraph paragraph = doc.createParagraph();
		XWPFRun run = paragraph.createRun();
		run.setText("This is text with ");

		run = paragraph.createRun();
		run.setText("background color background color background color background color background color "
				+ "background color background color background color background color background color "
				+ "background color background color background color background color background color ");
		CTShd cTShd = run.getCTR().addNewRPr().addNewShd();
		cTShd.setVal(STShd.CLEAR);
		cTShd.setColor("auto");
		cTShd.setFill("00FFFF");

		run = paragraph.createRun();
		run.setText(" and this is ");

		run = paragraph.createRun();
		run.setText("highlighted highlighted highlighted highlighted highlighted highlighted highlighted "
				+ "highlighted highlighted highlighted highlighted highlighted highlighted highlighted "
				+ "highlighted highlighted highlighted highlighted highlighted highlighted highlighted ");
		run.getCTR().addNewRPr().addNewHighlight().setVal(STHighlightColor.YELLOW);

		run = paragraph.createRun();
		run.setText(" text.");

		doc.write(new FileOutputStream("C:\\Users\\guillermo.ellison\\Downloads\\WordRunWithBGColor.docx"));

	}
}
