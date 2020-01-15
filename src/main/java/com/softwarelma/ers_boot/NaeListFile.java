package com.softwarelma.ers_boot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NaeListFile implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<NaeFile> listFile;
	private int code;
	private String error;
	private String lang;

	public NaeListFile() {
		this.listFile = new ArrayList<NaeFile>();
	}

	public NaeListFile(List<NaeFile> listFile, int code, String error, String lang) {
		this.listFile = listFile;
		this.code = code;
		this.error = error;
		this.lang = lang;
	}

	public List<NaeFile> getListFile() {
		if (this.listFile == null)
			this.listFile = new ArrayList<NaeFile>();
		return this.listFile;
	}

	public void setListFile(List<NaeFile> listFile) {
		this.listFile = new ArrayList<NaeFile>();
		if (listFile != null)
			this.listFile.addAll(listFile);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
		if (this.error != null && this.code == 0)
			this.code = 1;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

}
