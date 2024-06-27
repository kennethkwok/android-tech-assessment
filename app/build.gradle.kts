plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.pelagohealth.codingchallenge"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.pelagohealth.codingchallenge"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    val nav_version = "2.7.7"
    val core_version = "1.13.1"
    val lifecycle_version = "2.8.2"
    val activity_version = "1.9.0"
    val kotlin_coroutines_version = "1.8.1"
    val compose_bom_version = "2024.06.00"
    val retrofit_version = "2.11.0"
    val moshi_version = "1.15.1"
    val okhttp_interceptor_version = "4.12.0"
    val hilt_version = "2.51.1"
    val timber_version = "5.0.1"
    val junit_version = "4.13.2"
    val androidx_junit_version = "1.2.0"
    val espresso_version = "3.6.0"
    val hilt_navigation_compose_version = "1.2.0"
    val room_version = "2.6.1"
    val mockk_version = "1.13.11"
    val kotlinx_coroutines_test_version = "1.8.1"
    val core_testing_version = "2.2.0"
    val turbine_version = "1.1.0"

    implementation("androidx.core:core-ktx:$core_version")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-runtime-compose-android:$lifecycle_version")
    implementation("androidx.activity:activity-ktx:$activity_version")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlin_coroutines_version")

    // Compose
    implementation("androidx.activity:activity-compose:$activity_version")
    implementation(platform("androidx.compose:compose-bom:$compose_bom_version"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // navigation
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")
    implementation("androidx.navigation:navigation-compose:$nav_version")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-moshi:$retrofit_version")
    implementation("com.squareup.moshi:moshi:$moshi_version")
    implementation("com.squareup.moshi:moshi-kotlin:$moshi_version")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttp_interceptor_version")

    // Hilt
    implementation("com.google.dagger:hilt-android:$hilt_version")
    kapt("com.google.dagger:hilt-android-compiler:$hilt_version")
    implementation("androidx.hilt:hilt-navigation-compose:$hilt_navigation_compose_version")

    // Logging
    implementation("com.jakewharton.timber:timber:$timber_version")

    // Room database
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")

    // Unit testing
    testImplementation("junit:junit:$junit_version")
    testImplementation("io.mockk:mockk:$mockk_version")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinx_coroutines_test_version")
    testImplementation("androidx.arch.core:core-testing:$core_testing_version")
    testImplementation("app.cash.turbine:turbine:$turbine_version")
    testImplementation("com.squareup.okhttp3:mockwebserver:$okhttp_interceptor_version")

    // UI testing
    androidTestImplementation("androidx.test.ext:junit:$androidx_junit_version")
    androidTestImplementation("androidx.test.espresso:espresso-core:$espresso_version")
    androidTestImplementation(platform("androidx.compose:compose-bom:$compose_bom_version"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation("androidx.navigation:navigation-testing:$nav_version")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

kapt {
    correctErrorTypes = true
}