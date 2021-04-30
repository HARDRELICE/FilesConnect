package com.hardrelice.filesconnect

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.util.Log

object Settings {
    lateinit var sharedPreferences: SharedPreferences

    private const val settingName = "Globals"

    private val defaultSettings = mapOf(
        "exist" to true,
        "url" to "http://192.168.0.100:8081/apps/index.html",
        "server" to "http://192.168.0.100:9102/"
        // 自定义以put(键值,参数)的方式加入sharedPreferences
        // eg: sharedPreferences.putInt("魔法少女的低吟", 0)
    )

    fun init(context: Application) {
        sharedPreferences = context.getSharedPreferences(settingName, Activity.MODE_PRIVATE)
        if (!sharedPreferences.getBoolean("exist", false)) {
            println("False")
            applyDefaultSettings()
            println(sharedPreferences.getString("用户名", "失败了"))
//            sharedPreferences.edit()
//                .putString("用户名","hardrelice")
//                .apply()
        }
    }

    fun edit(key: String, value: Any) {
        try {
            val editor = sharedPreferences.edit()
            when (value.javaClass.canonicalName) {
                java.lang.String::class.java.canonicalName -> {
                    editor.putString(key, value as String)
                }
                java.lang.Boolean::class.java.canonicalName -> {
                    editor.putBoolean(key, value as Boolean)
                }
                java.lang.Float::class.java.canonicalName -> {
                    editor.putFloat(key, value as Float)
                }
                java.lang.Integer::class.java.canonicalName -> {
                    editor.putInt(key, value as Int)
                }
                java.lang.Long::class.java.canonicalName -> {
                    editor.putLong(key, value as Long)
                }
                "java.util.Collections.SingletonSet" -> {
                    editor.putStringSet(key, value as Set<String>)
                }
            }
            editor.apply()
            editor.commit()
        } catch (e: Exception) {
            e.message?.let { Log.e("Setting Edit", it) }
        }
    }

    fun edit(map: Map<String, Any>) {
        for (key in map.keys) {
            map[key]?.let { edit(key, it) }
        }
    }

    fun getString(key: String, default: String = ""): String {
        return sharedPreferences.getString(key, default).toString()
    }

    private fun applyDefaultSettings() {
        edit(defaultSettings)
    }
}