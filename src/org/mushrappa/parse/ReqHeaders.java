package org.mushrappa.parse;

public class ReqHeaders {
  private String applicationID = null;
  private String restAPIKey = null;
  
  public ReqHeaders() { }
  public ReqHeaders(String applicationID, String restAPIKey) {
    setApplicationID(applicationID);
    setRestAPIKey(restAPIKey);
  }
  
  public String getApplicationID() {
    return applicationID;
  }
  public void setApplicationID(String applicationID) {
    this.applicationID = applicationID;
  }
  public String getRestAPIKey() {
    return restAPIKey;
  }
  public void setRestAPIKey(String restAPIKey) {
    this.restAPIKey = restAPIKey;
  }
}
