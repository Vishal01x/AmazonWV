package com.exa.android.webviewapp

import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(
    url: String,
    onBackPressed: () -> Unit
) {
    var canGoBack by remember { mutableStateOf(false) }
    val webView = remember { mutableStateOf<WebView?>(null) }

    BackHandler(enabled = canGoBack) { // Handling back press in WebView
        webView.value?.let { webViewInstance ->
            if (webViewInstance.canGoBack()) {
                webViewInstance.goBack() // Navigate back in WebView
            } else {
                onBackPressed() // Perform the action when WebView cannot go back
            }
        }
    }

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        return false // Let WebView handle the URL
                    }
                }
                webChromeClient = WebChromeClient()
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                loadUrl(url)
            }
        },
        update = { view ->
            webView.value = view // Update WebView reference
            canGoBack = view.canGoBack() // Update back navigation state
        }
    )
}
