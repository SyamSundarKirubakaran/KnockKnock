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
        buildConfig = true
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    buildTypes {

        val apiBaseUrl = "API_BASE_URL"
        val apiBaseUrlType = "String"

        debug {
            isMinifyEnabled = false
            buildConfigField(apiBaseUrlType, apiBaseUrl, "\"https://api.github.com\"")
        }
        release {
            isMinifyEnabled = true
            buildConfigField(apiBaseUrlType, apiBaseUrl, "\"https://api.github.com\"")
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
    implementation(libs.square.okhttp.interceptor.logging)
    implementation(libs.fb.interceptor.stetho)
    implementation(libs.square.okhttp.interceptor.stetho)
    implementation(libs.square.retrofit)
    implementation(libs.square.retrofit.convertorfactory.gson)

    // coroutines
    implementation(libs.kotlinx.coroutines)

    // custom test runners
    implementation(libs.androidx.test.runner)

    /////// test dependencies

    // junit
    testImplementation(libs.junit4)

    // android test - junit, espresso
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso)

    // hilt
    testImplementation(libs.dagger.hilt.android.testing)
    kaptTest(libs.dagger.hilt.android.compiler)


    // mockito
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.android)

    // roboelectric
    testImplementation(libs.roboelectric)

    // coroutine test
    testImplementation(libs.kotlinx.coroutines.test)
}

kapt {
    correctErrorTypes = true
}