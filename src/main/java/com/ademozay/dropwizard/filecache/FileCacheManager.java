package com.ademozay.dropwizard.filecache;

import java.io.IOException;
import java.io.InputStream;

import com.google.common.base.Optional;

public interface FileCacheManager {

	boolean isFileExist(String name);

	Optional<String> readAsString(String name) throws IOException;
	
	Optional<InputStream> readAsInputStream(String name) throws IOException;
	
	Optional<String> readAsString(String name, FileReaderCondition frc) throws IOException;

	Optional<InputStream> readAsInputStream(String name, FileReaderCondition frc) throws IOException;
	
	void write(String name, InputStream is) throws IOException;
	
	void write(String name, String content) throws IOException;

	void write(String name, String content, FileAttribute... fas) throws IOException;
	
	void write(String name, InputStream is, FileAttribute... fas) throws IOException;
	
	boolean delete(String name) throws IOException;
	
	void clear() throws IOException;

	String getExactFilePath(String fileName);

}
