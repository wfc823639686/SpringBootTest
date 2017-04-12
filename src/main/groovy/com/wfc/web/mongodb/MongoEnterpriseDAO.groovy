package com.wfc.web.mongodb

import com.wfc.web.model.MongoEnterprise
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.geo.Point
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

/**
 * Created by wangfengchen on 2017/3/17.
 */
@Repository
class MongoEnterpriseDAO {


    final static String COLLECTION_NAME = "enterprise";

    @Autowired
    MongoTemplate mongoTemplate;

    void insert(MongoEnterprise e) {
        mongoTemplate.insert(e, COLLECTION_NAME);
    }

    List<MongoEnterprise> sortByDistance(double lng, double lat) {
//        Circle circle = new Circle(-73.99171, 40.738868, 0.003712240453784);
        Query q = new Query(
                Criteria.where("loc")
                        .nearSphere(new Point(lng, lat)))
                .skip(5)
                .limit(5)

        mongoTemplate.find(q, MongoEnterprise.class, COLLECTION_NAME)
    }

}
