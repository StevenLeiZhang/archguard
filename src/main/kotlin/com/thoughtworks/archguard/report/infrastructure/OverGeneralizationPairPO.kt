package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.module.ClassVO
import com.thoughtworks.archguard.report.domain.redundancy.OverGeneralizationPair

data class OverGeneralizationPairPO(val parentModuleName: String, val parentClzName: String,
                                    val childModuleName: String, val childClzName: String) {
    fun toOverGeneralizationPair(): OverGeneralizationPair {
        val parentClz = ClassVO.create(parentClzName, parentModuleName)
        val childClz = ClassVO.create(childClzName, childModuleName)
        return OverGeneralizationPair(parentClz, childClz)
    }
}