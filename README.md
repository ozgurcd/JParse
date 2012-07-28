JParse
======
Provides Java classes for using parse.com for storing JSON objects over REST API


Requirements
======
Google Gson

Usage
======
Say that you have a POJO as follows:

public class Band {
  private String name;
  private String origin;
  private Collection<String> members = null;
  
  public Band() { }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getOrigin() {
    return origin;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
  }

  public Collection<String> getMembers() {
    return members;
  }

  public void setMembers(Collection<String> members) {
    this.members = members;
  }
}


1. Create a Parse instance with your application id and rest api key.

Parse parse = new Parse("YOUR_APPLICATION_ID", "REST_API_KEY");

2. Create a JsonObject to specify your search criteria

JsonObject queryJO = new JsonObject();
queryJO.addProperty("name", "Rammstein");

3. Create a Type object to store your target objects type

Type type = new TypeToken<Band>() {}.getType();

4. Fire your query:

ArrayList<Object> results = prs.query("Band", queryJO.toString(), type);

First argument "Band" is the class name on Parse.com. This query returns all the objects where name is Rammstein.
