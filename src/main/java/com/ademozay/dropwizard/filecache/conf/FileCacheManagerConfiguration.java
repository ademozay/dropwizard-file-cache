package com.ademozay.dropwizard.filecache.conf;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileCacheManagerConfiguration {

	@JsonProperty
	@NotNull
	private String cacheDir;

	@JsonProperty
	@NotNull
	private String cacheFolder;

	public String getCacheDir() {
		return cacheDir;
	}

	public void setCacheDir(String cacheDir) {
		this.cacheDir = cacheDir;
	}

	public String getCacheFolder() {
		return cacheFolder;
	}

	public void setCacheFolder(String cacheFolder) {
		this.cacheFolder = cacheFolder;
	}

}
