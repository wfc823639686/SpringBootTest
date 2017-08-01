package com.wfc.web.mapper

import com.wfc.web.model.EnterpriseCommodity
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Result
import org.apache.ibatis.annotations.Results
import org.apache.ibatis.annotations.Select

/**
 * Created by wangfengchen on 2017/3/27.
 */
@Mapper
interface EnterpriseCommodityMapper {

    @Results([
        @Result(column = "enterprise_id", property = "enterpriseId")
    ])
    @Select('''
        SELECT ec.id, ec.commodity_id commodityId,  co.name, co.index
        FROM `enterprise_commodity` ec
        join `commodity` co
        on co.id = ec.commodity_id
        WHERE enterprise_id=#{0}
    ''')
    List<EnterpriseCommodity> getCommodities(Integer id)

    List getLessThan6()
}