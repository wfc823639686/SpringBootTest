package com.wfc.web.service.impl

import com.sun.xml.internal.ws.developer.EPRRecipe
import com.wfc.web.common.aliyun.OSSApi
import com.wfc.web.common.aliyun.OSSConstants
import com.wfc.web.common.utils.POIUtils
import com.wfc.web.common.utils.UploadUtils
import com.wfc.web.mapper.EnterprisePhoto1Mapper
import com.wfc.web.mapper.EnterprisePhotoMapper
import com.wfc.web.model.EnterprisePhoto
import com.wfc.web.mongodb.MongoEnterpriseDAO
import com.wfc.web.mapper.EnterpriseMapper
import com.wfc.web.mapper.EnterpriseVideoMapper
import com.wfc.web.model.Enterprise
import com.wfc.web.model.EnterpriseVideo
import com.wfc.web.model.MongoEnterprise

import com.wfc.web.service.EnterpriseService
import org.apache.commons.lang3.StringUtils
import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.hssf.usermodel.HSSFRow
import org.apache.poi.hssf.usermodel.HSSFSheet
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.poifs.filesystem.POIFSFileSystem
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
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
    EnterprisePhoto1Mapper enterprisePhoto1Mapper
    @Autowired
    MongoEnterpriseDAO mongoEnterpriseDAO
    @Value('${config.locationExcelFilePath}')
    String locationExcelFilePath


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
        int i = enterprisePhotoMapper.insert(ep)
        println(i)
    }

    @Override
    def updPhotos(List<String> list) {
        FileReader fr
        try {
            List<String> lines;
            if (list == null) {
                fr = new FileReader(UploadUtils.PF_NAME)
                lines = fr.readLines()
            } else {
                lines = list
            }

            for (String line : lines) {
                line.replace('\n', '')
                println(line)
                String[] splits = line.split(' ')
                if (splits.length == 3) {
                    if (StringUtils.isNotBlank(splits[1])) {
                        String[] ids = splits[1].split(',')
                        String[] photos = splits[2].split(',')
                        for (String idstr : ids) {
                            int id = Integer.parseInt(idstr)
                            for (int i = 0; i < photos.length; i++) {
                                if (i == 0) {
                                    updLogo(id, photos[i])
                                } else {
                                    updPhoto(id, photos[i], i)
                                }
                            }
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

    def updLogo(int id, String photo) {
        Enterprise e = new Enterprise([
                "head": photo,
                "id"  : id
        ])
        println('update head ' + id)
        int i = enterpriseMapper.updateLogo(e)
        println(i)
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
        List<String> list = []
        PrintStream ps, eps
        try {
            File lf = new File(UploadUtils.PF_NAME)
            File elf = new File(UploadUtils.EPF_NAME)
            if (lf.exists()) {
                lf.delete()
            }
            if (elf.exists()) {
                elf.delete()
            }
            lf.createNewFile()
            elf.createNewFile()
            ps = new PrintStream(new FileOutputStream(lf))
            eps = new PrintStream(new FileOutputStream(elf))
            UploadUtils.listPhotoDir({
                String name, List ds ->
                    println(name)
                    List<Integer> ids = enterpriseMapper.getIdsByShortName(name)
                    String s = "${name} ${ids.join(',')} ${ds.join(',')}\n"
                    list.add(s)
                    ps.print(s)
                    if (ids == null || ids.isEmpty())
                        eps.print(s)
            })
            ps.flush()
            eps.flush()
        } catch (Exception e) {
            e.printStackTrace()
        }
        if (ps != null)
            ps.close()
        if (eps != null)
            eps.close()
        return list
    }

    @Override
    def fastUpdPhotos() {
        List<String> list = listPhotoDir2File()
        updPhotos(list)
    }

    def updVideos() {
        FileReader fr
        try {
            fr = new FileReader(UploadUtils.VF_NAME)
            List<String> lines = fr.readLines()
            for (String line : lines) {
                line.replace('\n', '')
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

    def updVideo(int id, String v, String p, int times) {
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

    @Override
    def updateLocation() {
        println(locationExcelFilePath)
        POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(locationExcelFilePath))
        //得到Excel工作簿对象
        HSSFWorkbook wb = new HSSFWorkbook(fs)
        //得到Excel工作表对象
        HSSFSheet sheet = wb.getSheetAt(0)
        int last = sheet.getLastRowNum()
        println('last ' + last)

        List<Enterprise> enterpriseList = []
        try {
            for (i in 0..last) {
                HSSFRow row = sheet.getRow(i)
                HSSFCell cell = row.getCell(0)
                String idStr = POIUtils.getCellStringValue(cell)
                if (StringUtils.isNotBlank(idStr)) {
                    int id = Float.parseFloat(idStr)
                    HSSFCell cell1 = row.getCell(1)
                    String lngStr = POIUtils.getCellStringValue(cell1)
                    double lng = Double.parseDouble(lngStr)
                    HSSFCell cell2 = row.getCell(2)
                    String latStr = POIUtils.getCellStringValue(cell2)
                    double lat = Double.parseDouble(latStr)
                    println "${id} ${lngStr} ${latStr}"
                    Enterprise e = new Enterprise([
                            "id" : id,
                            "lng": lng,
                            "lat": lat
                    ])
                    enterpriseList.add(e)
                }
            }
            for (Enterprise e : enterpriseList) {
//                println(e)
                println(e.id +' '+e.lng +' '+ e.lat)
                enterpriseMapper.updateLocation(e)
            }
        } catch (Exception e) {
            e.printStackTrace()
        }
    }

    def delPhoto() {
        int start = 0, offset = 1000
        int count = enterprisePhoto1Mapper.enterprisePhotoCount()
        int p = count / offset
        for (i in 0.. p) {
            start *=1000
            List<EnterprisePhoto> eps = enterprisePhoto1Mapper.getEnterprisePhotos(start, offset)
            Set<String> set = new HashSet<>()
            for (EnterprisePhoto ep : eps) {
                String s = ep.getPhoto();
                s = s.replace('http://ssb-img.shangshaban.com/', '')
                set.add(s)
                println(s)
            }
            List<String> list = new ArrayList<>()
            list.addAll(set)
            OSSApi.delObject("ssb-img", list)
        }
    }

}
