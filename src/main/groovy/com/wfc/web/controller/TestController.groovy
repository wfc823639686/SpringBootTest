package com.wfc.web.controller

import com.aliyun.openservices.ons.api.Message
import com.aliyun.openservices.ons.api.SendResult
import com.aliyun.openservices.ons.api.bean.ProducerBean
import com.aliyun.oss.OSSException
import com.aliyun.oss.model.ObjectMetadata
import com.wfc.web.common.RestClient
import com.wfc.web.common.aliyun.OSSApi
import com.wfc.web.common.utils.EncryptUtils
import com.wfc.web.common.utils.JSONUtils
import com.wfc.web.common.utils.POIUtils
import com.wfc.web.common.utils.SecurityUtils
import com.wfc.web.common.utils.UploadUtils
import com.wfc.web.mapper.EnterpriseCommodityMapper
import com.wfc.web.mapper.EnterpriseMapper
import com.wfc.web.mapper.EnterprisePhotoMapper
import com.wfc.web.mapper.UserMapper
import com.wfc.web.model.Enterprise
import com.wfc.web.model.EnterpriseFamous
import com.wfc.web.model.EnterprisePhoto
import com.wfc.web.model.EnterpriseVideo
import com.wfc.web.model.QueryJobsModel
import com.wfc.web.model.User
import com.wfc.web.rabbitmq.Sender
import com.wfc.web.service.CommonService
import com.wfc.web.service.EnterpriseService
import com.wfc.web.service.JobService
import com.wfc.web.service.UserService
import org.apache.commons.lang3.StringUtils
import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.hssf.usermodel.HSSFRow
import org.apache.poi.hssf.usermodel.HSSFSheet
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.poifs.filesystem.POIFSFileSystem

//import org.springframework.amqp.core.AmqpTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.ListOperations
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.http.HttpHeaders
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowCallbackHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

import javax.servlet.http.HttpServletRequest
import java.sql.ResultSet
import java.sql.SQLException

/**
 * Created by wangfengchen on 2016/11/21.
 */

@RestController
@RequestMapping("/test")
class TestController {

    @Autowired
    UserService userService
    @Autowired
    EnterpriseService enterpriseService
    @Autowired
    CommonService commonService
    @Autowired
    JobService jobService
    @Autowired
    StringRedisTemplate redisTemplate
//    @Autowired
//    Sender sender
//    @Autowired
//    ProducerBean producer
    @Autowired
    EnterpriseMapper enterpriseMapper
    @Autowired
    EnterprisePhotoMapper enterprisePhotoMapper
    @Autowired
    JdbcTemplate jdbcTemplate
    @Autowired
    UserMapper userMapper

    @RequestMapping("/test")
    Object test(Integer id) {
        println('id ' + id)
        return userService.getUser(id)
    }

    @RequestMapping("/token")
    def token(String d, String timestamp, String r, String secret) {
        def token = "";
        token = EncryptUtils.MD5(r + timestamp + secret + d)
        token;
    }

    @RequestMapping("/cities")
    def cities() {
        commonService.cities()
    }

    @RequestMapping("/jobs")
    def jobs(QueryJobsModel q) {
        jobService.getJobs(q)
    }

    @RequestMapping("/getLabels")
    def getLabels(String key) {
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        List<String> list = listOperations.range(key, 0, listOperations.size(key));
        return list
    }

    @RequestMapping("/send")
    def send(String tag, String body) {
//        sender.send();
        def topic = "RAY_TOPIC_TEST"
        def map = ['uid': 3, 'body': body]
        Message msg = new Message(topic, tag, JSONUtils.toJson(map).bytes)
        SendResult sendResult = producer.send(msg)
        if (sendResult != null) {
            println("" + new Date() + " Send mq message success! Topic is:" + topic + " msgId is: " + sendResult.getMessageId())
        }
    }

