package com.wfc.web.mapper

import com.wfc.web.model.EnterprisePhoto
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper

@Mapper
interface EnterprisePhotoMapper {

    @Insert('''
        INSERT INTO enterprise_photo
                (enterprise_id, photo, thumb, pos)
                VALUES (#{enterpriseId}, #{photo}, #{thumb}, #{pos})''')
    int insert(EnterprisePhoto ep)
}
