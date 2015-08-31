package com.ademozay.dropwizard.filecache.bundle;

import com.ademozay.dropwizard.filecache.FileCacheManagerImpl;
import com.ademozay.dropwizard.filecache.conf.IFileCacheManagerConfiguration;
import com.yammer.dropwizard.ConfiguredBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.Environment;

public class FileCacheManagerBundle<T extends Configuration & IFileCacheManagerConfiguration> implements ConfiguredBundle<T> {

	@Override
	public void initialize(Bootstrap<?> bootstrap) { }

	@Override
	public void run(T configuration, Environment environment) throws Exception {
		FileCacheManagerImpl.configure(configuration.getFileCacheManagerConfiguration());
	}

}
