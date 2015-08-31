package com.ademozay.dropwizard.filecache;

public interface FileAttributeConditionOperation {

	boolean apply(FileAttributeType fileAttributeType, String attributeValue, String conditionValue);
	
}
