/**
 * JParse Java API for manipulating object on parse.com
 * Copyright (C) 2012 Ozgur Demir <ocd@mushrappa.org>
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

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
