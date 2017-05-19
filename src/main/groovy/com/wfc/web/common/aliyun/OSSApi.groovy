package com.wfc.web.common.aliyun

import com.aliyun.oss.HttpMethod
import com.aliyun.oss.OSSClient
import com.aliyun.oss.model.DeleteObjectsRequest
import com.aliyun.oss.model.DeleteObjectsResult
import com.aliyun.oss.model.GeneratePresignedUrlRequest
import com.aliyun.oss.model.PutObjectResult
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.multipart.MultipartFile

/**
 * Created by wangfengchen on 2016/11/24.
 */
class OSSApi {

    /**
     * 上传文件
     *
     * @param filename 名称
     * @param file 文件
     * @throws Exception
     */
    static boolean putObject(String bucketName, String filename, MultipartFile file) throws Exception {
        return putObject(bucketName, filename, file.getInputStream(), file.getSize());
    }

    /**
     * @param filename 名称
     * @param filepath 文件路径
     * @throws Exception
     */
    static boolean putObject(String bucketName, String filename, String filepath) throws Exception {
        File file = new File(filepath);
        return putObject(bucketName, filename, file);
    }

    /**
     * 上传文件
     *
     * @param filename 名称
     * @param file 文件
     * @throws Exception
     */
    static boolean putObject(String bucketName, String filename, File file) throws Exception {
        return putObject(bucketName, filename, new FileInputStream(file), file.length());
    }

    static boolean putObject(String bucketName, String filename, InputStream is, long length) throws Exception {
        //服务器端生成url签名字串
        OSSClient Server = new OSSClient(OSSConstants.ENDPOINT, OSSConstants.ACCESS_KEY, OSSConstants.SECRET_KEY)
        Date expiration = new Date(new Date().getTime() + 3600 * 1000);
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, filename, HttpMethod.PUT);
        //设置过期时间
        request.setExpiration(expiration);
        //设置Content-Type
        request.setContentType("application/octet-stream");
        // 添加user meta
        request.addUserMetadata("author", "ssb");
        // 生成URL签名(HTTP PUT请求)
        URL signedUrl = Server.generatePresignedUrl(request);
        System.out.println("signed url for putObject: " + signedUrl);

        //客户端使用使用url签名字串发送请求
        OSSClient client = new OSSClient(OSSConstants.ENDPOINT, OSSConstants.ACCESS_KEY, OSSConstants.SECRET_KEY);
        // 添加PutObject请求头
        Map<String, String> customHeaders = new HashMap<String, String>();
        customHeaders.put("Content-Type", "application/octet-stream");
        // 添加user meta
        customHeaders.put("x-oss-meta-author", "ssb");
        PutObjectResult result = client.putObject(signedUrl, is, length, customHeaders);

        return result != null && StringUtils.isNotBlank(result.getETag());
    }

    static void delObject(String bucketName, List<String> keys) {
        OSSClient client = new OSSClient(OSSConstants.ENDPOINT, OSSConstants.ACCESS_KEY, OSSConstants.SECRET_KEY)
        DeleteObjectsResult deleteObjectsResult = client.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(keys))
        List<String> deletedObjects = deleteObjectsResult.getDeletedObjects()
        for (String s : deletedObjects)
            println(s)
        // 关闭client
        client.shutdown();
    }


//    static void main(String[] args) throws Exception {
//
//        Object result = putObject(OSSConstants.RES_BUCKET_NAME, "userhead/shouye1.jpg", "/Users/wangfengchen/Downloads/shouye1.jpg");
//
//        System.out.println(result);
//
//        delObject("ssb-img-debug", Arrays.asList("banner1.png"))
//    }
}
