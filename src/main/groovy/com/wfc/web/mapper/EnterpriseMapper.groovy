package com.wfc.web.mapper

import com.wfc.web.model.Enterprise
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Result
import org.apache.ibatis.annotations.Results
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

/**
 * Created by wangfengchen on 2017/3/27.
 */
@Mapper
interface EnterpriseMapper {

    @Select("select * from enterprise where id=#{0}")
    Enterprise getInfo(Integer id)

    @Select("select id, lng, lat from enterprise limit 10")
    List<Enterprise> getEnterprises()

    @Update("update enterprise set video_count=1 where id=#{0}")
    int updateVideoCount(Integer id)

    @Select("select id from enterprise where short_name=#{0}")
    List<Integer> getIdsByShortName(String sn)

    @Update("update enterprise set lng=#{lng}, lat=#{lat} where id=#{id}")
    int updateLocation(Enterprise e)

    @Update("update user set head=#{head} where id=#{id}")
    int updateLogo(Enterprise e)

    @Results([
        @Result(column = "full_name", property = "fullName")
    ])
    @Select("""select * from (
            select full_name, count(id) as c from enterprise where full_name is not null group by full_name
            ) as a where c > 1""")
    List<Enterprise> getRepeats()

    @Results([
        @Result(column = "full_name", property = "fullName")
    ])
    @Select("select id, full_name from enterprise where full_name=#{0}")
    List<Enterprise> getEnterprisesByFullName(String fullName)

}
