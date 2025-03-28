import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    kotlin("plugin.serialization") version "2.0.20"
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.accompanist.systemuicontroller)
            implementation(libs.androidx.work.runtime)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(compose.materialIconsExtended)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.okhttp)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.navigation.compose)
            implementation(libs.androidx.datastore.preferences.core)
            implementation(libs.lifecycle.viewmodel.compose)
            implementation(libs.androidx.room.runtime)
            implementation(libs.sqlite.bundled)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.jsoup)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

android {
    namespace = "com.sakethh.linkora"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.sakethh.linkora"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 36
        versionName = "0.12.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }

        register("preview") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
            applicationIdSuffix = ".preview"
            versionNameSuffix = "-preview"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    sourceSets["main"].res.srcDirs(
        "src/commonMain/resources",
        "src/androidMain/resources"
    )

    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
    ksp(libs.androidx.room.compiler)
}

compose.desktop {
    application {
        mainClass = "com.sakethh.linkora.MainKt"

        nativeDistributions {
            targetFormats(
                TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb,
                TargetFormat.AppImage, TargetFormat.Rpm, TargetFormat.Pkg, TargetFormat.Exe
            )
            packageName = "Linkora"
            this.vendor = "Saketh Pathike"
            this.packageVersion = "1.0.3"
            /*

            This logo (src/desktopMain/resources/logo.*) was painted by `mondstern`.
            The original post can be found here: https://pixelfed.social/p/mondstern/747494483548287527

             */

            windows {
                this.iconFile.set(project.file("src/desktopMain/resources/logo.ico"))
            }

            linux {
                this.iconFile.set(project.file("src/desktopMain/resources/logo.png"))
            }
            modules("jdk.unsupported")
            modules("jdk.unsupported.desktop")
        }
    }
}
