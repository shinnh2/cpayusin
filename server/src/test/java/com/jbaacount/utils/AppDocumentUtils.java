package com.jbaacount.utils;

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

public interface AppDocumentUtils
{
    static OperationRequestPreprocessor getRequestPreProcessor()
    {
        return preprocessRequest(prettyPrint());
    }

    static OperationResponsePreprocessor getResponsePreProcessor()
    {
        return preprocessResponse(prettyPrint());
    }
}
