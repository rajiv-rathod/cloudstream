package com.lagradost.cloudstream3.desktop.utils

import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Desktop equivalent of Android Context for file operations and app data
 */
object DesktopContext {
    private val userHome: Path = Paths.get(System.getProperty("user.home"))
    private val appDataDir: Path = userHome.resolve(".cloudstream")
    
    init {
        // Create app data directory if it doesn't exist
        appDataDir.toFile().mkdirs()
    }
    
    fun getAppDataDir(): File = appDataDir.toFile()
    
    fun getCacheDir(): File = appDataDir.resolve("cache").toFile().also { it.mkdirs() }
    
    fun getFilesDir(): File = appDataDir.resolve("files").toFile().also { it.mkdirs() }
    
    fun getDatabaseDir(): File = appDataDir.resolve("databases").toFile().also { it.mkdirs() }
    
    fun getConfigDir(): File = appDataDir.resolve("config").toFile().also { it.mkdirs() }
    
    fun getDownloadsDir(): File = userHome.resolve("Downloads").toFile()
    
    fun openFile(file: File) {
        try {
            val os = System.getProperty("os.name").lowercase()
            when {
                os.contains("win") -> {
                    Runtime.getRuntime().exec("cmd /c start \"\" \"${file.absolutePath}\"")
                }
                os.contains("mac") -> {
                    Runtime.getRuntime().exec("open \"${file.absolutePath}\"")
                }
                else -> {
                    Runtime.getRuntime().exec("xdg-open \"${file.absolutePath}\"")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}