    @RequestMapping("/http")
    def http() {
    }

//    @RequestMapping("/delRepeat")
//    def delRepeat() {
//        List<Enterprise> list = enterpriseMapper.getRepeats();
//        for (Enterprise e : list) {
//            println(e.fullName)
//            List<Enterprise> subList = enterpriseMapper.getEnterprisesByFullName(e.fullName)
//            if (subList != null && subList.size() > 1) {
//                for (int i = 0; i < subList.size(); i++) {
//                    Enterprise e1 = subList.get(i)
//                    println(e1.id)
//                    del(e1.id)
//                }
//                println("---")
//            }
//        }
//    }

//    private del(int id) {
//        try {
//            jdbcTemplate.update("delete from enterprise where id=${id}")
//            jdbcTemplate.update("delete from user where id=${id}")
//            jdbcTemplate.update("delete from  enterprise_comment  where enterprise_id =${id} and id>0")
//            jdbcTemplate.update("delete from  enterprise_comment_label  where enterprise_id =${id} and id>0")
//            jdbcTemplate.update("delete from  enterprise_commodity  where enterprise_id =${id} and id>0")
//            jdbcTemplate.update("delete from  enterprise_famous  where enterprise_id =${id} and id>0")
//            jdbcTemplate.update("delete from  enterprise_notice  where enterprise_id =${id} and id>0")
//            jdbcTemplate.update("delete from  enterprise_report  where enterprise_id =${id} and id>0")
//            jdbcTemplate.update("delete from  enterprise_time  where enterprise_id =${id} and enterprise_id>0")
//            jdbcTemplate.update("delete from  enterprise_to_audit  where enterprise_id =${id} and id>0")
////            List<EnterprisePhoto> eps = enterprisePhotoMapper.getPhotos(id)
////            Set<String> set = new HashSet<>()
////            if (eps != null && eps.size() > 0) {
////                for (EnterprisePhoto ep : eps) {
////                    String s = ep.photo
////                    println(s)
////                    s = s.replace('http://ssb-img.shangshaban.com/', '')
////                    set.add(s)
////                }
////            }
////            List<String> list = new ArrayList<>()
////            list.addAll(set)
////            if (!list.isEmpty())
////                OSSApi.delObject("ssb-img", list)
//            //删除相册
//            jdbcTemplate.update("delete from enterprise_photo where enterprise_id =${id} and id>0")
//        } catch (Exception e) {
//            e.printStackTrace()
//        }
//    }


