package com.github.sinsinpub.doc.web.exception;

public class DataNotFoundException extends RuntimeSqlException {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    public DataNotFoundException() {
        super();
    }

    public DataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataNotFoundException(String message) {
        super(message);
    }

    public DataNotFoundException(Throwable cause) {
        super(cause);
    }

}
