package com.softwarelma.ers_boot.pojo;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.softwarelma.ers_boot.dto.NaeFileDto;
import com.softwarelma.ers_boot.dto.NaeListFileDto;

public class NaeDocPojo {

	private static final Logger logger = Logger.getLogger(NaeDocPojo.class.getName());
	// private static final String FILEX = "C:\\Users\\guillermo.ellison\\Downloads\\docs\\04-Specifiche operations lettura e aggiornamento.docx";
	// private static final String FILE = "C:\\Users\\guillermo.ellison\\Downloads\\docs\\PASTEN TORO - Irenko Y La Ciudad De Cristal - 2003.DOC";
	// private static final String FILE_TO_READ = FILE;
	private static boolean toBase64 = true;
	private final NaeDocPojoUser user = new NaeDocPojoUser();
	private final NaeDocPojoReader reader = new NaeDocPojoReader();
	private final NaeDocPojoAI ai = new NaeDocPojoAI();
	private final NaeDocPojoWriter writer = new NaeDocPojoWriter();

	public NaeListFileDto postDocsAndGetTexts(NaeListFileDto listFileReq) {
		logger.log(Level.INFO, "postDocsAndGetTexts - begin");
		listFileReq = this.validatePostDocsAndGetTexts(listFileReq);
		if (listFileReq.getError() != null)
			return listFileReq;
		try {
			// if (FILE_TO_READ.toLowerCase().endsWith("x"))
			// this.readDocxFile2(FILE_TO_READ);
			// else
			// this.readDocFile2(FILE_TO_READ);
			for (NaeFileDto fileReq : listFileReq.getListFile()) {
				byte[] arrayByte = this.reader.toArrayByte(fileReq.getBase64());
				List<String> listParagraph = fileReq.getName().toLowerCase().endsWith("x") ? this.reader.readDocxFile(toBase64, arrayByte)
						: this.reader.readDocFile(toBase64, arrayByte);
				fileReq.setBase64(null);
				fileReq.setListParagraph(listParagraph);
			}
			logger.log(Level.INFO, "postDocsAndGetTexts - end");
		} catch (UnsupportedEncodingException e) {
			logger.log(Level.SEVERE, "postDocsAndGetTexts - error", e);
			listFileReq.setError("Errore nella lettura del file in base 64. " + e.getClass().getName() + ". " + (e.getMessage() == null ? "" : e.getMessage()));
		} catch (Exception e) {
			logger.log(Level.SEVERE, "postDocsAndGetTexts - error", e);
			listFileReq.setError("Errore generico. " + e.getClass().getName() + ". " + (e.getMessage() == null ? "" : e.getMessage()));
		}
		return listFileReq;
	}

	public NaeListFileDto postTextsAndGetAnnotations(NaeListFileDto listFileReq) {
		logger.log(Level.INFO, "postTextsAndGetAnnotations - begin");
		listFileReq = this.validatePostTextsAndGetAnnotations(listFileReq);
		if (listFileReq.getError() != null)
			return listFileReq;
		try {
			for (NaeFileDto fileReq : listFileReq.getListFile())
				fileReq.setListAnnotation(this.ai.retrieveListAnnotation(fileReq.getListParagraph()));
			logger.log(Level.INFO, "postTextsAndGetAnnotations - end");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "postTextsAndGetAnnotations - error", e);
			listFileReq.setError("Errore generico. " + e.getClass().getName() + ". " + (e.getMessage() == null ? "" : e.getMessage()));
		}
		return listFileReq;
	}

	public NaeListFileDto postAnnotationsAndGetDocs(NaeListFileDto listFileReq) {
		logger.log(Level.INFO, "postAnnotationsAndGetDocs - begin");
		listFileReq = this.validatePostAnnotationsAndGetDocs(listFileReq);
		if (listFileReq.getError() != null)
			return listFileReq;
		try {
			for (NaeFileDto fileReq : listFileReq.getListFile())
				fileReq.setBase64(this.writer.retrieveBase64Docx(fileReq.getListParagraph(), fileReq.getListAnnotation()));
			logger.log(Level.INFO, "postAnnotationsAndGetDocs - end");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "postAnnotationsAndGetDocs - error", e);
			listFileReq.setError("Errore generico. " + e.getClass().getName() + ". " + (e.getMessage() == null ? "" : e.getMessage()));
		}
		return listFileReq;
	}

	private NaeListFileDto validateRequestAndList(NaeListFileDto listFileReq) {
		if (listFileReq == null) {
			listFileReq = new NaeListFileDto();
			listFileReq.setError("Request non trovata");
			return listFileReq;
		}
		listFileReq.setError(null);// no error
		if (listFileReq.getListFile().isEmpty()) {
			listFileReq.setError("File non trovati");
			return listFileReq;
		}
		return listFileReq;
	}

	private NaeListFileDto validatePostDocsAndGetTexts(NaeListFileDto listFileReq) {
		listFileReq = this.validateRequestAndList(listFileReq);
		if (listFileReq.getError() != null)
			return listFileReq;
		for (int i = 0; i < listFileReq.getListFile().size(); i++) {
			NaeFileDto file = listFileReq.getListFile().get(i);
			if (file == null) {
				listFileReq.setError("Non trovato il file nella posizione " + i);
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

	private NaeListFileDto validatePostTextsAndGetAnnotations(NaeListFileDto listFileReq) {
		listFileReq = this.validateRequestAndList(listFileReq);
		if (listFileReq.getError() != null)
			return listFileReq;
		for (int i = 0; i < listFileReq.getListFile().size(); i++) {
			NaeFileDto file = listFileReq.getListFile().get(i);
			if (file == null) {
				listFileReq.setError("Non trovato il file nella posizione " + i);
			} else if (file.getListParagraph().isEmpty()) {
				listFileReq.setError("Non trovata la lista dei paragrafi nella posizione " + i);
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

	private NaeListFileDto validatePostAnnotationsAndGetDocs(NaeListFileDto listFileReq) {
		listFileReq = this.validateRequestAndList(listFileReq);
		if (listFileReq.getError() != null)
			return listFileReq;
		for (int i = 0; i < listFileReq.getListFile().size(); i++) {
			NaeFileDto file = listFileReq.getListFile().get(i);
			if (file == null) {
				listFileReq.setError("Non trovato il file nella posizione " + i);
			} else if (file.getListParagraph().isEmpty()) {
				listFileReq.setError("Non trovata la lista dei paragrafi nella posizione " + i);
			} else if (file.getListAnnotation().isEmpty()) {
				listFileReq.setError("Non trovata la lista delle annotazioni nella posizione " + i);
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

}
