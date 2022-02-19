package com.thoughtworks.archguard.scanner.infrastructure.db

import com.thoughtworks.archguard.scanner.domain.scanner.git.GitHotFile
import com.thoughtworks.archguard.scanner.domain.scanner.git.GitHotFileRepo
import org.springframework.stereotype.Repository

@Repository
class GitHotFileRepoImpl(val gitHotFileDao: GitHotFileDao) : GitHotFileRepo {

    // FIXME
    override fun save(gitHotFiles: List<GitHotFile>) {
        gitHotFileDao.deleteBySystemId(gitHotFiles[0].systemId)
        gitHotFileDao.saveAll(gitHotFiles)
    }
    
    override fun findBySystemId(systemId: Long): List<GitHotFile> {
        return gitHotFileDao.findBySystemId(systemId)
    }
}