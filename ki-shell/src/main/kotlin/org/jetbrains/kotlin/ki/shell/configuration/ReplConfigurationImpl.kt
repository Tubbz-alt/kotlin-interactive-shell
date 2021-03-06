package org.jetbrains.kotlin.ki.shell.configuration

import org.jetbrains.kotlin.ki.shell.plugins.*
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.*

open class ReplConfigurationImpl(extraPlugins: List<String> = listOf())
    : PropertyBasedReplConfiguration(
        Properties(),
        listOf(LoadFilePlugin::class.qualifiedName!!,
                RuntimePlugin::class.qualifiedName!!,
                HelpPlugin::class.qualifiedName!!,
                PastePlugin::class.qualifiedName!!,
                SyntaxPlugin::class.qualifiedName!!,
                PromptPlugin::class.qualifiedName!!,
                ConfigPlugin::class.qualifiedName!!,
                DependenciesPlugin::class.qualifiedName!!,
                ExecutionEnvironmentPlugin::class.qualifiedName!!
        ) + extraPlugins
) {
    override fun load() {
        val path = configPath()

        if (File(path).exists()) {
            props.load(BufferedReader(FileReader(path)))
        }

        super.load()
    }

    private fun configPath() =
            System.getProperty("config.path") ?:
            System.getenv("KI_CONFIG") ?:
            (System.getProperty("user.home") ?: "") + File.separator + ".ki-shell"
}