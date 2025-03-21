package vttp.batch5.csf.assessment.server.services;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import vttp.batch5.csf.assessment.server.repositories.OrdersRepository;
import vttp.batch5.csf.assessment.server.repositories.RestaurantRepository;

@Service
public class RestaurantService {

  @Autowired
  private OrdersRepository ordersRepository;

  @Autowired
  private RestaurantRepository restaurantRepository;

  public static final String PYMT_URL = "https://payment-service-production-a75a.up.railway.app/api/payment";


  // TODO: Task 2.2
  // You may change the method's signature
  public JsonArray getMenu() {
    List<Document> docList = ordersRepository.getMenu();

    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
    List<JsonObject> jObjList = docList.stream()
      .map( doc -> {
        JsonObject temp = Json.createObjectBuilder()
          .add("id", doc.getString("_id"))
          .add("name", doc.getString("name"))
          .add("description", doc.getString("description"))
          .add("price", doc.getDouble("price"))
          .build();
          return temp;
      }).toList();

    for(JsonObject obj : jObjList){
      arrayBuilder.add(obj);
    }

    return arrayBuilder.build();
  }
  
  // TODO: Task 4

  public boolean validate(JsonObject order) throws NoSuchAlgorithmException{
    String username = order.getString("username");
    String password = order.getString("password");

    boolean bool = restaurantRepository.validate(username, password);

    return bool;
  }

  public String placeOrder(JsonObject object){
    return UUID.randomUUID().toString().substring(0, 8);
  }



  public String makePayment(JsonObject order){
    try {

    RequestEntity<String> req = RequestEntity
      .post(PYMT_URL)
      .header("X-Authenticate", order.getJsonObject("results").getString("payer"))
      .header("Content-Type", "application/json")
      .body(order.getJsonObject("results").toString(), String.class);

    RestTemplate template = new RestTemplate();
      ResponseEntity<String> resp = template.exchange(req, String.class); 
      String payload = resp.getBody();
      return payload;
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return null;
  }


/*
 
	order_id char(8) unique not null primary key,
    payment_id varchar(128) unique not null,
    order_date date not null,
    total float(8, 2) not null,
    username varchar(128) not null,
	constraint fk_username foreign key(username) REFERENCES customers(username)
 */

  public void insert(String payload){

  }





}
