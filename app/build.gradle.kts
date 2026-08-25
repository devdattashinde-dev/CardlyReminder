import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.cardleyreminder.apps"
    compileSdk = 35

    defaultConfig {

        applicationId = "com.cardleyreminder.apps"
        minSdk = 28
        targetSdk = 35
        versionCode = 11
        versionName = "1.0.6"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        /*applicationVariants.all { variant ->
            variant.outputs.all { output ->
                val appName = "CardlyReminder" // Change this to your desired APK name
                val versionName = variant.versionName
                val versionCode = variant.versionCode
                val buildType = variant.buildType.name

                // Generate a timestamp (YYYYMMDD_HHmmss format)
                val timestamp = SimpleDateFormat("ddMMyyyy_HHmmss").format(Date())

                // Append build type (DEBUG or RELEASE)
                val suffix = if (buildType == "debug") "-DEBUG" else "-RELEASE"
                val name = "$appName$suffix-v$versionName($versionCode)-$timestamp.apk"
                // Set the new APK file name
                output.outputFileName.set(name)
            }
        }*/
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}

dependencies {

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0-beta01")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("androidx.activity:activity-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")

    implementation("androidx.core:core-ktx:1.15.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")


    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.annotation:annotation:1.9.1")

    // Testing
    implementation("junit:junit:4.13.2")
    implementation("androidx.test.ext:junit:1.2.1")
    implementation("androidx.test.espresso:espresso-core:3.6.1")

    // Utility Libraries
    implementation("com.intuit.sdp:sdp-android:1.0.6")
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // Kotlin StdLib (updated)
    implementation ("org.jetbrains.kotlin:kotlin-stdlib:2.1.10")

    // ViewModel & LiveData
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")

    // Retrofit & Gson
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // Glide & Picasso
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.squareup.picasso:picasso:2.8")

    // DataStore
    implementation ("androidx.datastore:datastore-preferences:1.1.3")


    // Image Cropper
    implementation ("com.vanniktech:android-image-cropper:4.5.0")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.10.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-messaging:24.1.0")

    // Google Sign-In
    implementation("com.google.android.gms:play-services-auth:21.3.0")

    // Other Libraries
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:13.0.0")
    implementation("com.github.mukeshsolanki.android-otpview-pinview:otpview:3.1.0")
    implementation("me.leolin:ShortcutBadger:1.1.22@aar")
    implementation("com.hbb20:ccp:2.5.4")

    // Switch Button
    implementation("com.github.zcweng:switch-button:0.0.3@aar")

    // Jetpack Compose (only if using Compose)
    implementation("androidx.compose.runtime:runtime:1.7.8")
    implementation("androidx.compose.foundation:foundation:1.7.8")
    implementation("com.facebook.android:facebook-android-sdk:[8,9)")
}
