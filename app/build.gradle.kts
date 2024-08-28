import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.narae.fliwith"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.narae.fliwith"
        minSdk = 24
        targetSdk = 34
        versionCode = 3
        versionName = "1.2"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "SERVER_URL", getProperty("SERVER_URL"))
        buildConfigField("String", "GPT_KEY", getProperty("GPT_KEY"))
        buildConfigField("String", "POLICY_CONTRACT_URL", getProperty("POLICY_CONTRACT_URL"))
        buildConfigField("String", "PRIVACY_CONTRACT_URL", getProperty("PRIVACY_CONTRACT_URL"))
        buildConfigField("String", "SENSITIVE_CONTRACT_URL", getProperty("SENSITIVE_CONTRACT_URL"))
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
    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-location:21.2.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // https://github.com/square/retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // https://github.com/square/okhttp
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    // https://github.com/square/retrofit/tree/master/retrofit-converters/gson
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
    // https://github.com/ybq/Android-SpinKit
    implementation("com.github.ybq:Android-SpinKit:1.4.0")
    //framework ktx dependency 추가
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    // kakao map
    implementation("com.kakao.maps.open:android:2.9.5")
    // kakao link
    implementation ("com.kakao.sdk:v2-share:2.20.3")
    // navigation
    val nav_version = "2.7.7"
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    // ViewPager2
    implementation("androidx.viewpager2:viewpager2:1.0.0")

    // Normal
    implementation("io.github.ParkSangGwon:tedpermission-normal:3.3.0")
    // glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    // photo picker
    implementation("androidx.activity:activity-ktx:1.6.0")
    // kakao login
    implementation("com.kakao.sdk:v2-user:2.20.3")

    // splash Screen api
    implementation ("androidx.core:core-splashscreen:1.0.1")
}

fun getProperty(keyName: String): String {
    return gradleLocalProperties(rootDir).getProperty(keyName)
}