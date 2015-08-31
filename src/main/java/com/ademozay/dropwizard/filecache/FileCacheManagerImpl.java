package com.ademozay.dropwizard.filecache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ademozay.dropwizard.filecache.conf.FileCacheManagerConfiguration;
import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;

public class FileCacheManagerImpl implements FileCacheManager {

	private static String CACHE_MANAGER_FOLDER;
	private static String TEMP_DIR;
	private static String MAIN_CACHE_FOLDER;
	
	private static Map<FileCacheInstance, FileCacheManager> INSTANCES = Collections.synchronizedMap(new ConcurrentHashMap<FileCacheInstance, FileCacheManager>());
	
	private String exactCacheFolder;
	
	public static void configure(FileCacheManagerConfiguration conf) {
		FileCacheManagerImpl.TEMP_DIR = conf.getCacheDir();
		FileCacheManagerImpl.CACHE_MANAGER_FOLDER =  conf.getCacheFolder();
		FileCacheManagerImpl.MAIN_CACHE_FOLDER = TEMP_DIR + File.separator + CACHE_MANAGER_FOLDER;
		Path p = Paths.get(MAIN_CACHE_FOLDER);
		if(java.nio.file.Files.notExists(p)) {
			if(!p.toFile().mkdir()) {
				throw new RuntimeException(String.format("Could not create folder %s", p.getFileName()));
			}
		}
	}
	
	public static FileCacheManager getFileCacheManager() {
		return getFileCacheManager(null);
	}

	public static FileCacheManager getFileCacheManager(String cacheFolder) {
		FileCacheInstance instance = new FileCacheInstance(cacheFolder);
		if (!INSTANCES.containsKey(instance)) {
			FileCacheManager cacheManager;
			synchronized (FileCacheManagerImpl.class) {
				cacheManager = INSTANCES.get(instance);
				if (cacheManager == null) {
					synchronized (FileCacheManagerImpl.class) {
						cacheManager = new FileCacheManagerImpl(Optional.<String>fromNullable(cacheFolder));
					}
				}
				INSTANCES.put(instance, cacheManager);
			}
		}
		return INSTANCES.get(instance);
	}

	private FileCacheManagerImpl(Optional<String> innerCacheFolder) {
		if(innerCacheFolder.isPresent()) {
			exactCacheFolder = MAIN_CACHE_FOLDER + File.separator + innerCacheFolder.get() + File.separator;
			Path p = Paths.get(exactCacheFolder);
			if(java.nio.file.Files.notExists(p)) {
				if(!p.toFile().mkdir()) {
					throw new RuntimeException(String.format("Could not create inner cache folder %s", innerCacheFolder.get()));
				}
			}
		} else {
			exactCacheFolder = MAIN_CACHE_FOLDER + File.separator;
		}
	}
	
	@Override
	public boolean isFileExist(String name) {
		Path p = Paths.get(getExactFilePath(name));
		return java.nio.file.Files.exists(p);
	}
	
	@Override
	public Optional<String> readAsString(String name) throws IOException {
		Path p = Paths.get(getExactFilePath(name));
		if (java.nio.file.Files.exists(p)) {
			return Optional.<String>fromNullable(Files.toString(p.toFile(), Charsets.UTF_8));
		}
		return Optional.absent();
	}

	@Override
	public Optional<InputStream> readAsInputStream(String name) throws IOException {
		Path p = Paths.get(getExactFilePath(name));
		if (java.nio.file.Files.exists(p)) {
			return Optional.<InputStream>fromNullable(Files.asByteSource(p.toFile()).openStream());
		}
		return Optional.absent();
	}

	@Override
	public Optional<String> readAsString(String name, FileReaderCondition frc) throws IOException {
		Path p = Paths.get(getExactFilePath(name));
		if (java.nio.file.Files.exists(p) && areConditionsOK(p, frc)) {
			return Optional.<String>fromNullable(Files.toString(p.toFile(), Charsets.UTF_8));
		}
		return Optional.absent();
	}

	@Override
	public Optional<InputStream> readAsInputStream(String name, FileReaderCondition frc) throws IOException {
		Path p = Paths.get(getExactFilePath(name));
		if (java.nio.file.Files.exists(p) && areConditionsOK(p, frc)) {
			return Optional.<InputStream>fromNullable(Files.asByteSource(p.toFile()).openStream());
		}
		return Optional.absent();
	}

	@Override
	public void write(String name, InputStream is) throws IOException {
		Files.write(ByteStreams.toByteArray(is), new File(getExactFilePath(name)));
	}

	@Override
	public void write(String name, String content) throws IOException {
		Files.write(content.getBytes(Charsets.UTF_8), new File(getExactFilePath(name)));
	}

	@Override
	public void write(String name, String content, FileAttribute... fas) throws IOException {
		Path p = Paths.get(getExactFilePath(name));
		Files.write(content.getBytes(Charsets.UTF_8), p.toFile());
		if (fas != null) {
			writeFileAttribute(p, fas);
		}
	}

	@Override
	public void write(String name, InputStream is, FileAttribute... fas) throws IOException {
		Path p = Paths.get(getExactFilePath(name));
		Files.write(ByteStreams.toByteArray(is), p.toFile());
		if (fas != null) {
			writeFileAttribute(p, fas);
		}
	}

	@Override
	public boolean delete(String name) throws IOException {
		Path p = Paths.get(getExactFilePath(name));
		return java.nio.file.Files.deleteIfExists(p);
	}

	@Override
	public void clear() throws IOException {
		synchronized (this) {
			File dir = new File(exactCacheFolder);
			for (File file : dir.listFiles()) {
				if (!file.isDirectory()) {
					file.delete();
				}
			}
		}
	}
	
	@Override
	public String getExactFilePath(String fileName) {
		return exactCacheFolder + fileName;
	}
	
	private void writeFileAttribute(Path p, FileAttribute... fas) throws IOException {
		UserDefinedFileAttributeView view = java.nio.file.Files.getFileAttributeView(p, UserDefinedFileAttributeView.class);
		for (FileAttribute fa : fas) {
			final byte[] array = fa.getValue().getBytes(Charsets.UTF_8);
			final ByteBuffer buf = ByteBuffer.wrap(array);
			view.write(fa.getKey(), buf);
		}
	}

	private boolean areConditionsOK(Path p, FileReaderCondition frc) throws IOException {
		boolean conditionStatus = false;
		if (frc != null) {
			UserDefinedFileAttributeView view = java.nio.file.Files.getFileAttributeView(p, UserDefinedFileAttributeView.class);
			Iterator<FileAttributeCondition> it = frc.getFileAttributeConditions().iterator();
			while (it.hasNext()) {
				FileAttributeCondition fileAttributeCondition = it.next();
				FileAttribute fileAttribute = fileAttributeCondition.getFileAttribute();
				
				int attrSize = view.size(fileAttribute.getKey());
				ByteBuffer buf = ByteBuffer.allocate(attrSize);
				view.read(fileAttribute.getKey(), buf);

				String attrValue = new String(buf.array(), Charsets.UTF_8);

				conditionStatus = fileAttributeCondition.getType().apply(fileAttributeCondition.getOperator(), attrValue, fileAttribute.getValue());
			}
		}
		return conditionStatus;
	}

}
