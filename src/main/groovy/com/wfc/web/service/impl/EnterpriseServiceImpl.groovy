package com.wfc.web.service.impl

import com.wfc.web.common.utils.UploadUtils
import com.wfc.web.mapper.EnterprisePhotoMapper
import com.wfc.web.model.EnterprisePhoto
import com.wfc.web.mongodb.MongoEnterpriseDAO
import com.wfc.web.mapper.EnterpriseMapper
import com.wfc.web.mapper.EnterpriseVideoMapper
import com.wfc.web.model.Enterprise
import com.wfc.web.model.EnterpriseVideo
import com.wfc.web.model.MongoEnterprise

import com.wfc.web.service.EnterpriseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.stereotype.Service

/**
 * Created by wangfengchen on 2016/11/24.
 */
@Service
class EnterpriseServiceImpl implements EnterpriseService {

    @Autowired
    EnterpriseVideoMapper enterpriseVideoMapper
    @Autowired
    EnterpriseMapper enterpriseMapper
    @Autowired
    EnterprisePhotoMapper enterprisePhotoMapper
    @Autowired
    MongoEnterpriseDAO mongoEnterpriseDAO

    @Override
    def insertVideo(EnterpriseVideo enterpriseVideo) {
        def r = enterpriseVideoMapper.insert(enterpriseVideo)
        return r
    }

    @Override
    def insertVideos() {
        UploadUtils.uploadVideos({
            Integer id, times ->
                def photo = "http://ssb-img.shangshaban.com/Image_" + id + "_20160901113830.jpg";
                def video = "http://ssb-video.shangshaban.com/Video_" + id + "_20160909100117.mp4";
                EnterpriseVideo ev = new EnterpriseVideo(
                        ['enterpriseId': id,
                         'photo'       : photo,
                         'video'       : video,
                         'times'       : times,
                         'status'      : 1
                        ])
                insertVideo(ev)
                enterpriseMapper.updateVideoCount(id)
        })
//        UploadUtils.uploadVideos()
    }

    @Override
    Enterprise getInfo(int id) {
        println("info " + id)
        return enterpriseMapper.getInfo(id)
    }

    @Override
    List<Enterprise> getEnterprises() {
        return enterpriseMapper.getEnterprises()
    }

    @Override
    int insertMongo(Enterprise e) {
        mongoEnterpriseDAO.insert(
                new MongoEnterprise(
                        "id": e.id, "loc": new GeoJsonPoint(e.lng, e.lat)
                ))
        return 0
    }

    @Override
    List<MongoEnterprise> sortByDistance(double lng, double lat) {
        mongoEnterpriseDAO.sortByDistance(lng, lat)
    }

    def insertPhoto(EnterprisePhoto ep) {
        println('insert photo ' + ep.getEnterpriseId())
        enterprisePhotoMapper.insert(ep)
    }

    @Override
    def updPhotos() {
        UploadUtils.updPhotos({
            String[] ss, List ds ->
                for (String s : ss) {
                    int id = Integer.parseInt(s)
                    for (int i = 0; i < ds.size(); i++) {
                        String d = ds.get(i)
                        EnterprisePhoto ep = new EnterprisePhoto([
                                'enterpriseId': id,
                                'photo': d,
                                'thumb': d + '@!ephotos',
                                'pos': i + 1
                        ])
                        insertPhoto(ep)
                    }
                }

        })
    }
}
