package com.hardrelice.filesconnect.utils

import android.util.Log
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

object Network {
    private const val boundary = "------WebKitFormBoundaryTcWC8Wc13aJQcZ37"
    fun post(url: String, params: HashMap<String, String>){
        Thread {
            val conn: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
            try {
                val byteFormData = getByteFormData(params)
//                conn.setRequestProperty("Content-Length", byteFormData.size.toString())
                conn.setRequestProperty("Accept", "*/*")
                conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8")
                conn.setRequestProperty("Cache-Control", "no-cache")
                conn.setRequestProperty("Origin", url)
                conn.addRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")
                conn.doOutput = true
                conn.doInput = true
                conn.useCaches = false
                conn.connect()
                val outputStream = conn.outputStream
                outputStream.write(byteFormData)
                conn.inputStream.read()
            } catch (e:Exception){
                Log.e("Http Post",e.message?:"")
            } finally {
                conn.disconnect()
            }
            conn.disconnect()
        }.start()
    }

    private fun getByteFormData(params: HashMap<String, String>):ByteArray{
        val stringBuffer = StringBuffer()
        for (key in params.keys){
            stringBuffer.append(boundary)
            stringBuffer.append("\r\n")
            stringBuffer.append("Content-Disposition: form-data; name=\"$key\"")
            stringBuffer.append("\r\n")
            stringBuffer.append("\r\n")
            stringBuffer.append(params[key])
            stringBuffer.append("\r\n")
        }
        if(params.size!=0) {
            stringBuffer.append("$boundary--")
            stringBuffer.append("\r\n")
        }
        println(stringBuffer.toString().toByteArray().size)
        return stringBuffer.toString().toByteArray()
    }
}