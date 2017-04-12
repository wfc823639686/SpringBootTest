package com.wfc.web.mapper

import com.wfc.web.model.Enterprise
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

/**
 * Created by wangfengchen on 2017/3/27.
 */
@Mapper
interface EnterpriseMapper {

    @Select("select * from enterprise where id=#{0}")
    Enterprise getInfo(Integer id);

    @Select("select id, lng, lat from enterprise limit 10")
    List<Enterprise> getEnterprises();

    @Update("update enterprise set video_count=1 where id=#{0}")
    int updateVideoCount(Integer id);
}
