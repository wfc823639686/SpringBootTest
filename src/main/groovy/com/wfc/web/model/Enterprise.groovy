package com.wfc.web.model

/**
 * Created by wangfengchen on 2016/11/21.
 */
class Enterprise extends User {
    Integer id
    String fullName, shortName, head
    Integer scale, videoCount
    Double lng, lat
    Integer province, city, district
    String intro
    Integer auth
    List<EnterpriseCommodity> commodities


    @Override
    public String toString() {
        return "Enterprise{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", shortName='" + shortName + '\'' +
                ", head='" + head + '\'' +
                ", scale=" + scale +
                ", videoCount=" + videoCount +
                ", lng=" + lng +
                ", lat=" + lat +
                ", province=" + province +
                ", city=" + city +
                ", district=" + district +
                ", intro='" + intro + '\'' +
                ", auth=" + auth +
                ", commodities=" + commodities +
                '}';
    }
}
