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

and you create an instance as follows:

Set<String> members = new HashSet<String>();
members.add("Till Lindemann");
members.add("Richard Zven Kruspe");
members.add("Paul H. Landers");
members.add("Oliver Riedel");
members.add("Christoph Schneider");
members.add("Christian Lorenz");

Band band = new Band();
band.setName("Rammstein"):
band.setOrigin("Germany");
band.setMembers(members);

1. Create a Parse instance with your application id and rest api key.

Parse parse = new Parse("YOUR_APPLICATION_ID", "REST_API_KEY");

To Save this Object to parse.com

2a. call the store() method

String id = prs.store("ZBand", obj);

This method returns the id of the created object

To query existing objects

2b. Create a JsonObject to specify your search criteria

JsonObject queryJO = new JsonObject();
queryJO.addProperty("name", "Rammstein");

3. Create a Type object to store your target objects type

Type type = new TypeToken<Band>() {}.getType();

4. Fire your query:

ArrayList<Object> results = prs.query("Band", queryJO.toString(), type);

First argument "Band" is the class name on Parse.com. This query returns all the objects where name is Rammstein.

