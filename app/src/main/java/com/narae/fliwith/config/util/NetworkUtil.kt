package com.narae.fliwith.config.util

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.Settings

class NetworkUtil(private val context: Context) {
    fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }

    fun showNetworkDialog() {
        AlertDialog.Builder(context)
            .setTitle("네트워크 연결 필요")
            .setMessage("이 앱을 사용하려면 인터넷 연결이 필요합니다. 와이파이나 모바일 데이터를 켜시겠습니까?")
            .setPositiveButton("설정") { _, _ ->
                context.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
                // 앱 종료 또는 다른 처리
            }
            .setCancelable(false)
            .show()
    }
}
