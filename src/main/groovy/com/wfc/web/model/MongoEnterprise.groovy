package com.wfc.web.model

import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.mapping.Document
import com.google.code.morphia.annotations.Id

/**
 * Created by wangfengchen on 2017/3/17.
 */
@Document(collection="enterprise")
class MongoEnterprise {
    @Id
    String id;
    GeoJsonPoint loc;
}
