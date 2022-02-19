package com.thoughtworks.archgard.scanner.domain.tools

import com.thoughtworks.archgard.scanner.infrastructure.FileOperator
import com.thoughtworks.archgard.scanner.infrastructure.Processor
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class DesigniteJavaTool(val systemRoot: File) {
    private val log = LoggerFactory.getLogger(DesigniteJavaTool::class.java)
    private val host = "ec2-68-79-38-105.cn-northwest-1.compute.amazonaws.com.cn:8080"

    fun readReport(designiteJavaReportType: DesigniteJavaReportType): List<String> {
        prepareTool()
        return getTargetScannedDirections(systemRoot)
                .map { generateReport(designiteJavaReportType, it)?.readLines() }
                .filterNotNull()
                .flatten()
                .filter { !it.contains("Project Name") }
    }

    private fun generateReport(type: DesigniteJavaReportType, currentScannedDirection: String): File? {
        process(currentScannedDirection)
        val report = File(currentScannedDirection + "/DesigniteReport/${type.reportName}")
        return if (report.exists()) report else null
    }

    private fun process(currentScannedDirection: String) {
        scan(listOf("java", "-jar", "-Xmx1G", "${systemRoot.absolutePath}/DesigniteJava.jar", "-i", currentScannedDirection, "-o", "$currentScannedDirection/DesigniteReport"))
    }

    private fun prepareTool() {
        val file = File(systemRoot.toString() + "/DesigniteJava.jar")
        when {
            file.exists() -> {
                log.info("DesigniteJava.jar already exists in systemRoot")
                Files.copy(file.toPath(),
                        File(systemRoot.toString() + "/DesigniteJava.jar").toPath(),
                        StandardCopyOption.REPLACE_EXISTING)
            }
            checkIfExistInLocal() -> {
                log.info("DesigniteJava.jar exists in local")
                Files.copy(File("DesigniteJava.jar").toPath(),
                        File(systemRoot.toString() + "/DesigniteJava.jar").toPath(),
                        StandardCopyOption.REPLACE_EXISTING)
            }
            else -> {
                log.info("Download DesigniteJava.jar from remote")
                val downloadUrl = "http://$host/job/DesigniteJava/lastSuccessfulBuild/artifact/target/DesigniteJava.jar"
                FileOperator.download(URL(downloadUrl), File(systemRoot.toString() + "/DesigniteJava.jar"))
            }
        }

        val chmod = ProcessBuilder("chmod", "+x", "DesigniteJava.jar")
        chmod.directory(systemRoot)
        chmod.start().waitFor()
    }

    private fun checkIfExistInLocal(): Boolean {
        return File("DesigniteJava.jar").exists()
    }

    private fun scan(cmd: List<String>) {
        Processor.executeWithLogs(ProcessBuilder(cmd), systemRoot)
    }

    private fun getTargetScannedDirections(workspace: File): List<String> {
        val target = workspace.walkTopDown()
                .filter { f -> checkIsSubModulePath(f) }
                .map { f -> f.absolutePath }
                .distinct()
                .toList()
        return if (target.size > 1) {
            target.filter { it != workspace.absolutePath }
        } else {
            target
        }
    }

    private fun checkIsSubModulePath(f: File): Boolean {
        val list = f.list() ?: return false
        if (list.contains("src") && list.any { it.contains("pom.xml") || it.contains("build.gradle") }) {
            return true
        }
        return false
    }

}

enum class DesigniteJavaReportType(val reportName: String) {
    TYPE_METRICS("typeMetrics.csv"), METHOD_METRICS("methodMetrics.csv"), BAD_SMELL_METRICS("designCodeSmells.csv")
}