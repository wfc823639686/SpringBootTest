package com.wfc.web.mapper

import com.wfc.web.model.EnterprisePhoto
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface EnterprisePhoto1Mapper {

    @Select('select * from enterprise_photo_tem limit #{0}, #{1}')
    List<EnterprisePhoto> getEnterprisePhotos(int start, int offset)

    @Select('select count(id) from enterprise_photo_tem')
    int enterprisePhotoCount()
}
