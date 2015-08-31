package com.ademozay.dropwizard.filecache;

import java.util.List;

import com.google.common.collect.Lists;

public class FileReaderCondition {

	private List<FileAttributeCondition> fileAttributeConditions = Lists.newArrayList();

	private FileReaderCondition(List<FileAttributeCondition> fileAttributes) {
		this.fileAttributeConditions = fileAttributes;
	}

	public static Builder builder() {
		return new Builder();
	}

	public List<FileAttributeCondition> getFileAttributeConditions() {
		return fileAttributeConditions;
	}

	public void setFileAttributes(List<FileAttributeCondition> fileAttributeConditions) {
		this.fileAttributeConditions = fileAttributeConditions;
	}

	public static class Builder {

		private List<FileAttributeCondition> fileAttributeConditions = Lists.newArrayList();

		public Builder addFileAttributeCondition(FileAttributeCondition fileAttribute) {
			this.fileAttributeConditions.add(fileAttribute);
			return this;
		}

		public FileReaderCondition build() {
			return new FileReaderCondition(fileAttributeConditions);
		}

	}

}
