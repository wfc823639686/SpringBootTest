package com.wfc.web.mapper

import com.wfc.web.mapper.JobMapper.JobSqlBuilder
import com.wfc.web.model.EnterpriseVideo
import com.wfc.web.model.Job
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Many
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Result
import org.apache.ibatis.annotations.ResultMap
import org.apache.ibatis.annotations.Results
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.SelectProvider
import org.apache.ibatis.jdbc.SQL

/**
 * Created by wangfengchen on 2017/3/27.
 */
@Mapper
interface JobMapper {

    @Insert('''
        INSERT INTO enterprise_video
                (enterprise_id, times, video, photo, status)
                VALUES (#{enterpriseId}, #{times}, #{video}, #{photo}, #{status})''')
    int insert(EnterpriseVideo ev);

    @Results(id = "joblist", value = [
            @Result(column = "enterpriseId", property = "enterprise.id"),
            @Result(column = "enterpriseName", property = "enterprise.name"),
            @Result(column = "enterpriseShortName", property = "enterprise.shortName"),
            @Result(column = "enterpriseHead", property = "enterprise.head"),
            @Result(column = "enterpriseScore", property = "enterprise.score"),
            @Result(column = "hiring_last_login_time", property = "enterprise.hiringLastLoginTime"),
            @Result(column = "enterprise_position", property = "enterprise.enterprisePosition"),
            @Result(column = "enterpriseVideoCount", property = "enterprise.videoCount"),
            @Result(column = "enterpriseScale", property = "enterprise.scale"),
            @Result(column = "enterpriseAuth", property = "enterprise.auth"),
            @Result(column = "enterpriseVideoCount", property = "enterprise.videoCount"),
            @Result(column = "enterprise_id", property = "enterprise.commodities",
                    many = @Many(select = "com.wfc.web.mapper.EnterpriseCommodityMapper.getCommodities"))
    ])
    @SelectProvider(type = JobSqlBuilder.class, method = "buildGetJobs")
    List<Job> getJobs(Map map)

    @Results(
            value = [
                    @Result(column = "job_name", property = "jobName")
            ])
    @Select("select job_name, position, position1 from job where id=#{0}")
    Job getInfo(Integer id)

    class JobSqlBuilder {

        String buildGetJobs(Map map) {
            String innerSql = new SQL() {
                {
                    SELECT("j.id, j.enterprise_id, e.video_count")
                    if (map.get("lng") != null)
                        SELECT("SQRT(POW( ABS( e.lng - #{lng}), 2) + POW( ABS(e.lat - #{lat}), 2 )) AS distance")
                    FROM("job as j, enterprise as e")
                    WHERE("j.enterprise_id=e.id and j.status=1 and e.status=1")
                    if (map.get("lng") != null)
                        WHERE("SQRT(POW( ABS( e.lng - #{lng}), 2) + POW( ABS(e.lat - #{lat}), 2 )) < 30")
                    if (map.get("workProvince") != null)
                        WHERE("j.work_province=#{workProvince}")
                    if (map.get("workCity") != null)
                        WHERE("j.work_city=#{workCity}")
                    if (map.get("workDistrict") != null)
                        WHERE("j.work_district=#{workDistrict}")
                    if (map.get("experience") != null)
                        WHERE("j.exp=#{experience}")
                    if (map.get("industry") != null)
                        WHERE("e.industry=#{industry}")
                    if (map.get("scale") != null)
                        WHERE("e.scale=#{scale}")
                    if (map.get("orderBy") != null) {
                        int orderBy = (int) map.get("orderBy")
                        switch (orderBy) {
                            case 1:
                                ORDER_BY("salary_highest DESC, j.update_time DESC")
                                if (map.get("lng") != null)
                                    ORDER_BY("distance ASC")
                                break
                            case 2:
                                if (map.get("lng") != null)
                                    ORDER_BY("distance ASC")
                                ORDER_BY("j.update_time DESC")
                                break
                        }
                    }
                }
            }

            String rowSql = new SQL() {
                {
                    SELECT("@x:=@x+1 as rownum, a.*")
                    if (map.get("last") != null)
                        SELECT("if(id=#{last}, @y:=@x,0)")
                    FROM("(" + innerSql + ") as a, (SELECT @x:=0) as it")

                }
            }
            if (map.get("offset") != null)
                rowSql += " LIMIT #{offset}"

            String rowSql1 = new SQL() {
                {
                    SELECT("*")
                    FROM("(" + rowSql + ") as b")
                    if (map.get("last") != null)
                        WHERE("b.rownum>@y")
                }
            }
            
            String otherSql = new SQL() {
                {
                    SELECT("c.id, c.enterprise_id, d.job_name, d.position, d.position1, d.temptation, d.salary_minimum, d.salary_highest," +
                            "        d.exp, d.degree," +
                            "        (select name from district where id=d.work_district) workDistrictStr," +
                            "        d.status, d.update_time, d.create_time, u.head as enterpriseHead, u.name as enterpriseName," +
                            "        e.short_name as enterpriseShortName, e.scale enterpriseScale, e.auth enterpriseAuth, e.video_count" +
                            "        enterpriseVideoCount," +
                            "        u.hiring_last_login_time, u.enterprise_position")
                    if (map.get("lng") != null)
                        SELECT("c.distance")
                    FROM("job d ,enterprise e, `user` u," +
                            "        (" + rowSql1 + ") as c")
                    WHERE("c.id=d.id and c.enterprise_id=e.id and e.id=u.id")
                    ORDER_BY("rownum")
                }
            }
            println(otherSql)
            return otherSql
        }

    }
}
