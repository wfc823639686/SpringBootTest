package com.wfc.web.controller

import com.wfc.web.common.utils.EncryptUtils
import com.wfc.web.service.CommonService
import com.wfc.web.service.EnterpriseService
import com.wfc.web.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * Created by wangfengchen on 2016/11/21.
 */

@RestController
@RequestMapping("/enterprise")
class EnterpriseController {

    @Autowired
    EnterpriseService enterpriseService


    @RequestMapping("/insertVideos")
    Object insertVideos() {
        enterpriseService.insertVideos()
        "success"
    }

    @RequestMapping("/updPhotos")
    Object updPhotos() {
        enterpriseService.updPhotos()
        "success"
    }

    @RequestMapping("/listPhotoDir2File")
    Object listPhotoDir2File() {
        enterpriseService.listPhotoDir2File()
        "success"
    }

    @RequestMapping(value = "/fastUpdPhotos", method = RequestMethod.POST)
    Object fastUpdPhotos() {
        enterpriseService.fastUpdPhotos()
        "success"
    }

    @RequestMapping("/updVideos")
    Object updVideos() {
        enterpriseService.updVideos()
        "success"
    }

    @RequestMapping("/listVideoDir2File")
    Object listVideoDir2File() {
        enterpriseService.listVideoDir2File()
        "success"
    }

    @RequestMapping("/updateLocation")
    Object updateLocation() {
        enterpriseService.updateLocation()
        "success"
    }

    @RequestMapping("/delPhoto")
    Object delPhoto() {
        enterpriseService.delPhoto()
        "success"
    }

}
