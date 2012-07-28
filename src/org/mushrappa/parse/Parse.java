package org.mushrappa.parse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.mushrappa.parse.exceptions.JParseException;

public class Parse {
  private ReqHeaders rh = null;
  private URIBuilder builder = null;
  
  public Parse(
      String applicationID,
      String restAPIKey) {
    this.rh = new ReqHeaders(applicationID, restAPIKey);
    this.builder = new URIBuilder();
    builder.setScheme("https")
    .setHost("api.parse.com");
  }
  
  public String query(String className, String query) {
    builder.setPath("/1/classes/" + className);
    builder.addParameter("where", query);
    String result = null;
    try {
      result = doGet(builder.build());
    } catch (URISyntaxException e) {
      e.printStackTrace();
    } catch (JParseException e) {
      e.printStackTrace();
    }
    return result;
  }
  
  public String getByObjectId(String className, String id) {
    builder.setPath("/1/classes/" + className + "/" + id);
    
    String result = null;
    try {
      result = doGet(builder.build());
    } catch (URISyntaxException e) {
      e.printStackTrace();
    } catch (JParseException e) {
      e.printStackTrace();
    }
    return result;
  }
  
  private String doGet(URI uri)
      throws JParseException {
    HttpClient client = new DefaultHttpClient();
    BufferedReader in = null;
    StringBuilder sb = new StringBuilder();
    String line = null;
    
    try {
      HttpGet hget = new HttpGet(uri);
      
      hget.setHeader("X-Parse-Application-Id", rh.getApplicationID());
      hget.setHeader("X-Parse-REST-API-Key", rh.getRestAPIKey());
      hget.setHeader("Accept-Charset","UTF-8");
      
      HttpResponse response = client.execute(hget);
      StatusLine status = response.getStatusLine();
      System.out.println("Status code: " + status.getStatusCode());
      System.out.println("Status Mesg: " + status.getReasonPhrase());
      
      HttpEntity entity = response.getEntity();
      if (entity != null) {
        in = new BufferedReader(new InputStreamReader(entity.getContent()));
        while ((line = in.readLine()) != null) {
          sb.append(line);
        }
      }
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      client.getConnectionManager().shutdown();
    }
    
    return sb.toString();
  }
  
}
