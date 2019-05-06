package com.security.demo.entity;

/**
 * description
 *
 * @author fanglingxiao
 * @date 2019/5/6
 */
public class ExcelError {
    private String reason;
    private String errorLine;
    private String fieldName;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getErrorLine() {
        return errorLine;
    }

    public void setErrorLine(String errorLine) {
        this.errorLine = errorLine;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
