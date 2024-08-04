package com.narae.fliwith.config.util

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.Window
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import com.narae.fliwith.R
import com.narae.fliwith.databinding.DialogRequireNetworkBinding

class NetworkUtil(private val context: Context) {

    private var networkDialog: CustomNetworkDialog = CustomNetworkDialog(context)

    fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }

    fun showNetworkDialog() {
        networkDialog.show()
        networkDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
    }
}

class CustomNetworkDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        val binding = DialogRequireNetworkBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnSettings.setOnClickListener {
            context.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            dismiss()
        }

        setCancelable(false)
    }
}