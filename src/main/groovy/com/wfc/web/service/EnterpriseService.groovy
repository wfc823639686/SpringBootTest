package com.wfc.web.service

import com.wfc.web.model.Enterprise
import com.wfc.web.model.EnterpriseFamous
import com.wfc.web.model.EnterpriseVideo
import com.wfc.web.model.MongoEnterprise

/**
 * Created by wangfengchen on 2016/11/24.
 */
interface EnterpriseService {

    def insertVideo(EnterpriseVideo enterpriseVideo)

    def insertVideos();

    Enterprise getInfo(int id)

    List<Enterprise> getEnterprises();

    int insertMongo(Enterprise e);

    List<MongoEnterprise> sortByDistance(double lng, double lat)

    def updPhotos(List<String> list)

    def listPhotoDir2File()

    def fastUpdPhotos()

    def updVideos()

    def listVideoDir2File()

    def updateLocation()
}
