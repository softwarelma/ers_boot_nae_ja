package com.softwarelma.ers_boot.pojo;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.softwarelma.ers_boot.dto.NaeAnnotationDto;
import com.softwarelma.ers_boot.dto.NaeFileDto;
import com.softwarelma.ers_boot.dto.NaeListFileDto;

public class NaeDocPojo {

	private static final Logger logger = Logger.getLogger(NaeDocPojo.class.getName());
	// private static final String FILEX = "C:\\Users\\guillermo.ellison\\Downloads\\docs\\04-Specifiche operations lettura e aggiornamento.docx";
	// private static final String FILE = "C:\\Users\\guillermo.ellison\\Downloads\\docs\\PASTEN TORO - Irenko Y La Ciudad De Cristal - 2003.DOC";
	// private static final String FILE_TO_READ = FILE;
	private static boolean testBase64 = false;
	private static boolean toBase64 = true;
	private static boolean contractAnnotations = true;
	private final NaeDocPojoUser user = new NaeDocPojoUser();
	private final NaeDocPojoReader reader = new NaeDocPojoReader();
	private final NaeDocPojoAI ai = new NaeDocPojoAI();
	private final NaeDocPojoWriter writer = new NaeDocPojoWriter();

	private static final String charset1Amp = "&amp;";
	private static final String charset2Common = //
			" \tabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!?\"'$%()[]{}<>=*+-/\\|_.,:;";

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
				byte[] arrayByte = this.toArrayByte(fileReq.getBase64());
				List<String> listParagraph = fileReq.getName().toLowerCase().endsWith("x") ? this.reader.readDocxFile(arrayByte)
						: this.reader.readDocFile(arrayByte);
				this.clean4ReadingListParagraph(listParagraph);
				this.toBase64ListParagraph(listParagraph);
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
			for (NaeFileDto fileReq : listFileReq.getListFile()) {
				fileReq.setListAnnotation(this.ai.retrieveListAnnotation(fileReq.getListParagraph()));
				fileReq.setListAnnotation(this.cleanListAnnotation(fileReq.getListParagraph(), fileReq.getListAnnotation()));
			}
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
			for (NaeFileDto fileReq : listFileReq.getListFile()) {
				fileReq.setBase64(this.writer.retrieveBase64Docx(fileReq.getListParagraph(), fileReq.getListAnnotation()));
			}
			logger.log(Level.INFO, "postAnnotationsAndGetDocs - end");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "postAnnotationsAndGetDocs - error", e);
			listFileReq.setError("Errore generico. " + e.getClass().getName() + ". " + (e.getMessage() == null ? "" : e.getMessage()));
		}
		return listFileReq;
	}

	private void toBase64ListParagraph(List<String> listParagraph) throws UnsupportedEncodingException {
		if (!toBase64)
			return;
		for (int i = 0; i < listParagraph.size(); i++)
			listParagraph.set(i, this.toBase64Paragraph(listParagraph.get(i)));
	}

	private String toBase64Paragraph(String paragraph) throws UnsupportedEncodingException {
		String base64 = new String(Base64.getEncoder().encode(paragraph.getBytes()));
		if (testBase64) {
			String decoded2 = new String(this.toArrayByte(base64));
			if (!paragraph.equals(decoded2)) {
				throw new RuntimeException("Invalid coding for: " + paragraph);
			}
		}
		return base64;
	}

	private byte[] toArrayByte(String base64) throws UnsupportedEncodingException {
		// Base64.getDecoder().decode(base64.getBytes("UTF-8"));
		// Base64.getMimeDecoder().decode(base64);
		return Base64.getMimeDecoder().decode(base64.substring(base64.indexOf(",") + 1));
	}

	private List<NaeAnnotationDto> cleanListAnnotation(List<String> listParagraph, List<NaeAnnotationDto> listAnnotation) {
		List<NaeAnnotationDto> listAnnotationClean = new ArrayList<>();
		for (int i = 0; i < listParagraph.size(); i++) {
			String paragraph = listParagraph.get(i);
			if (paragraph.equals("&nbsp;"))
				continue;
			List<int[]> listEncodingIntervals = new ArrayList<>();
			retrieveListEncodingIntervals(paragraph, listEncodingIntervals, null);
			List<NaeAnnotationDto> listAnnotationPar = NaeDocPojoWriter.retrieveListAnnotationByParagraph(i, listAnnotation);
			listAnnotationClean.addAll(this.cleanListAnnotationParagraph(listEncodingIntervals, listAnnotationPar));
		}
		return listAnnotationClean;
	}

	private List<NaeAnnotationDto> cleanListAnnotationParagraph(List<int[]> listEncodingIntervals, List<NaeAnnotationDto> listAnnotationPar) {
		List<NaeAnnotationDto> listAnnotationClean = new ArrayList<>();
		for (NaeAnnotationDto annotation : listAnnotationPar)
			if (this.isCleanAnnotation(listEncodingIntervals, annotation))
				listAnnotationClean.add(annotation);
		return listAnnotationClean;
	}

	private boolean isCleanAnnotation(List<int[]> listEncodingIntervals, NaeAnnotationDto annotation) {
		// int nextValid = 0;
		boolean found = false;
		for (int i = 0; i < listEncodingIntervals.size(); i++) {
			// nextValid = i;
			int[] interval = listEncodingIntervals.get(i);
			if (annotation.getEnd() <= interval[0]) { // annotation before
				continue;
			} else if (interval[1] <= annotation.getBegin()) {// annotation after
				continue;
			} else {// collision
				found = true;
				break;
			}
		}
		// for (int i = 0; i < nextValid; i++)
		// listEncodingIntervals.remove(0);//non possible, not ordered
		return !found;
	}

	private static void retrieveListEncodingIntervals(String paragraph, List<int[]> listEncodingIntervals, Map<Character, String> charset3MapSpecial) {
		int fromIndex = 0;
		int ind;
		while ((ind = paragraph.indexOf("&#", fromIndex)) != -1) {
			fromIndex = ind + 2;
			ind = paragraph.indexOf(";", fromIndex);
			if (ind == -1)
				continue;
			String num = paragraph.substring(fromIndex, ind);
			while (num.startsWith("0"))
				num = num.substring(1);
			int n;
			try {
				n = Integer.parseInt(num);
				if ((n + "").length() > 5)
					continue;
			} catch (NumberFormatException e) {
				continue;
			}
			if (listEncodingIntervals != null)
				listEncodingIntervals.add(new int[] { fromIndex - 2, ind + 1 });
			fromIndex = ind + 1;
			if (charset3MapSpecial != null)
				charset3MapSpecial.put((char) n, "&#" + n + ";");
		}
	}

	// private List<int[]> retrieveListEncodingIntervals2(String paragraph) {
	// List<int[]> listEncodingIntervals = new ArrayList<>();
	// listEncodingIntervals.addAll(this.retrieveListIntervals(paragraph, charset1Amp));
	// for (char c : charset3MapSpecial.keySet()) {
	// String s = charset3MapSpecial.get(c);
	// listEncodingIntervals.addAll(this.retrieveListIntervals(paragraph, s));
	// }
	// return listEncodingIntervals;
	// }

	private List<int[]> retrieveListIntervals(String text, String subtext) {
		List<int[]> listIntervals = new ArrayList<>();
		int fromIndex = 0;
		int ind;
		while ((ind = text.indexOf(subtext, fromIndex)) != -1) {
			listIntervals.add(new int[] { ind, ind + subtext.length() });
			fromIndex = ind + subtext.length();
		}
		return listIntervals;
	}

	/**
	 * <pre>
	 * public static void clean4WritingListParagraph(List<String> listParagraph) {
	 * 	for (int i = 0; i < listParagraph.size(); i++)
	 * 		listParagraph.set(i, clean4WritingParagraph(listParagraph.get(i)));
	 * }
	 * </pre>
	 */
	public static String clean4WritingParagraph(String paragraph, List<NaeAnnotationDto> listAnnotationByParagraph) {
		Map<Character, String> charset3MapSpecial = new HashMap<>();
		// System.out.println(paragraph);
		retrieveListEncodingIntervals(paragraph, null, charset3MapSpecial);
		for (char replacement : charset3MapSpecial.keySet()) {
			String target = charset3MapSpecial.get(replacement);
			paragraph = clean4WritingParagraphByTarget(paragraph, listAnnotationByParagraph, target, replacement);
		}
		paragraph = clean4WritingParagraphByTarget(paragraph, listAnnotationByParagraph, charset1Amp, '&');
		// System.out.println(paragraph);
		return paragraph;
	}

	private static String clean4WritingParagraphByTarget(String paragraph, List<NaeAnnotationDto> listAnnotationByParagraph, String target, char replacement) {
		int ind;
		while ((ind = paragraph.indexOf(target)) != -1) {
			paragraph = paragraph.replaceFirst(target, replacement + "");
			clean4WritingParagraphByTargetByListAnnotation(listAnnotationByParagraph, target, ind);
		}
		return paragraph;
	}

	private static void clean4WritingParagraphByTargetByListAnnotation(List<NaeAnnotationDto> listAnnotationByParagraph, String target, int ind) {
		if (!contractAnnotations)
			return;
		List<NaeAnnotationDto> listAnnotationByParagraphRet = new ArrayList<>();
		for (NaeAnnotationDto annotation : listAnnotationByParagraph) {
			if (annotation.getBegin() > ind)
				annotation.setBegin(annotation.getBegin() - (target.length() - 1));
			if (annotation.getEnd() > ind)
				annotation.setEnd(annotation.getEnd() - (target.length() - 1));
			if (annotation.getBegin() > -1 && annotation.getBegin() < annotation.getEnd())
				listAnnotationByParagraphRet.add(annotation);
		}
		listAnnotationByParagraph.clear();
		listAnnotationByParagraph.addAll(listAnnotationByParagraphRet);
	}

	private void clean4ReadingListParagraph(List<String> listParagraph) {
		for (int i = 0; i < listParagraph.size(); i++)
			listParagraph.set(i, this.clean4ReadingParagraph(listParagraph.get(i)));
	}

	private String clean4ReadingParagraph(String paragraph) {
		paragraph = paragraph.replace("&", charset1Amp);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < paragraph.length(); i++)
			sb.append(this.clean4ReadingChar(paragraph.charAt(i)));
		// System.out.println(paragraph);
		// System.out.println(sb);
		String ret = sb.toString();
		ret = ret.replace("&#1;", "&nbsp;");
		ret = ret.replace("&#12;", "&nbsp;");
		return ret == null || ret.trim().isEmpty() ? "&nbsp;" : ret;
	}

	private String clean4ReadingChar(char c) {
		if (charset2Common.contains(c + "")) {
			return c + "";
			// } else if (charset3MapSpecial.containsKey(c)) {
			// return charset3MapSpecial.get(c);
		} else {
			// System.out.println("clean4ReadingChar, char=" + c + ", int=" + ((int) c));
			return "&#" + ((int) c) + ";";
		}
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
