package com.wfc.web.service

import com.wfc.web.model.Job
import com.wfc.web.model.QueryJobsModel

/**
 * Created by wangfengchen on 2017/2/23.
 */
interface JobService {

    List<Job> getJobs(QueryJobsModel q);
}