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
import com.wfc.web.common.utils.UploadUtils
import com.wfc.web.mapper.EnterpriseMapper
import com.wfc.web.mapper.EnterprisePhotoMapper
import com.wfc.web.model.Enterprise
import com.wfc.web.model.EnterpriseFamous
import com.wfc.web.model.EnterprisePhoto
import com.wfc.web.model.EnterpriseVideo
import com.wfc.web.model.QueryJobsModel
import com.wfc.web.rabbitmq.Sender
import com.wfc.web.service.CommonService
import com.wfc.web.service.EnterpriseService
import com.wfc.web.service.JobService
import com.wfc.web.service.UserService
import org.apache.commons.lang3.StringUtils

//import org.springframework.amqp.core.AmqpTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.ListOperations
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

import javax.servlet.http.HttpServletRequest

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
    @Autowired
    Sender sender
    @Autowired
    ProducerBean producer
    @Autowired
    EnterpriseMapper enterpriseMapper
    @Autowired
    EnterprisePhotoMapper enterprisePhotoMapper
    @Autowired
    JdbcTemplate jdbcTemplate;

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


    @RequestMapping("/delRepeat")
    def delRepeat() {
        List<Enterprise> list = enterpriseMapper.getRepeats();
        for (Enterprise e : list) {
            println(e.fullName)
            List<Enterprise> subList = enterpriseMapper.getEnterprisesByFullName(e.fullName)
            if (subList != null && subList.size() > 1) {
                for (int i = 0; i < subList.size(); i++) {
                    Enterprise e1 = subList.get(i)
                    println(e1.id)
                    del(e1.id)
                }
                println("---")
            }
        }
    }

    private del(int id) {
        try {
            jdbcTemplate.update("delete from enterprise where id=${id}")
            jdbcTemplate.update("delete from user where id=${id}")
            jdbcTemplate.update("delete from  enterprise_comment  where enterprise_id =${id} and id>0")
            jdbcTemplate.update("delete from  enterprise_comment_label  where enterprise_id =${id} and id>0")
            jdbcTemplate.update("delete from  enterprise_commodity  where enterprise_id =${id} and id>0")
            jdbcTemplate.update("delete from  enterprise_famous  where enterprise_id =${id} and id>0")
            jdbcTemplate.update("delete from  enterprise_notice  where enterprise_id =${id} and id>0")
            jdbcTemplate.update("delete from  enterprise_report  where enterprise_id =${id} and id>0")
            jdbcTemplate.update("delete from  enterprise_time  where enterprise_id =${id} and enterprise_id>0")
            jdbcTemplate.update("delete from  enterprise_to_audit  where enterprise_id =${id} and id>0")
//            List<EnterprisePhoto> eps = enterprisePhotoMapper.getPhotos(id)
//            Set<String> set = new HashSet<>()
//            if (eps != null && eps.size() > 0) {
//                for (EnterprisePhoto ep : eps) {
//                    String s = ep.photo
//                    println(s)
//                    s = s.replace('http://ssb-img.shangshaban.com/', '')
//                    set.add(s)
//                }
//            }
//            List<String> list = new ArrayList<>()
//            list.addAll(set)
//            if (!list.isEmpty())
//                OSSApi.delObject("ssb-img", list)
            //删除相册
            jdbcTemplate.update("delete from enterprise_photo where enterprise_id =${id} and id>0")
        } catch (Exception e) {
            e.printStackTrace()
        }
    }


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
}
