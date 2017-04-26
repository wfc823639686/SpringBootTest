package com.wfc.web.common.utils

import com.wfc.web.common.aliyun.OSSApi
import com.wfc.web.common.aliyun.OSSConstants
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.DateFormatUtils

/**
 * Created by wangfengchen on 2016/11/24.
 */
class UploadUtils {

    static def VIDEO_ROOT = "/Users/wangfengchen/uploads/videos/"

    static def IMAGE_ROOT = "/Users/wangfengchen/uploads/images/"

    static def PHOTO_ROOT = "/Users/wangfengchen/uploads/photos/"

    static def uploadVideos(callback) {
        File root = new File(VIDEO_ROOT)
        if (root.isDirectory()) {
            File[] updDirs = root.listFiles()
            if (updDirs != null && updDirs.length > 0) {
                for (File updDir : updDirs) {
                    uploadVideo(updDir, callback)
                }
            }
        }
    }

    private static def uploadVideo(File updDir, callback) {
        def id, times

        if (updDir != null && updDir.isDirectory()) {
            def ss = updDir.name.split(' ')
            if (ss == null || ss.length != 2) {
                println("名字错误 " + updDir.name)
                return
            }
            id = ss[0]
            times = ss[1]
            if (!StringUtils.isNumeric(id)) {
                println("不是目标目录")
                return
            }

            File[] fs = updDir.listFiles()
            if (fs != null && fs.length > 1) {
                boolean imageRes, videoRes;
                for (File f : fs) {
                    if (f.name.contains(".jpg")) {
                        imageRes = OSSApi.putObject(OSSConstants.IMG_BUCKET_NAME, "Image_" + id + "_20160901113830.jpg", f);
                    } else if (f.name.contains(".mp4")) {
                        videoRes = OSSApi.putObject(OSSConstants.VIDEO_BUCKET_NAME, "Video_" + id + "_20160909100117.mp4", f);
                    }
                }
                if (imageRes && videoRes) {
                    println("上传成功")
                    int i = 0, t = 0
                    if (StringUtils.isNumeric(id))
                        i = Integer.parseInt(id)
                    if (StringUtils.isNumeric(times))
                        t = Integer.parseInt(times)
                    callback.call(i, t)
                }
            } else {
                println("文件少于2个")
            }
        }
    }

    static def uploadImages(callback) {
        File root = new File(IMAGE_ROOT)
        root.writable = true
        File[] updDirs = root.listFiles()
        root.setReadable(true, true)
        println(root)
        println(root.canWrite())
        println(root.canRead())
        if (updDirs != null && updDirs.length > 0) {
            println(updDirs.length)
            for (File imgFile : updDirs) {
                uploadImage(imgFile, callback)
            }
        }
    }

    private static def uploadImage(File imgFile, callback) {
        if (imgFile.name.contains(".jpg")) {
            def id = imgFile.name.replace(".jpg", '')
            def r = OSSApi.putObject(OSSConstants.IMG_BUCKET_NAME, "Image_" + id + "_20161124113830.jpg", imgFile);
            if (r) {
                callback.call(id)
            }
        }
    }

    private static String updPhoto(File file, int id) {
        if (file.name.toLowerCase().endsWith('.png')
                || file.name.toLowerCase().endsWith('.jpg')) {
            String time = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss")
            String name = "Image_${id}_${time}.jpg"
            println(name)
            def r = OSSApi.putObject(OSSConstants.IMG_BUCKET_NAME, name, file)
            if (r)
                return "http://ssb-img.shangshaban.com/" + name
        }
        return null
    }

    static String updPhotos(callback) {
        File root = new File(PHOTO_ROOT)
        if (root.isDirectory()) {
            File[] updDirs = root.listFiles()
            if (updDirs != null && updDirs.length > 0) {
                for (File updDir : updDirs) {
                    File[] cs = updDir.listFiles()
                    if (updDir.isDirectory()
                            && cs!=null
                            && cs.length!=0) {
                        String[] ss = updDir.name.split(',')
                        int eid = Integer.parseInt(ss[0])
                        def list = []
                        for (File c : cs) {
                            String d = updPhoto(c, eid)
                            println(d)
                            list.add(d)
                        }
                        callback.call(ss, list)
                    }
                }
            }
        }
    }
}
