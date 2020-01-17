package com.softwarelma.ers_boot.pojo;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

public class NaeDocPojoReader {

	private static boolean testBase64 = false;

	public byte[] toArrayByte(String base64) throws UnsupportedEncodingException {
		return Base64.getDecoder().decode(new String(base64).getBytes("UTF-8"));
	}

	private String toBase64(String decoded) throws UnsupportedEncodingException {
		String base64 = new String(Base64.getEncoder().encode(decoded.getBytes()));
		if (testBase64) {
			String decoded2 = new String(this.toArrayByte(base64));
			if (!decoded.equals(decoded2)) {
				throw new RuntimeException("Invalid coding for: " + decoded);
			}
		}
		return base64;
	}

	public List<String> readDocFile(boolean toBase64, byte[] arrayByte) throws IOException {
		List<String> listParagraph = new LinkedList<String>();
		ByteArrayInputStream bais = new ByteArrayInputStream(arrayByte);
		// File file = new File(fileName);
		// FileInputStream fis = new FileInputStream(file.getAbsolutePath());
		HWPFDocument doc = new HWPFDocument(bais);
		WordExtractor we = new WordExtractor(doc);
		String[] paragraphs = we.getParagraphText();
		System.out.println("Total no of paragraph " + paragraphs.length);
		for (String para : paragraphs) {
			String text = toBase64 ? this.toBase64(para) : para;
			text = text.replace("\r\n", "");
			text = text.replace("\n", "");
			listParagraph.add(text);
		}
		// fis.close();
		we.close();
		// doc.close();
		return listParagraph;
	}

	public List<String> readDocxFile(boolean toBase64, byte[] arrayByte) throws IOException {
		List<String> listParagraph = new LinkedList<String>();
		ByteArrayInputStream bais = new ByteArrayInputStream(arrayByte);
		// File file = new File(fileName);
		// FileInputStream fis = new FileInputStream(file.getAbsolutePath());
		XWPFDocument document = new XWPFDocument(bais);
		List<XWPFParagraph> paragraphs = document.getParagraphs();
		System.out.println("Total no of paragraph " + paragraphs.size());
		for (XWPFParagraph para : paragraphs) {
			String text = toBase64 ? this.toBase64(para.getText()) : para.getText();
			text = text.replace("\r\n", "");
			text = text.replace("\n", "");
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
