package com.github.sinsinpub.doc.web.exception;

public class RuntimeSqlException extends RuntimeException {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    public RuntimeSqlException() {
        super();
    }

    public RuntimeSqlException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuntimeSqlException(String message) {
        super(message);
    }

    public RuntimeSqlException(Throwable cause) {
        super(cause);
    }

}
