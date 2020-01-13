package de.handler.mpp.mobile.data

import com.squareup.sqldelight.drivers.ios.NativeSqliteDriver

actual fun createDatabase(): AstronautDatabase? {
    val driver = NativeSqliteDriver(AstronautDatabase.Schema, "mpp.db")
    return AstronautDatabase(driver = driver)
}