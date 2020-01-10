import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("kotlinx-serialization")
    id("com.android.library")
    //id("org.jetbrains.kotlin.native.cocoapods")
}

kotlin {
    ios {
        compilations {
            val main by getting {
                kotlinOptions.freeCompilerArgs = listOf("-Xobjc-generics")
            }
        }
    }
    android()

    //select iOS target platform depending on the Xcode environment variables
    val iOSTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
            ::iosArm64
        else
            ::iosX64

    iOSTarget("ios") {
        binaries {
            framework {
                baseName = "SharedCode"
            }
        }
    }

    sourceSets["commonMain"].dependencies {
        // Kotlin
        implementation("org.jetbrains.kotlin:kotlin-stdlib-common")

        // Coroutines
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.3.3")

        // Ktor
        implementation("io.ktor:ktor-client-core:1.3.0-rc2")
        implementation("io.ktor:ktor-client-json:1.3.0-rc2")
        implementation("io.ktor:ktor-client-logging:1.3.0-rc2")
        implementation("io.ktor:ktor-client-serialization:1.3.0-rc2")

        // Serialization
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:0.14.0")
    }

    sourceSets["iosMain"].dependencies {
        // Coroutines
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:1.3.3")

        // Serialization
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:0.14")

        // Ktor
        implementation("io.ktor:ktor-client-ios:1.3.0-rc2")
        implementation("io.ktor:ktor-client-core-native:1.3.0-rc2")
        implementation("io.ktor:ktor-client-json-native:1.3.0-rc2")
        implementation("io.ktor:ktor-client-logging-native:1.3.0-rc2")
        implementation("io.ktor:ktor-client-serialization-native:1.3.0-rc2")
    }
}

android {
    compileSdkVersion(29)
    buildToolsVersion = "29.0.2"
    defaultConfig {
        minSdkVersion(16)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/LICENSE")
        exclude("META-INF/LICENSE.txt")
        exclude("META-INF/license.txt")
        exclude("META-INF/NOTICE")
        exclude("META-INF/NOTICE.txt")
        exclude("META-INF/notice.txt")
        exclude("META-INF/ASL2.0")
        exclude("META-INF/*.kotlin_module")
    }

    sourceSets {
        getByName("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            java.srcDirs("src/androidMain/kotlin")
            res.srcDirs("src/androidMain/res")
        }
    }
}

dependencies {
    // Kotlin
    kotlin("stdlib")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3")

    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.14.0")

    // Ktor
    implementation("io.ktor:ktor-client-android:1.3.0-rc2")
    implementation("io.ktor:ktor-client-core-jvm:1.3.0-rc2")
    implementation("io.ktor:ktor-client-json-jvm:1.3.0-rc2")
    implementation("io.ktor:ktor-client-logging-jvm:1.3.0-rc2")
    implementation("io.ktor:ktor-client-serialization-jvm:1.3.0-rc2")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-extensions:2.1.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.1.0")
}

// Used for the xCode integration
val packForXcode by tasks.creating(Sync::class) {
    val targetDir = File(buildDir, "xcode-frameworks")

    /// selecting the right configuration for the iOS
    /// framework depending on the environment
    /// variables set by Xcode build
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val framework = kotlin.targets
        .getByName<KotlinNativeTarget>("ios")
        .binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)

    from({ framework.outputDirectory })
    into(targetDir)

    /// generate a helpful ./gradlew wrapper with embedded Java path
    doLast {
        val gradlew = File(targetDir, "gradlew")
        gradlew.writeText(
            "#!/bin/bash\n"
                    + "export 'JAVA_HOME=${System.getProperty("java.home")}'\n"
                    + "cd '${rootProject.rootDir}'\n"
                    + "./gradlew \$@\n"
        )
        gradlew.setExecutable(true)
    }
}

tasks.getByName("build").dependsOn(packForXcode)