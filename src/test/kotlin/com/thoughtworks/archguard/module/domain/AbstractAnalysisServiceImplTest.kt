package com.thoughtworks.archguard.module.domain

import com.thoughtworks.archguard.clazz.domain.ClazzType
import com.thoughtworks.archguard.clazz.domain.JClass
import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.metrics.domain.abstracts.AbstractAnalysisServiceImpl
import com.thoughtworks.archguard.module.domain.model.LogicModule
import com.thoughtworks.archguard.module.domain.model.PackageVO
import com.thoughtworks.archguard.module.domain.model.SubModule
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class AbstractAnalysisServiceImplTest {
    @MockK
    lateinit var jClassRepository: JClassRepository

    @MockK
    lateinit var logicModuleRepository: LogicModuleRepository

    private lateinit var abstractAnalysisService: AbstractAnalysisServiceImpl

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        abstractAnalysisService = AbstractAnalysisServiceImpl(jClassRepository, logicModuleRepository)
    }

    @Test
    internal fun should_calculate_package_abstract_ratio() {
        val jClass1 = JClass("1", "pk1.name1", "module1")
        jClass1.addClassType(ClazzType.ABSTRACT_CLASS)
        val jClass2 = JClass("2", "pk1.name2", "module1")
        jClass2.addClassType(ClazzType.INTERFACE)
        val jClass3 = JClass("3", "pk1.name3", "module1")
        every { jClassRepository.getAll() } returns listOf(jClass1, jClass2, jClass3)
        val abstractRatio = abstractAnalysisService.calculatePackageAbstractRatio(PackageVO("pk1", "module1"))
        assertThat(abstractRatio.ratio).isEqualTo(2.0 / 3)
    }

    @Test
    internal fun should_calculate_module_abstract_ratio() {
        val jClass1 = JClass("1", "pk1.name1", "module1")
        jClass1.addClassType(ClazzType.ABSTRACT_CLASS)
        val jClass2 = JClass("2", "pk1.name2", "module1")
        jClass2.addClassType(ClazzType.INTERFACE)
        val jClass3 = JClass("3", "pk1.name3", "module1")
        val lg1 = LogicModule.createWithOnlyLeafMembers("id1", "lg1", listOf(SubModule("module1")))
        val lg2 = LogicModule.createWithOnlyLeafMembers("id2", "lg2", listOf(SubModule("module2")))
        every { logicModuleRepository.getAll() } returns listOf(lg1)
        every { jClassRepository.getAll() } returns listOf(jClass1, jClass2, jClass3)
        val abstractRatio = abstractAnalysisService.calculateModuleAbstractRatio(lg1)
        assertThat(abstractRatio.ratio).isEqualTo(2.0 / 3)
    }
}