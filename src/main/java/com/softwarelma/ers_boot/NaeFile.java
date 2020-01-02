package com.softwarelma.ers_boot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NaeFile implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id;
	private String base64;
	private List<String> listParagraph;
	private String name;
	private String size;
	private String type;
	private String lastModifiedDate;

	public NaeFile() {
		this.listParagraph = new ArrayList<String>();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		if (id != other.id)
			return false;
		return true;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBase64() {
		return base64;
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}

	public List<String> getListParagraph() {
		if (listParagraph == null)
			this.listParagraph = new ArrayList<String>();
		return this.listParagraph;
	}

	public void setListParagraph(List<String> listParagraph) {
		this.listParagraph = new ArrayList<String>();
		if (listParagraph != null)
			this.listParagraph.addAll(listParagraph);
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
