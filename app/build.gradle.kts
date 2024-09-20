plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    viewBinding {
        enable = true
    }
    namespace = "com.afterow.sanyaoyi"
    compileSdk = 30 // 保持为 30

    defaultConfig {
        applicationId = "com.afterow.sanyaoyi"
        minSdk = 30 // 可以保持为 30
        targetSdk = 30 // 保持为 30
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)

    // 检查和降级这些依赖项
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0") // 确保使用兼容的版本
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0") // 确保使用兼容的版本
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.5") // 确保使用兼容的版本
    implementation("androidx.navigation:navigation-ui-ktx:2.3.5") // 确保使用兼容的版本

    implementation("cn.6tail:tyme4j:1.1.2")
    implementation(libs.androidx.activity)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
