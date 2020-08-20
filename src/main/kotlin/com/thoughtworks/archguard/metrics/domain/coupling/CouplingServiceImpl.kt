package com.thoughtworks.archguard.metrics.domain.coupling

import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.clazz.exception.ClassNotFountException
import com.thoughtworks.archguard.module.domain.LogicModuleRepository
import com.thoughtworks.archguard.module.domain.dependency.DependencyService
import com.thoughtworks.archguard.module.domain.getModule
import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClassVO
import com.thoughtworks.archguard.module.domain.model.LogicModule
import com.thoughtworks.archguard.module.domain.model.PackageVO
import org.springframework.stereotype.Service

@Service
class CouplingServiceImpl(val jClassRepository: JClassRepository, val logicModuleRepository: LogicModuleRepository,
                          val dependencyService: DependencyService, val metricsRepository: MetricsRepository) : CouplingService {
    override fun persistAllClassCouplingResults() {
        val classCouplingResults = getAllClassCouplingResults()
        metricsRepository.insertAllClassCouplings(classCouplingResults)
    }

    override fun getAllClassCouplingResults(): List<ClassCoupling> {
        val classes = jClassRepository.getJClassesHasModules()
        val logicModules = logicModuleRepository.getAll()
        val classDependency = dependencyService.getAllClassDependencies()
        return classes.map { getClassCouplingWithData(it.toVO(), classDependency, logicModules) }
    }

    override fun calculateClassCoupling(jClassVO: JClassVO): ClassCoupling {
        val jClass = jClassRepository.getJClassBy(jClassVO.name, jClassVO.module)
                ?: throw ClassNotFountException("Cannot found class with module: ${jClassVO.module} and name: ${jClassVO.name}")
        val classCoupling = metricsRepository.getClassCoupling(jClass.toVO())
        if (classCoupling != null) {
            return classCoupling
        }

        val logicModules = logicModuleRepository.getAll()
        val classDependency = dependencyService.getAllClassDependencies()
        return getClassCouplingWithData(jClassVO, classDependency, logicModules)
    }

    override fun calculatePackageCoupling(packageVO: PackageVO): PackageCoupling {
        val classes = jClassRepository.getAll()
        val classesBelongToPackage = classes.filter { packageVO.containClass(it.toVO()) }
        val classCouplingsCached = metricsRepository.getClassCoupling(classesBelongToPackage.map { it.toVO() })
        if (classCouplingsCached.isNotEmpty()) {
            return PackageCoupling.of(packageVO, classCouplingsCached)
        }

        val logicModules = logicModuleRepository.getAll()
        val classDependency = dependencyService.getAllClassDependencies()
        val classCouplings = classesBelongToPackage.map { it.toVO() }.map { getClassCouplingWithData(it, classDependency, logicModules) }
        return PackageCoupling.of(packageVO, classCouplings)
    }

    override fun calculateModuleCoupling(logicModule: LogicModule): ModuleCoupling {
        val classes = jClassRepository.getAll()
        val logicModules = logicModuleRepository.getAll()
        val classesBelongToModule = classes.filter { getModule(logicModules, it.toVO()).contains(logicModule) }
        val classCouplingsCached = metricsRepository.getClassCoupling(classesBelongToModule.map { it.toVO() })
        if (classCouplingsCached.isNotEmpty()) {
            return ModuleCoupling.of(logicModule, classCouplingsCached)
        }

        val classDependency = dependencyService.getAllClassDependencies()
        val classCouplings = classesBelongToModule.map { it.toVO() }.map { getClassCouplingWithData(it, classDependency, logicModules) }
        return ModuleCoupling.of(logicModule, classCouplings)
    }

    override fun calculateAllModuleCoupling(): List<ModuleCoupling> {
        val classes = jClassRepository.getAll()
        val logicModules = logicModuleRepository.getAll()
        val moduleCouplings = mutableListOf<ModuleCoupling>()
        logicModules.parallelStream().forEach { logicModule ->
            val classesBelongToModule = classes.filter { getModule(logicModules, it.toVO()).contains(logicModule) }
            val classCouplingsCached = metricsRepository.getClassCoupling(classesBelongToModule.map { it.toVO() })
            if (classCouplingsCached.isNotEmpty()) {
                moduleCouplings.add(ModuleCoupling.of(logicModule, classCouplingsCached))
                return@forEach
            }
            val classDependency = dependencyService.getAllClassDependencies()
            val classCouplings = classesBelongToModule.map { it.toVO() }.map { getClassCouplingWithData(it, classDependency, logicModules) }
            moduleCouplings.add(ModuleCoupling.of(logicModule, classCouplings))
        }
        return moduleCouplings
    }

    fun getClassCouplingWithData(clazz: JClassVO, dependency: List<Dependency<JClassVO>>,
                                 modules: List<LogicModule>): ClassCoupling {
        val innerFanIn = dependency.filter { it.callee == clazz }.filter { isInSameModule(modules, it) }.count()
        val innerFanOut = dependency.filter { it.caller == clazz }.filter { isInSameModule(modules, it) }.count()
        val outerFanIn = dependency.filter { it.callee == clazz }.filter { !isInSameModule(modules, it) }.count()
        val outerFanOut = dependency.filter { it.caller == clazz }.filter { !isInSameModule(modules, it) }.count()
        return ClassCoupling(clazz, innerFanIn, innerFanOut, outerFanIn, outerFanOut)
    }

    private fun isInSameModule(modules: List<LogicModule>, it: Dependency<JClassVO>): Boolean {
        val callerModules = getModule(modules, it.caller)
        val calleeModules = getModule(modules, it.callee)
        return callerModules.intersect(calleeModules).isNotEmpty()
    }
}