package vttp.batch5.csf.assessment.server.controllers;

import java.io.StringReader;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import vttp.batch5.csf.assessment.server.services.RestaurantService;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {

  @Autowired RestaurantService restaurantService;

  // TODO: Task 2.2
  // You may change the method's signature
  @GetMapping(path = "/menu")
  public ResponseEntity<String> getMenus() {

    JsonArray jArray = restaurantService.getMenu();
    // System.out.println(">>>>>>>>>>>testing resp  :" + jArray);

    return ResponseEntity.ok(jArray.toString());
  }

  // TODO: Task 4
  // Do not change the method's signature
  @PostMapping(path = "/food_order")
  public ResponseEntity<String> postFoodOrder(@RequestBody String payload) throws NoSuchAlgorithmException{

    JsonReader details = Json.createReader(new StringReader(payload));
    JsonObject order = details.readObject();

    JsonArray items = order.getJsonArray("items");

    double total = 0;
    DecimalFormat df = new DecimalFormat("####0.00");

    for(JsonValue obj : items){
      JsonObject temp = obj.asJsonObject();

      double price = temp.getJsonNumber("price").doubleValue();
      int qty = temp.getInt("quantity");

      System.out.println("price" + price);
      System.out.println("qty" + qty);

      total += (price * qty);
    }


    boolean bool = restaurantService.validate(order);

    if (bool == true) {
      //place order

      String orderId = restaurantService.placeOrder(order);
      JsonObject success = Json.createObjectBuilder()
        .add("order_id", orderId)
        .add("payer", order.getString("username"))
        .add("payee", "Phuar Yilin Samuel")
        .add("payment", Double.valueOf(df.format(total)))
        .build();

      return ResponseEntity.ok(success.toString());

    }

    JsonObject errObj = Json.createObjectBuilder()
      .add("message", "Invalid username and/or password")
      .build();

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errObj.toString());
  } 


  @PostMapping(path = "/payment")
  public ResponseEntity<String> payment(@RequestBody String payload){

    JsonReader details = Json.createReader(new StringReader(payload));
    JsonObject order = details.readObject();
    // System.out.println(">>>>>>>order \n\n\n" + order);

    String response =  restaurantService.makePayment(order);
  
    JsonReader respDetails = Json.createReader(new StringReader(response));
    JsonObject additionalDetails = respDetails.readObject();

    // System.out.println(">>>>>>>chuk's \n\n\n" + additionalDetails);
    // System.out.println(">>>>>>>chuk time's \n\n\n" + additionalDetails.getInt("timestamp"));

    //from chuk's response: grab payment_id, order_id, timestamp in date
    //from order object, grab order_id, payer, payment 

    //sql

    //mongo
    String pattern = "MM/dd/yyyy HH:mm:ss";
    DateFormat df = new SimpleDateFormat(pattern);
    String dateString = df.format(new Date(String.valueOf(additionalDetails.getInt("timestamp"))));

    System.out.println(dateString);

    JsonObject mongo = Json.createObjectBuilder()
      .add("_id", additionalDetails.getString("order_id"))
      .add("order_id", additionalDetails.getString("order_id"))
      .add("payment_id", additionalDetails.getString("payment_id"))
      .add("username", order.getJsonObject("results").getString("payer"))
      .add("total", order.getJsonObject("results").getJsonNumber("payment").doubleValue())
      .add("timestamp", dateString)
      .add("items", order.getJsonArray("menu"))
      .build();


    // restaurantService.insert(payload);



    return null;

  }

}
