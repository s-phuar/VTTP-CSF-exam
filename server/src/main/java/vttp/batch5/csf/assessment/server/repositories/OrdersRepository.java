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
  
}
