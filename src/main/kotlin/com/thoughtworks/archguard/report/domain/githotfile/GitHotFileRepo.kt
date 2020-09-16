package com.thoughtworks.archguard.report.domain.githotfile

interface GitHotFileRepo {
    fun findBySystemId(systemId: Long) : List<GitHotFile>
}