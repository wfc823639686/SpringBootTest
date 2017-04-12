package com.wfc.web.model

/**
 * Created by wangfengchen on 2016/11/21.
 */
class Enterprise extends User {
    Integer id
    String fullName, shortName
    Integer scale, videoCount
    Double lng, lat
    Integer province, city, district
    String intro
    Integer auth
    List<EnterpriseCommodity> commodities
}
