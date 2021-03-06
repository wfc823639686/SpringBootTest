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

    static def PF_NAME = "/Users/wangfengchen/uploads/pf.txt"

    /**
     * 发生错误写入该文件
     */
    static def EPF_NAME = "/Users/wangfengchen/uploads/epf.txt"

    static def VF_NAME = "/Users/wangfengchen/uploads/vf.txt"

    static Random RANDOM = new Random()

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

    @Deprecated
    private static String updPhoto(File file, int id) {
        if (file.name.toLowerCase().endsWith('.png')
                || file.name.toLowerCase().endsWith('.jpg')) {
            String time = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss")
            String name = "Image_${id}_${time}.jpg"
            println(name)
            def r = OSSApi.putObject(OSSConstants.IMG_BUCKET_NAME, name, file)
            if (r)
                return "http://${OSSConstants.IMG_BUCKET_NAME}.shangshaban.com/" + name
        }
        return null
    }

    @Deprecated
    static def updPhotos(callback) {
        File root = new File(PHOTO_ROOT)
        if (root.isDirectory()) {
            File[] updDirs = root.listFiles()
            if (updDirs != null && updDirs.length > 0) {
                for (File updDir : updDirs) {
                    File[] cs = updDir.listFiles()
                    if (updDir.isDirectory()
                            && cs != null
                            && cs.length != 0) {
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

    private static String updPhoto(File file) {
        String time = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss")
        int rad = RANDOM.nextInt()
        String name = "Image_r${rad}_${time}.jpg"
        println(name)
        def r = OSSApi.putObject(OSSConstants.IMG_BUCKET_NAME, name, file)
        if (r)
            return "http://${OSSConstants.IMG_BUCKET_NAME}.shangshaban.com/" + name
        return null
    }

    static def listPhotoDir(callback) {
        File root = new File(PHOTO_ROOT)
        if (root.isDirectory()) {
            File[] updDirs = root.listFiles()//每个企业的相册目录
            if (updDirs != null && updDirs.length > 0) {
                for (File updDir : updDirs) {
                    File[] cs = updDir.listFiles()
                    if (updDir.isDirectory()
                            && cs != null
                            && cs.length != 0) {
                        def photoList = []
                        for (File c : cs) {
                            if (c.name.toLowerCase().endsWith('.png')
                                    || c.name.toLowerCase().endsWith('.jpg')) {
                                String p = updPhoto(c)
                                if (p != null)
                                    photoList.add(p)
                            }
                        }
                        callback.call(updDir.name, photoList)
                    }
                }
            }
        }
    }

    private static def updVideo(File file) {
        String time = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss")
        int rad = RANDOM.nextInt()
        String name = "Video_r${rad}_${time}.mp4"
        println(name)
        def r = OSSApi.putObject(OSSConstants.VIDEO_BUCKET_NAME, name, file)
        if (r)
            return "http://${OSSConstants.VIDEO_BUCKET_NAME}.shangshaban.com/" + name
        return null
    }

    private static String[] updVideoAndPhoto(File dir) {
        String[] results = new String[2]
        if (dir.isDirectory()) {
            File[] updDirs = dir.listFiles()
            if(updDirs != null && updDirs.length > 0) {
                for (File file : updDirs) {
                    if (file.name.toLowerCase().endsWith('.mp4')) {
                        results[0] = updVideo(file)
                    } else if (file.name.toLowerCase().endsWith('.jpg')
                            || file.name.toLowerCase().endsWith('.png')) {
                        results[1] = updPhoto(file)
                    }
                }
            }
        }
        return results
    }

    static def listVideoDir(callback) {
        File root = new File(VIDEO_ROOT)
        if (root.isDirectory()) {
            File[] updDirs = root.listFiles()//每个企业的相册目录
            if (updDirs != null && updDirs.length > 0) {
                for (File updDir : updDirs) {
                    String[] ps = updVideoAndPhoto(updDir)
                    callback.call(updDir.name, ps)
                }
            }
        }
    }
}
