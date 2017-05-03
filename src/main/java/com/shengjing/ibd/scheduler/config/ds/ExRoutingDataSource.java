package com.shengjing.ibd.scheduler.config.ds;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;


public class ExRoutingDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		return DynamicDataSourceHolder.getDataSource();
	}
}