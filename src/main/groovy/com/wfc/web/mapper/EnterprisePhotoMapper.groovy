package com.wfc.web.mapper

import com.wfc.web.model.EnterprisePhoto
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Result
import org.apache.ibatis.annotations.Results
import org.apache.ibatis.annotations.Select

@Mapper
interface EnterprisePhotoMapper {

    @Insert('''
        INSERT INTO enterprise_photo
                (enterprise_id, photo, thumb, pos)
                VALUES (#{enterpriseId}, #{photo}, #{thumb}, #{pos})''')
    int insert(EnterprisePhoto ep)

    @Select("select * from enterprise_photo where enterprise_id=#{0}")
    List<EnterprisePhoto> getPhotos(Integer eid)

    @Results([
            @Result(column = "enterprise_id", property = "enterpriseId")
    ])
    @Select("select * from enterprise_photo")
    List<EnterprisePhoto> getAll()

    @Delete("delete from enterprise_photo where id=#{0}")
    int delete(int id)
}
