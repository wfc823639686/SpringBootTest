package com.wfc.web.mapper

import com.wfc.web.model.Enterprise
import com.wfc.web.model.EnterpriseVideo
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

/**
 * Created by wangfengchen on 2017/3/27.
 */
@Mapper
interface EnterpriseVideoMapper {

    @Insert('''
        INSERT INTO enterprise_video
                (enterprise_id, times, video, photo, status)
                VALUES (#{enterpriseId}, #{times}, #{video}, #{photo}, #{status})''')
    int insert(EnterpriseVideo ev);
}
