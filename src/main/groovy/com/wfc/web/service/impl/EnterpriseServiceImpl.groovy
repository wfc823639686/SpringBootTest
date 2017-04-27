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
        FileReader fr
        try {
            fr = new FileReader(UploadUtils.PF_NAME)
            List<String> lines = fr.readLines()
            for (String line : lines) {
                println(line)
                String[] splits = line.split(' ')
                if (splits.length == 3) {
                    String[] ids = splits[1].split(',')
                    String[] photos = splits[2].split(',')
                    for (String idstr : ids) {
                        int id = Integer.parseInt(idstr)
                        for (int i = 0; i < photos.length; i++) {
                            updPhoto(id, photos[i], i + 1)
                        }
                    }
                } else {
                    println(line)
                }
            }
        } catch (Exception e) {
            e.printStackTrace()
        }
        if (fr != null)
            fr.close()
    }

    def updPhoto(int id, String photo, int pos) {
        EnterprisePhoto ep = new EnterprisePhoto([
                'enterpriseId': id,
                'photo'       : photo,
                'thumb'       : photo + '@!ephotos',
                'pos'         : pos
        ])
        insertPhoto(ep)
    }

    def listPhotoDir2File() {
        PrintStream ps
        try {
            File lf = new File(UploadUtils.PF_NAME)
            if (lf.exists()) {
                lf.delete()
            }
            lf.createNewFile()
            ps = new PrintStream(new FileOutputStream(lf))
            UploadUtils.listPhotoDir({
                String name, List ds ->
                    List<Integer> ids = enterpriseMapper.getIdsByShortName(name)
                    String s = "${name} ${ids.join(',')} ${ds.join(',')}\n"
                    ps.print(s)
            })
            ps.flush()
            ps.close()
        } catch (Exception e) {
            e.printStackTrace()
        }
        if (ps != null)
            ps.close()
    }

    def updVideos() {
        FileReader fr
        try {
            fr = new FileReader(UploadUtils.VF_NAME)
            List<String> lines = fr.readLines()
            for (String line : lines) {
                println(line)
                String[] splits = line.split(' ')
                if (splits.length == 4) {
                    int times = Integer.parseInt(splits[1])
                    String[] ids = splits[2].split(',')
                    String[] videos = splits[3].split(',')
                    for (String idstr : ids) {
                        int id = Integer.parseInt(idstr)
                        updVideo(id, videos[0], videos[1], times)
                    }
                } else {
                    println(line)
                }
            }
        } catch (Exception e) {
            e.printStackTrace()
        }
        if (fr != null)
            fr.close()
    }

    def updVideo(int id, String v, String p, int  times) {
        EnterpriseVideo ev = new EnterpriseVideo([
                'enterpriseId': id,
                'video'       : v,
                'photo'       : p,
                'times'       : times,
                'status'      : 1
        ])
        insertVideo(ev)
    }

    def listVideoDir2File() {
        PrintStream ps
        try {
            File lf = new File(UploadUtils.VF_NAME)
            if (lf.exists()) {
                lf.delete()
            }
            lf.createNewFile()
            ps = new PrintStream(new FileOutputStream(lf))
            UploadUtils.listVideoDir({
                String name, String[] ds ->
                    String[] ss = name.split(' ')
                    List<Integer> ids = enterpriseMapper.getIdsByShortName(ss[0])
                    String s = "${name} ${ids.join(',')} ${ds.join(',')}\n"
                    ps.print(s)
            })
            ps.flush()
            ps.close()
        } catch (Exception e) {
            e.printStackTrace()
        }
        if (ps != null)
            ps.close()
    }
}
