import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

// Đọc khóa bí mật tạo mã kích hoạt từ local.properties (KHÔNG commit lên Git).
// Thêm dòng sau vào file local.properties trên máy bạn (file này đã có trong .gitignore):
//   LICENSE_SECRET=chuoi-bi-mat-cua-rieng-ban
val localProps = Properties().apply {
    val f = rootProject.file("local.properties")
    if (f.exists()) load(FileInputStream(f))
}
val licenseSecret: String = (localProps.getProperty("LICENSE_SECRET")
    ?: System.getenv("LICENSE_SECRET")
    ?: "CHANGE_ME_SET_IN_local.properties")

android {
    namespace = "com.byd.greeting"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.byd.greeting"
        minSdk = 24
        targetSdk = 34
        versionCode = 5
        versionName = "1.4"
        buildConfigField("String", "LICENSE_SECRET", "\"$licenseSecret\"")
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
        buildConfig = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.preference:preference-ktx:1.2.1")
}
