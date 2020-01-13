package de.handler.mpp.mobile

import android.app.Application
import de.handler.mpp.mobile.data.appContext

class AstronautApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
    }
}