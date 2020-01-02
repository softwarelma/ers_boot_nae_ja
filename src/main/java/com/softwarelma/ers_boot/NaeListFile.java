package com.softwarelma.ers_boot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NaeListFile implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<NaeFile> listFile;
	private String error;

	public NaeListFile() {
		this.listFile = new ArrayList<NaeFile>();
	}

	public NaeListFile(List<NaeFile> listFile, String error) {
		this.listFile = listFile;
		this.error = error;
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

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
