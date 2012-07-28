package org.mushrappa.parse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.mushrappa.parse.exceptions.JParseException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Parse {
  private URIBuilder builder = null;
  private String applicationID;
  private String restAPIKey;
  
  public Parse(
      String applicationID,
      String restAPIKey) {
    this.applicationID = applicationID;
    this.restAPIKey = restAPIKey;
    
    this.builder = new URIBuilder();
    builder.setScheme("https")
    .setHost("api.parse.com");
  }
  
  public ArrayList<Object> query(
      String className,
      String query,
      Type type) throws JParseException {
    
    builder.setPath("/1/classes/" + className);
    builder.addParameter("where", query);
    JsonObject result = null;
    try {
      result = doGet(builder.build());
    } catch (URISyntaxException e) {
      e.printStackTrace();
    } catch (JParseException e) {
      e.printStackTrace();
    }
    
    ArrayList<Object> or = new ArrayList<Object>();
    
    if (result.get("results").isJsonArray()) {
      JsonArray array = result.get("results").getAsJsonArray();
      Gson gson = new Gson();
      for (JsonElement jel : array) {
        or.add(gson.fromJson(jel, type));
      }
    } else {
      throw new JParseException();
    }
    
    return or;
  }
  
  private JsonObject doGet(URI uri)
      throws JParseException {
    HttpClient client = new DefaultHttpClient();
    BufferedReader in = null;
    StringBuilder sb = new StringBuilder();
    String line = null;
    
    try {
      HttpGet hget = new HttpGet(uri);
      
      hget.setHeader("X-Parse-Application-Id", applicationID);
      hget.setHeader("X-Parse-REST-API-Key", restAPIKey);
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
    
    return new JsonParser().parse(sb.toString()).getAsJsonObject();
  }
}
