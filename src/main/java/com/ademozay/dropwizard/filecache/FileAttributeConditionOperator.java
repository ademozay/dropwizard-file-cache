package com.ademozay.dropwizard.filecache;

import org.joda.time.DateTime;

public enum FileAttributeConditionOperator implements FileAttributeConditionOperation {

	EQUALS {
		@Override
		public boolean apply(FileAttributeType fileAttributeType, String attributeValue, String conditionValue) {
			switch (fileAttributeType) {
			case STRING: {
				return attributeValue.equals(conditionValue);
			}
			case DATE: {
				DateTime attributeDate = new DateTime(attributeValue);
				DateTime conditionDate = new DateTime(conditionValue);
				return attributeDate.isEqual(conditionDate);
			}
			default:
				throw new IllegalArgumentException(String.format("%s attribute type cannot perfom %s operation", fileAttributeType, this));
			}
		}
	},

	GREATER {
		@Override
		public boolean apply(FileAttributeType fileAttributeType, String attributeValue, String conditionValue) {
			switch (fileAttributeType) {
			case DATE: {
				DateTime attributeDate = new DateTime(Long.valueOf(attributeValue));
				DateTime conditionDate = new DateTime(Long.valueOf(conditionValue));
				return conditionDate.isAfter(attributeDate);
			}
			default:
				throw new IllegalArgumentException(String.format("%s attribute type cannot perfom %s operation", fileAttributeType, this));
			}
		}
	},

	LESS {
		@Override
		public boolean apply(FileAttributeType fileAttributeType, String attributeValue, String conditionValue) {
			switch (fileAttributeType) {
			case DATE: {
				DateTime attributeDate = new DateTime(Long.valueOf(attributeValue));
				DateTime conditionDate = new DateTime(Long.valueOf(conditionValue));
				return conditionDate.isBefore(attributeDate);
			}
			default:
				throw new IllegalArgumentException(String.format("%s attribute type cannot perfom %s operation", fileAttributeType, this));
			}
		}
	};

}