    @RequestMapping("/getPhotoStatus")
    def getPhotoStatus() {
        long st = System.currentTimeMillis()
        Set<Integer> errorPhotos = new HashSet<>()
        List<EnterprisePhoto> eps = enterprisePhotoMapper.getAll()
        for (EnterprisePhoto ep : eps) {
            println(ep.getId())
            String s = ep.photo
            println(s)
            s = s.replace('http://ssb-img.shangshaban.com/', '')
            try {
                ObjectMetadata om = OSSApi.getObjectMetadata("ssb-img", s)
                println(om.getObjectType())
            } catch (OSSException e) {
                if (StringUtils.equals(e.getErrorCode(), "NoSuchKey")) {
                    enterprisePhotoMapper.delete(ep.getId())
                    errorPhotos.add(ep.getId())
                }
            } catch (Exception ex) {
                ex.printStackTrace()
            }
        }
        long et = System.currentTimeMillis()
        long r = (et - st) / 1000
        println("执行时间 ${r}s")
        errorPhotos
    }

//    @RequestMapping("/updateEaseMob")
//    def updateEaseMob() {
//        List<User> us = userMapper.getByEaseMobNameIsNull()
//        for (User u : us) {
//            println(u.id)
//            String e = String.format("ssb%010d", u.getId())
//            println(e)
//            Object r = RestClient.post("http://test-portals.shangshaban.com/common/registerEasemob.do",
//                    [
//                            "clientId"    : "",
//                            "clientSecret": "",
//                            "orgName"     : "",
//                            "appName"     : "",
//                            "u"           : e,
//                            "p"           : e,
//                            "userExisted" : "0"
//                    ])
//            println(JSONUtils.toJson(r))
//            u.setEaseMobName(e)
//            u.setEaseMobPassword(e)
//            userMapper.updateEaseMob(u)
//
//        }
//    }

//    @RequestMapping("/resetEaseMobPassword")
//    def resetEaseMobPassword() {
//        for (i in 72876.. 73195) {
//            println(i)
//            String e = String.format("ssb%010d", i)
//            println(e)
//            Object r = RestClient.post("http://localhost:8080/common/registerEasemob.do",
//                    [
//                            "clientId"    : "",
//                            "clientSecret": "",
//                            "orgName"     : "shangshaban",
//                            "appName"     : "ssb",
//                            "u"           : e,
//                            "p"           : "",
//                            "userExisted" : "0"
//                    ])
//            println(JSONUtils.toJson(r))
//        }
//
//    }

//    @RequestMapping("/updateIdentityCode")
//    def updateIdentityCode() {
//        String sql = "select id, identity_code from job where identity_code is null"
//        List<Map> r = jdbcTemplate.queryForList(sql)
//        def size = r.size()
//        int i = 0
//        for (Map m : r) {
//            i++
//            long id = m.get("id") as long
//            println("${i}/${size} ----- ${id}")
//            def s = SecurityUtils.encrypt1(id)
//            update(id, s)
//        }
//    }
//
//    def update(id, code) {
//        jdbcTemplate.update("update job set identity_code=${code} where id=${id}")
//    }

//    @RequestMapping("/updVideoStatus")
//    def updVideoStatus() {
//        String fp = "/Users/wangfengchen/Downloads/上传的公司视频.xls"
//        POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(fp))
//        //得到Excel工作簿对象
//        HSSFWorkbook wb = new HSSFWorkbook(fs)
//        //得到Excel工作表对象
//        HSSFSheet sheet = wb.getSheetAt(0)
//        int last = sheet.getLastRowNum()
//        println('last ' + last)
//
//        try {
//            Set<Integer> all = new HashSet<>()
//            for (i in 0..last) {
//                HSSFRow row = sheet.getRow(i)
//                HSSFCell cell = row.getCell(0)
//                if (cell != null) {
//                    String shortName = POIUtils.getCellStringValue(cell)
//                    if (StringUtils.isNotBlank(shortName)) {
//                        all.addAll(getIdByShortName(shortName.trim()))
//                    }
//                }
//                HSSFCell cell1 = row.getCell(1)
//                if (cell1 != null) {
//                    String fullName = POIUtils.getCellStringValue(cell1)
//                    if (StringUtils.isNotBlank(fullName)) {
//                        all.addAll(getIdByFullName(fullName.trim()))
//                    }
//                }
//            }
//            println( JSONUtils.toJson(all))
//            for (Integer id : all) {
//                println("enterprise id "+id)
//                updVideoDownLine(id)
//            }
//        } catch (Exception e) {
//            e.printStackTrace()
//        }
//    }
//
//
//    List<Integer> getIdByFullName(String fn) {
//        String url = "select id from enterprise where full_name='${fn}'"
//        return jdbcTemplate.queryForList(url, Integer.class)
//    }
//
//
//    List<Integer> getIdByShortName(String sn) {
//        String url = "select id from enterprise where short_name='${sn}'"
//        return jdbcTemplate.queryForList(url, Integer.class)
//    }
//
//    def updVideoDownLine(int enterpriseId) {
//        jdbcTemplate.update("update enterprise_video set status=2 where enterprise_id=${enterpriseId} and status=1")
//    }

    @RequestMapping("/genSensitiveWords")
    def genSensitiveWords(String src, String fn) {
        String filepath = "/User/wangfengchen/Downloads/lex-${fn}.lex"
        String fp = "/Users/wangfengchen/Downloads/${src}.xls"
        POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(fp))
        //得到Excel工作簿对象
        HSSFWorkbook wb = new HSSFWorkbook(fs)
        //得到Excel工作表对象
        HSSFSheet sheet = wb.getSheetAt(0)
        int last = sheet.getLastRowNum()
        println('last ' + last)
        PrintStream ps
        try {
            File lf = new File(filepath)
            if (lf.exists()) {
                lf.delete()
            }
            lf.createNewFile()
            ps = new PrintStream(new FileOutputStream(lf))
            for (i in 0..last) {
                HSSFRow row = sheet.getRow(i)
                HSSFCell cell = row.getCell(0)
                if (cell != null) {
                    String text = POIUtils.getCellStringValue(cell).trim()
                    ps.print(text)
                    ps.println("///null")
                }
            }
            ps.flush()
        } catch (Exception e) {
            e.printStackTrace()
        } finally {
            if (ps != null)
                ps.close()
        }
    }

}
