package com.rlc.http.httpserver;

/**
 * 解析http request header头部异常
 */
public class RequestHeaderParseException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 6597955667743961603L;

  public RequestHeaderParseException() {
    super();
  }

  public RequestHeaderParseException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public RequestHeaderParseException(String message, Throwable cause) {
    super(message, cause);
  }

  public RequestHeaderParseException(String message) {
    super(message);
  }

  public RequestHeaderParseException(Throwable cause) {
    super(cause);
  }

}
