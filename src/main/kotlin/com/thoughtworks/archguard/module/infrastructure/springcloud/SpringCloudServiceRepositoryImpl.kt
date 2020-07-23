package com.thoughtworks.archguard.module.infrastructure.springcloud

import com.thoughtworks.archguard.module.domain.model.JMethodVO
import com.thoughtworks.archguard.module.domain.springcloud.SpringCloudServiceRepository
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class SpringCloudServiceRepositoryImpl : SpringCloudServiceRepository {
    @Autowired
    lateinit var jdbi: Jdbi

    override fun getMethodIdsByClassId(classId: String): List<String> {
        val sql = "select b from _ClassMethods where a='$classId'"
        return jdbi.withHandle<List<String>, Nothing> {
            it.createQuery(sql)
                    .mapTo(String::class.java)
                    .list()
        }
    }

    override fun getServiceNameByMethodId(methodId: String): String {
        val sql = "select module from JMethod where id = '$methodId'"
        return jdbi.withHandle<String, Nothing> {
            it.createQuery(sql)
                    .mapTo(String::class.java)
                    .one()
        }
    }

    override fun getMethodById(methodId: String): JMethodVO {
        val sql = "select module as moduleName, clzname as className, name from JMethod where id = '$methodId'"

        return jdbi.withHandle<JMethodVO, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JMethodVO::class.java))
            it.createQuery(sql)
                    .mapTo(JMethodVO::class.java)
                    .first()
        }
    }


}
