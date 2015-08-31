package com.ademozay.dropwizard.filecache;

public interface FileAttributeTypeOperation {

	boolean apply(FileAttributeConditionOperator op, String attributeValue, Object value);
	
}
