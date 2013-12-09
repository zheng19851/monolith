package com.kongur.monolith.common.poi;

/**
 * µ¼³öÒì³£
 * 
 * @author zhengwei
 */
public class ExportException extends RuntimeException {

    public ExportException(String message) {
        super(message);
    }

    public ExportException(Throwable e) {
        super(e);
    }

    public ExportException(String message, Throwable e) {
        super(message, e);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1015100126559746382L;

}
