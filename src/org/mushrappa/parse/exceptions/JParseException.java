package org.mushrappa.parse.exceptions;

public class JParseException extends Exception {
  private static final long serialVersionUID = 7845198089116025901L;
  
  public JParseException() {
    super();
  }
  
  public JParseException(String str) {
    super(str);
  }
  
  public JParseException(Throwable thr) {
    super(thr);
  }
  
}
