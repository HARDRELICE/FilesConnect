package com.hardrelice.filesconnect.clients

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.core.app.ActivityCompat.startActivityForResult
import com.hardrelice.filesconnect.MainActivity


class FCChromeClient(private val activity: Activity):WebChromeClient() {
    override fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: FileChooserParams?
    ): Boolean {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        //intent.setType(“image/*”);//选择图片
        //intent.setType(“audio/*”); //选择音频
        //intent.setType(“video/*”); //选择视频 （mp4 3gp 是android支持的视频格式）
        //intent.setType(“video/*;image/*”);//同时选择视频和图片
        //intent.setType(“image/*”);//选择图片
        //intent.setType(“audio/*”); //选择音频
        //intent.setType(“video/*”); //选择视频 （mp4 3gp 是android支持的视频格式）
        //intent.setType(“video/*;image/*”);//同时选择视频和图片
        intent.type = "*/*" //无类型限制

        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(activity, intent, MainActivity.FILE_CHOOSE,null)
        return super.onShowFileChooser(webView, filePathCallback, fileChooserParams)
    }
}