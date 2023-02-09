package kr.co.vilez.util

import android.content.Context
import android.net.ConnectivityManager


class LiveNetworkMonitor(context: Context) : NetworkMonitor {
    private val applicationContext: Context

    init {
        applicationContext = context.applicationContext
    }

    override val isConnected: Boolean
        get() {
            val cm =
                applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            return activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting
        }
}