package com.narae.fliwith.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.narae.fliwith.databinding.DialogLoadingBinding

import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout

class LoadingDialog(context: Context) : Dialog(context) {
    private lateinit var binding: DialogLoadingBinding
    private val overlayView: View
    private val windowManager: WindowManager
    private val layoutParams: WindowManager.LayoutParams

    init {
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        overlayView = View(context).apply {
            setBackgroundColor(Color.TRANSPARENT)
        }

        layoutParams = WindowManager.LayoutParams().apply {
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.MATCH_PARENT
            type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
            format = PixelFormat.TRANSLUCENT
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DialogLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        window!!.setBackgroundDrawable(ColorDrawable())
        window!!.setDimAmount(0.2f)
    }

    private fun disableTouchEvents() {
        windowManager.addView(overlayView, layoutParams)
    }

    private fun enableTouchEvents() {
        windowManager.removeView(overlayView)
    }

    override fun show() {
        if (!this.isShowing) super.show()
        disableTouchEvents()
    }

    override fun dismiss() {
        super.dismiss()
        enableTouchEvents()
    }
}
