package com.wfc.web.service.impl

import com.wfc.web.config.RedisConfig
import com.wfc.web.mapper.UserMapper
import com.wfc.web.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

/**
 * Created by wangfengchen on 2016/11/21.
 */
@Service
class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper

    @Override
    @Cacheable(value = "user",keyGenerator = "defKeyGenerator")
    def getUser(Integer id) {
        println("getUser")
        userMapper.getUserById(id)
    }
}
