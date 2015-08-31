package com.ademozay.dropwizard.filecache;

public class FileAttributeCondition {

	private FileAttribute fileAttribute;
	private FileAttributeType type;
	private FileAttributeConditionOperator operator;

	public FileAttributeCondition(FileAttribute fileAttribute, FileAttributeType type, FileAttributeConditionOperator operator) {
		this.fileAttribute = fileAttribute;
		this.type = type;
		this.operator = operator;
	}

	public FileAttribute getFileAttribute() {
		return fileAttribute;
	}

	public void setFileAttribute(FileAttribute fileAttribute) {
		this.fileAttribute = fileAttribute;
	}

	public FileAttributeType getType() {
		return type;
	}

	public void setType(FileAttributeType type) {
		this.type = type;
	}

	public FileAttributeConditionOperator getOperator() {
		return operator;
	}

	public void setOperator(FileAttributeConditionOperator operator) {
		this.operator = operator;
	}

}
