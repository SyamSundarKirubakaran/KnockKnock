plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "work.syam.knockknock"
    compileSdk = 34

    defaultConfig {
        applicationId = "work.syam.knockknock"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    // standard libraries
    implementation(libs.androidx.ktx)
    implementation(libs.androidx.appcompat)

    // material design
    implementation(libs.google.materialdesign)

    // constraint layout
    implementation(libs.androidx.constraintlayout)

    // hilt
    implementation(libs.dagger.hilt)
    kapt(libs.dagger.hilt.compiler)

    // activity ktx
    implementation(libs.androidx.activity.ktx)

    // lifecycle ktx
    implementation(libs.androidx.viewmodel.lifecycle.ktx)
    implementation(libs.androidx.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)

    // fragment ktx
    implementation(libs.androidx.fragment.ktx)

    // picasso
    implementation(libs.squareup.picasso)
    
    // networking
    implementation(libs.gson)
    implementation(libs.square.okhttp)
    implementation(libs.square.okhttp.interceptor)
    implementation(libs.square.retrofit)
    implementation(libs.square.moshi)

    // coroutines
    implementation(libs.kotlinx.coroutines)

    /////// test dependencies

    // junit
    testImplementation(libs.junit4)

    // android test - junit, espresso
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso)
}

kapt {
    correctErrorTypes = true
}