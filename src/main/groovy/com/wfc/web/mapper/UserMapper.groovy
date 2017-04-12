package com.wfc.web.mapper

import com.wfc.web.model.User
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Result
import org.apache.ibatis.annotations.ResultMap
import org.apache.ibatis.annotations.Results
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.SelectProvider
import org.apache.ibatis.annotations.Update
import org.apache.ibatis.annotations.UpdateProvider
import org.apache.ibatis.jdbc.SQL

/**
 * Created by wangfengchen on 2017/3/25.
 */
@Mapper
interface UserMapper {

    @Results(id = "userResult", value = [
            @Result(property = "id", column = "id", id = true),
            @Result(property = "enterprisePosition", column = "enterprise_position")
    ])
    @Select("select * from user where id = #{id}")
    User getUserById(Integer id);

    @ResultMap("userResult")
    @SelectProvider(type = UserSqlBuilder.class, method = "buildGetUsersByName")
    List<User> getUsersByName(
            @Param("name") String name,
            @Param("phone") String phone);

    @UpdateProvider(type = UserSqlBuilder.class, method = "buildUpdate")
    int update(User user);

    class UserSqlBuilder {

        String buildGetUsersByName(Map<String, Object> map) {
            String sql = new SQL() {
                {
                    SELECT("*")
                    FROM("user")
                    if (map.get("name") != null) {
                        WHERE("name=#{name}")
                    }
                    if (map.get("phone") != null) {
                        WHERE("phone=#{phone}")
                    }

                }
            }.toString()
            println(sql)
            return sql
        }

        String buildUpdate(User user) {
            String sql = new SQL() {
                {
                    //构造块
                    UPDATE("user")
                    if (user.name != null)
                        SET("name=#{name}")
                    if (user.phone != null)
                        SET("phone=#{phone}")
                    WHERE("id=#{id}")
                }
            }.toString()
            println(sql)
            return sql
        }
    }

}
