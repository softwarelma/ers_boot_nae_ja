package com.softwarelma.ers_boot;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

public class NaeDocPojo {

	private static final Logger logger = Logger.getLogger(NaeDocPojo.class.getName());
	// private static final String FILEX = "C:\\Users\\guillermo.ellison\\Downloads\\docs\\04-Specifiche operations lettura e aggiornamento.docx";
	// private static final String FILE = "C:\\Users\\guillermo.ellison\\Downloads\\docs\\PASTEN TORO - Irenko Y La Ciudad De Cristal - 2003.DOC";
	// private static final String FILE_TO_READ = FILE;
	private static boolean toBase64 = true;
	private static boolean testBase64 = false;

	public NaeListFile doReadDocs(NaeListFile listFileReq) {
		logger.log(Level.INFO, "doReadDocs - begin");
		listFileReq = this.validateRequest(listFileReq);
		if (listFileReq.getError() != null)
			return listFileReq;
		try {
			// if (FILE_TO_READ.toLowerCase().endsWith("x"))
			// this.readDocxFile2(FILE_TO_READ);
			// else
			// this.readDocFile2(FILE_TO_READ);
			for (NaeFile fileReq : listFileReq.getListFile()) {
				byte[] arrayByte = this.toArrayByte(fileReq.getBase64());
				List<String> listParagraph = fileReq.getName().toLowerCase().endsWith("x") ? this.readDocxFile(toBase64, arrayByte)
						: this.readDocFile(toBase64, arrayByte);
				fileReq.setBase64(null);
				fileReq.setListParagraph(listParagraph);
			}
			logger.log(Level.INFO, "doReadDocs - end");
		} catch (UnsupportedEncodingException e) {
			logger.log(Level.SEVERE, "doReadDocs - error", e);
			listFileReq.setError("Errore nella lettura del file in base 64. " + e.getClass().getName() + ". " + (e.getMessage() == null ? "" : e.getMessage()));
		} catch (Exception e) {
			logger.log(Level.SEVERE, "doReadDocs - error", e);
			listFileReq.setError("Errore generico. " + e.getClass().getName() + ". " + (e.getMessage() == null ? "" : e.getMessage()));
		}
		return listFileReq;
	}

	private NaeListFile validateRequest(NaeListFile listFileReq) {
		if (listFileReq == null) {
			listFileReq = new NaeListFile();
			listFileReq.setError("Request non trovata");
			return listFileReq;
		}
		listFileReq.setError(null);// no error
		if (listFileReq.getListFile().isEmpty()) {
			listFileReq.setError("File non trovati");
			return listFileReq;
		}
		for (int i = 0; i < listFileReq.getListFile().size(); i++) {
			NaeFile file = listFileReq.getListFile().get(i);
			if (file == null) {
				listFileReq.setError("Non trovato il file nella posizione " + i);
			} else if (file.getId() < 1) {
				listFileReq.setError("File con id non valido (" + file.getId() + ") nella posizione " + i);
			} else if (file.getBase64() == null || file.getBase64().isEmpty()) {
				listFileReq.setError("File con base64 non valida (" + file.getBase64() + ") nella posizione " + i);
			} else if (file.getName() == null || file.getName().isEmpty()) {
				listFileReq.setError("File con name non valido (" + file.getName() + ") nella posizione " + i);
			} else if (file.getSize() == null || file.getSize().isEmpty()) {
				listFileReq.setError("File con size non valido (" + file.getSize() + ") nella posizione " + i);
			} else if (file.getType() == null || file.getType().isEmpty()) {
				listFileReq.setError("File con type non valido (" + file.getType() + ") nella posizione " + i);
			} else if (file.getLastModifiedDate() == null || file.getLastModifiedDate().isEmpty()) {
				listFileReq.setError("File con lastModifiedDate non valida (" + file.getType() + ") nella posizione " + i);
			}
			if (listFileReq.getError() != null)
				return listFileReq;
		}
		return listFileReq;
	}

	private byte[] toArrayByte(String base64) throws UnsupportedEncodingException {
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

	private List<String> readDocFile(boolean toBase64, byte[] arrayByte) throws IOException {
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

	private List<String> readDocxFile(boolean toBase64, byte[] arrayByte) throws IOException {
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
