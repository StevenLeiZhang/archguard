package com.thoughtworks.archgard.scanner.domain.scanner

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.config.model.ToolConfigure
import java.util.*

interface Scanner {
    val toolList: List<ToolConfigure>
        get() = toolListGenerator()

    fun getScannerName(): String

    fun toolListGenerator(): List<ToolConfigure> {
        val result = ArrayList<ToolConfigure>()
        val config = HashMap<String, String>()
        config["available"] = "false"
        result.add(ToolConfigure(getScannerName(), config))
        return result
    }

    fun scan(context: ScanContext)

    fun canScan(context: ScanContext): Boolean {
        return false
    }
}