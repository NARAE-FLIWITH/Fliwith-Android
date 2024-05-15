plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.narae.fliwith"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.narae.fliwith"
        minSdk = 24
        targetSdk = 34
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
    buildFeatures{
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // https://github.com/square/retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")

    // https://github.com/square/okhttp
    implementation ("com.squareup.okhttp3:okhttp:4.9.0")

    // https://github.com/square/retrofit/tree/master/retrofit-converters/gson
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")

    // https://github.com/ybq/Android-SpinKit
    implementation ("com.github.ybq:Android-SpinKit:1.4.0")

    //framework ktx dependency 추가
    implementation ("androidx.fragment:fragment-ktx:1.6.2")

    // kakao map
    implementation ("com.kakao.maps.open:android:2.9.5")
}