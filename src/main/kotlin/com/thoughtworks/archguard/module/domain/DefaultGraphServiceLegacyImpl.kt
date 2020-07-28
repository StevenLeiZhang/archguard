package com.thoughtworks.archguard.module.domain

import com.thoughtworks.archguard.module.domain.dependency.DependencyService
import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClassVO
import com.thoughtworks.archguard.module.domain.model.LogicModule
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
@Qualifier("Default")
class DefaultGraphServiceLegacyImpl(logicModuleRepository: LogicModuleRepository, dependencyService: DependencyService) : DefaultGraphServiceLegacy(logicModuleRepository, dependencyService) {

    private val log = LoggerFactory.getLogger(DefaultGraphServiceLegacyImpl::class.java)
    override fun mapClassDependencyToModuleDependency(logicModules: List<LogicModule>, jClassDependency: Dependency<JClassVO>): List<Dependency<LogicModule>> {
        val callerModules = getModule(logicModules, jClassDependency.caller)
        if (callerModules.size > 1) {
            log.error("Caller Class belong to more than one Module!", callerModules)
        }
        val callerModule = callerModules[0]
        val calleeModules = getModule(logicModules, jClassDependency.callee)

        return calleeModules.map { Dependency(callerModule, it) }
    }
}