package com.ademozay.dropwizard.filecache;

import static com.google.common.base.Preconditions.*;

public enum FileAttributeType {

	STRING, DATE;
	
	
	public boolean apply(FileAttributeConditionOperator op, String attributeValue, String value) {
		checkNotNull(attributeValue);
		checkNotNull(value);
		return op.apply(this, attributeValue, String.valueOf(value));
	}

}
