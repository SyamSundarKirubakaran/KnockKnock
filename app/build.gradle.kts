plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
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

    // junit
    testImplementation(libs.junit4)

    // android test - junit, espresso
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso)
}