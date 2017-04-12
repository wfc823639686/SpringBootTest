package com.wfc.web.service.impl

import com.wfc.web.mapper.JobMapper
import com.wfc.web.model.Job
import com.wfc.web.model.QueryJobsModel
import com.wfc.web.service.JobService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by wangfengchen on 2016/11/24.
 */
@Service
class JobServiceImpl implements JobService {

    @Autowired
    JobMapper jobMapper

    @Override
    List<Job> getJobs(QueryJobsModel q) {
        return jobMapper.getJobs([
                "lng": q.lng,
                "lat": q.lat,
                "last": q.last,
                "experience": q.experience,
                "scale": q.scale,
                "offset": 11
        ])
    }
}
