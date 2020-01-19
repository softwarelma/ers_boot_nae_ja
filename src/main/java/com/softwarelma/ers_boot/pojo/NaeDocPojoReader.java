package com.softwarelma.ers_boot.pojo;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

public class NaeDocPojoReader {

	public List<String> readDocFile(byte[] arrayByte) throws IOException {
		List<String> listParagraph = new LinkedList<String>();
		ByteArrayInputStream bais = new ByteArrayInputStream(arrayByte);
		// File file = new File(fileName);
		// FileInputStream fis = new FileInputStream(file.getAbsolutePath());
		HWPFDocument doc = new HWPFDocument(bais);
		WordExtractor we = new WordExtractor(doc);
		String[] paragraphs = we.getParagraphText();
		// System.out.println("Total no of paragraph " + paragraphs.length);
		for (String para : paragraphs) {
			para = para.replace("\r\n", "").replace("\n", "");
			listParagraph.add(para);
		}
		// fis.close();
		we.close();
		// doc.close();
		return listParagraph;
	}

	public List<String> readDocxFile(byte[] arrayByte) throws IOException {
		List<String> listParagraph = new LinkedList<String>();
		ByteArrayInputStream bais = new ByteArrayInputStream(arrayByte);
		// File file = new File(fileName);
		// FileInputStream fis = new FileInputStream(file.getAbsolutePath());
		XWPFDocument document = new XWPFDocument(bais);
		List<XWPFParagraph> paragraphs = document.getParagraphs();
		System.out.println("Total no of paragraph " + paragraphs.size());
		for (XWPFParagraph para : paragraphs) {
			String text = para.getText().replace("\r\n", "").replace("\n", "");
			listParagraph.add(text);
		}
		// fis.close();
		document.close();
		return listParagraph;
	}

	@SuppressWarnings("unused")
	private void readDocFile2(String fileName) throws IOException {
		File file = new File(fileName);
		FileInputStream fis = new FileInputStream(file.getAbsolutePath());
		HWPFDocument doc = new HWPFDocument(fis);
		WordExtractor we = new WordExtractor(doc);
		String[] paragraphs = we.getParagraphText();
		System.out.println("Total no of paragraph " + paragraphs.length);
		for (String para : paragraphs) {
			System.out.println(para.toString());
		}
		we.close();
		// fis.close();
	}

	@SuppressWarnings("unused")
	private void readDocxFile2(String fileName) throws IOException {
		File file = new File(fileName);
		FileInputStream fis = new FileInputStream(file.getAbsolutePath());
		XWPFDocument document = new XWPFDocument(fis);
		List<XWPFParagraph> paragraphs = document.getParagraphs();
		System.out.println("Total no of paragraph " + paragraphs.size());
		for (XWPFParagraph para : paragraphs) {
			System.out.println(para.getText());
		}
		// fis.close();
		document.close();
	}

}
