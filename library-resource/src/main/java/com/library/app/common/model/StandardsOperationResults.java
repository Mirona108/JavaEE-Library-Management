package com.library.app.common.model;

import com.library.app.common.exception.FieldNotValidException;

public final class StandardsOperationResults {

	private StandardsOperationResults() {
	}

	public static OperationResult getOperationResultExistent(ResourceMessage resourceMessage,
			String fieldsNames) {
		return OperationResult.error(resourceMessage.getKeyOfResourceExistent(),
				resourceMessage.getMessageOfResourceExistent(fieldsNames));
	}

	public static OperationResult getOperationResultInvalidField(ResourceMessage resourceMessage,
			FieldNotValidException ex) {
		return OperationResult.error(resourceMessage.getKeyOfInvalidField(ex.getFieldName()), ex.getMessage());
	}

	public static OperationResult getOperationResultNotFound(ResourceMessage resourceMessage) {
		return OperationResult.error(resourceMessage.getKeyOfResourceNotFound(),
				resourceMessage.getMessageOfResourceNotFound());
	}

}
