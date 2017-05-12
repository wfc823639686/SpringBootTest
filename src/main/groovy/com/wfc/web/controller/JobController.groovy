package com.wfc.web.controller

import com.wfc.web.service.JobService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/job")
class JobController {

    @Autowired
    JobService jobService

    @RequestMapping(value = "/importJobLatest", method = RequestMethod.POST)
    Object importJobLatest() {
        return jobService.importJobLatest()
    }
}
