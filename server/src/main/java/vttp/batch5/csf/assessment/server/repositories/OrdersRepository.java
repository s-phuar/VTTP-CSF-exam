package vttp.batch5.csf.assessment.server.repositories;


import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;


@Repository
public class OrdersRepository {

  @Autowired
  private MongoTemplate template;

  // TODO: Task 2.2
  // You may change the method's signature
  // Write the native MongoDB query in the comment below
  //
  //  Native MongoDB query here
  /* 
    db.menus.find({
      "name": { $ne: null, $ne: ""}
      })
      .sort({"name": 1})
  */
  public List<Document> getMenu() {

    Criteria criteria = Criteria.where("name").ne(null).ne("");
    Query query = Query.query(criteria)
      .with(Sort.by(Sort.Direction.ASC, "name"));

    return template.find(query,Document.class, "menus");
  }

  // TODO: Task 4
  // Write the native MongoDB query for your access methods in the comment below
  //
  //  Native MongoDB query here
  	/*db.orders.insertOne({
    "_id": "19c2fe46",
    "order_id": "19c2fe46",
    "payment_id": "01JPVVQ4Q45DVVQPT74XWY985J",
    "username": "fred",
    "timestamp": MM/dd/yyyy HH:mm:ss,

  "items": [
    {
      "id": "9aedc2a8",
      "price": 9.2,
      "quantity": 1
    },
    {
      "id": "b9f0f5e1",
      "price": 7.7,
      "quantity": 1
    },
    {
      "id": "4936f7a8",
      "price": 6.1,
      "quantity": 1
    }
  ]

	 })
	*/
  public void insertOrder(Document doc){
    
    template.insert(doc, "orders");
  }

  
}
