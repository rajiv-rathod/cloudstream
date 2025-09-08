package com.lagradost.cloudstream3.desktop.utils

import java.util.prefs.Preferences

object DesktopPreferences {
    private lateinit var prefs: Preferences
    
    fun init() {
        prefs = Preferences.userRoot().node("cloudstream-desktop")
    }
    
    fun getString(key: String, defaultValue: String = ""): String {
        return prefs.get(key, defaultValue)
    }
    
    fun putString(key: String, value: String) {
        prefs.put(key, value)
    }
    
    fun getInt(key: String, defaultValue: Int = 0): Int {
        return prefs.getInt(key, defaultValue)
    }
    
    fun putInt(key: String, value: Int) {
        prefs.putInt(key, value)
    }
    
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return prefs.getBoolean(key, defaultValue)
    }
    
    fun putBoolean(key: String, value: Boolean) {
        prefs.putBoolean(key, value)
    }
    
    fun remove(key: String) {
        prefs.remove(key)
    }
    
    fun clear() {
        prefs.clear()
    }
}