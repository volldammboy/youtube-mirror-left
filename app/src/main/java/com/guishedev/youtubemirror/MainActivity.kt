package com.guishedev.youtubemirror

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.guishedev.youtubemirror.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var mirrored = false
    private var speedIndex = 6
    private val speeds = doubleArrayOf(0.1, 0.2, 0.3, 0.4, 0.5, 0.75, 1.0, 1.25, 1.5, 2.0)

    private var customView: View? = null
    private var customViewCallback: WebChromeClient.CustomViewCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupWebView()

        binding.btnMirror.setOnClickListener { toggleMirror() }
        binding.btnSpeed.setOnClickListener { cycleSpeed() }
        binding.btnRefresh.setOnClickListener { binding.webView.reload() }

        binding.btnMirror.text = if (mirrored) "Espejo: SI" else "Espejo: NO"
        binding.btnSpeed.text = "x${speeds[speedIndex]}"
    }

    private fun setupWebView() {
        val webView = binding.webView
        val ws = webView.settings

        ws.javaScriptEnabled = true
        ws.domStorageEnabled = true
        ws.databaseEnabled = true
        ws.mediaPlaybackRequiresUserGesture = false
        ws.useWideViewPort = true
        ws.loadWithOverviewMode = true
        ws.setSupportZoom(true)
        ws.builtInZoomControls = true
        ws.displayZoomControls = false
        ws.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
        ws.userAgentString =
            "Mozilla/5.0 (Linux; Android 14; Pixel 8) AppleWebKit/537.36 " +
                "(KHTML, like Gecko) Chrome/126.0.0.0 Mobile Safari/537.36"

        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)

        webView.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: android.webkit.WebResourceRequest): Boolean = false

            override fun onPageFinished(view: WebView, url: String?) {
                applyMirrorState()
                applySpeed()
            }
        })

        webView.setWebChromeClient(object : WebChromeClient() {
            override fun onShowCustomView(view: View, callback: CustomViewCallback) {
                showFullscreen(view, callback)
            }

            override fun onHideCustomView() {
                hideFullscreen()
            }
        })

        webView.loadUrl("https://m.youtube.com")
    }

    private fun toggleMirror() {
        mirrored = !mirrored
        binding.btnMirror.text = if (mirrored) "Espejo: SI" else "Espejo: NO"
        applyMirrorState()
    }

    private fun cycleSpeed() {
        speedIndex = (speedIndex + 1) % speeds.size
        binding.btnSpeed.text = "x${speeds[speedIndex]}"
        applySpeed()
    }

    private fun applyMirrorState() {
        val script = """
            (function(){
                if (!window.__ytMirrorStyleInstalled) {
                    var s = document.createElement('style');
                    s.id = 'yt-mirror-style';
                    s.textContent = 'html.yt-mirror video { transform: scaleX(-1) !important; transform-origin: center center !important; }';
                    (document.head || document.documentElement).appendChild(s);
                    window.__ytMirrorStyleInstalled = true;
                }
                if (${if (mirrored) "true" else "false"}) {
                    document.documentElement.classList.add('yt-mirror');
                } else {
                    document.documentElement.classList.remove('yt-mirror');
                }
                document.querySelectorAll('video').forEach(function(v){
                    v.style.transform = ${if (mirrored) "'scaleX(-1)'" else "''"};
                });
            })();
        """.trimIndent()
        binding.webView.evaluateJavascript(script, null)
    }

    private fun applySpeed() {
        val speed = speeds[speedIndex]
        val script = "document.querySelectorAll('video').forEach(function(v){ v.playbackRate = $speed; });"
        binding.webView.evaluateJavascript(script, null)
    }

    private fun showFullscreen(view: View, callback: WebChromeClient.CustomViewCallback) {
        customView = view
        customViewCallback = callback
        (binding.root as ViewGroup).addView(
            view,
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        )
        binding.webView.visibility = View.GONE
        binding.controls.visibility = View.GONE
    }

    private fun hideFullscreen() {
        customView?.let { (binding.root as ViewGroup).removeView(it) }
        customView = null
        customViewCallback?.onCustomViewHidden()
        customViewCallback = null
        binding.webView.visibility = View.VISIBLE
        binding.controls.visibility = View.VISIBLE
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (customView != null) {
            hideFullscreen()
            return
        }
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        binding.webView.destroy()
        super.onDestroy()
    }
}
