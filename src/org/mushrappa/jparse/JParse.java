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

package org.mushrappa.jparse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.mushrappa.jparse.exceptions.JParseException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JParse {
  private URIBuilder builder = null;
  private String applicationID;
  private String restAPIKey;
  
  public JParse(
      String applicationID,
      String restAPIKey) {
    this.applicationID = applicationID;
    this.restAPIKey = restAPIKey;
    
    this.builder = new URIBuilder();
    builder.setScheme("https")
    .setHost("api.parse.com");
  }
  
  public <T> ArrayList<T> query(
      String query,
      Class<T> classofT) throws JParseException {
    
    builder.setPath("/1/classes/" + classofT.getSimpleName());
    builder.addParameter("where", query);
    JsonObject result = null;
    
    try {
      result = doGet(builder.build());
    } catch (URISyntaxException e) {
      throw new JParseException(e);
    }
    
    ArrayList<T> or = new ArrayList<T>();
    if (result.get("results").isJsonArray()) {
      JsonArray array = result.get("results").getAsJsonArray();
      Gson gson = new Gson();
      for (JsonElement jel : array) {
        or.add(gson.fromJson(jel, classofT));
      }
    } else {
      throw new JParseException();
    }
    
    return or;
  }
  
  public String store(Object pojo) throws JParseException {
    builder.setPath("/1/classes/" + pojo.getClass().getSimpleName());
    
    JsonObject result = null;
    try {
      result = doPost(builder.build(), pojo);
    } catch (URISyntaxException e) {
      throw new JParseException(e);
    }
    
    return result.get("objectId").toString();
  }
  
  private JsonObject doGet(URI uri) throws JParseException {
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
      throw new JParseException(e);
    } catch (IOException e) {
      throw new JParseException(e);
    } finally {
      client.getConnectionManager().shutdown();
    }
    
    return new JsonParser().parse(sb.toString()).getAsJsonObject();
  }
  
  public JsonObject doPost(URI uri, Object pojo) throws JParseException {
    HttpClient client = new DefaultHttpClient();
    BufferedReader in = null;
    StringBuilder sb = new StringBuilder();
    String line = null;
    Gson gson = new Gson();
    
    try {
      HttpPost hpost = new HttpPost(uri);
      hpost.setHeader("X-Parse-Application-Id", applicationID);
      hpost.setHeader("X-Parse-REST-API-Key", restAPIKey);
      hpost.setHeader("Content-Type", "application/json");
      hpost.setHeader("Accept-Charset","UTF-8");
      
      HttpEntity reqEnt = new StringEntity(gson.toJson(pojo));
      hpost.setEntity(reqEnt);
      
      
      HttpResponse response = client.execute(hpost);
      HttpEntity resEnt = response.getEntity();
      if (resEnt != null) {
        in = new BufferedReader(new InputStreamReader(resEnt.getContent()));
        while ((line = in.readLine()) != null) {
          sb.append(line);
        }
      }
    } catch (UnsupportedEncodingException e) {
      throw new JParseException(e);
    } catch (ClientProtocolException e) {
      throw new JParseException(e);
    } catch (IOException e) {
      throw new JParseException(e);
    }
    
    return new JsonParser().parse(sb.toString()).getAsJsonObject();
  }
}
