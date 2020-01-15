package com.softwarelma.ers_boot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NaeFile implements Serializable {

	private static final long serialVersionUID = 1L;
	private String base64;
	private List<String> listParagraph;
	private List<NaeAnnotation> listAnnotation;
	private String name;
	private String size;
	private String type;
	private String lastModifiedDate;

	public NaeFile() {
		this.listParagraph = new ArrayList<String>();
		this.listAnnotation = new ArrayList<NaeAnnotation>();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NaeFile other = (NaeFile) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public String getBase64() {
		return base64;
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}

	public List<String> getListParagraph() {
		if (this.listParagraph == null)
			this.listParagraph = new ArrayList<String>();
		return this.listParagraph;
	}

	public void setListParagraph(List<String> listParagraph) {
		this.listParagraph = new ArrayList<String>();
		if (listParagraph != null)
			this.listParagraph.addAll(listParagraph);
	}

	public List<NaeAnnotation> getListAnnotation() {
		if (this.listAnnotation == null)
			this.listAnnotation = new ArrayList<NaeAnnotation>();
		return this.listAnnotation;
	}

	public void setListAnnotation(List<NaeAnnotation> listAnnotation) {
		this.listAnnotation = new ArrayList<NaeAnnotation>();
		if (listAnnotation != null)
			this.listAnnotation.addAll(listAnnotation);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

}
