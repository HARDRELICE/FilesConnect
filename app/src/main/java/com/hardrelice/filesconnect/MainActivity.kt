package com.hardrelice.filesconnect

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Website.URL
import android.util.Log
import android.view.KeyEvent
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.hardrelice.filesconnect.clients.FCChromeClient
import com.hardrelice.filesconnect.databinding.ActivityMainBinding
import com.hardrelice.filesconnect.utils.Network
import com.hardrelice.filesconnect.utils.UriUtil.getPath
import java.io.File
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {
    companion object{
        const val FILE_CHOOSE = 1
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var webView: WebView
    private lateinit var valueCallback: ValueCallback<String>

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        valueCallback = ValueCallback {  }
        Settings.init(this.application)
        webView = binding.webView
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = FCChromeClient(this)
        webView.settings.javaScriptEnabled = true
        webView.settings.allowContentAccess = true
        webView.settings.allowFileAccess = true
        webView.isVerticalScrollBarEnabled = false
        val url = Settings.getString("url","")
        if (url!="") webView.loadUrl(url)
        getWindowLocation()
        println(webView.url)
        Network.post("http://192.168.0.100:9102/?action=test", hashMapOf())
        Thread{
            val conn:HttpURLConnection = URL("http://192.168.0.100:9102/?action=test").openConnection() as HttpURLConnection
            conn.doInput = true
        }.start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.e("ACTIVITY RESULT", "OK")
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                FILE_CHOOSE -> {
                    val uri = data?.data
                    if (uri!=null){
                        println(getWindowLocation())
//                        try {
                            val file = File(getPath(this.applicationContext, uri))
                            println(file.absolutePath)
                            val bytes = file.readBytes()
                            val str = bytes.toString(Charset.forName("iso-8859-1"))
                            val params = hashMapOf(
                                "file" to str,
                                "name" to file.name,
                                "location" to getWindowLocation()
                            )
                            Network.post(
                                Settings.getString("server") + "?action=upload",
                                params
                            )
                            webView.evaluateJavascript("getLatest()", valueCallback)
//                        }catch (e:Exception){
//                            Log.e("Http Post Error", e.message?:"")
//                        }
                    }
//                    getWindowLocation()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()
            return true
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event)
    }

fun getWindowLocation():String{
        val url = webView.url
        val index = url?.indexOf("#")
        if (index!=null&&index!=-1){
            val location = url.slice(index until url.length)
            if (location.isNotEmpty()) return location.slice(1 until location.length)
        }
        return ""
    }



}