package de.handler.mpp.mobile.data

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver

lateinit var appContext: Context

actual fun createDatabase(): AstronautDatabase? {
    val driver = AndroidSqliteDriver(AstronautDatabase.Schema,
        appContext, "mpp.db")
    AstronautDatabase.Schema.create(driver = driver)
    return AstronautDatabase(driver = driver)
}