package com.thoughtworks.archgard.scanner.infrastructure.db

import com.thoughtworks.archgard.scanner.domain.scanner.statistic.ClassStatistic
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.SqlBatch
import org.jdbi.v3.sqlobject.statement.SqlUpdate

interface ClassStatisticDao {
    @SqlBatch("insert into ClassStatistic (id, projectName, packageName, typeName, `lines`, fanin, fanout, updateAt, createAt) " +
            "values (:classStatistic.id, :classStatistic.projectName, :classStatistic.packageName, :classStatistic.typeName," +
            " :classStatistic.lines, :classStatistic.fanIn, :classStatistic.fanOut, NOW(), NOW())")
    fun saveAll(@BindBean("classStatistic") classStatistic: List<ClassStatistic>)

    @SqlUpdate("delete from ClassStatistic where 1=1 ")
    fun deleteAll()

}