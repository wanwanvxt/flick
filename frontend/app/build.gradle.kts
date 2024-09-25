plugins {
  alias(libs.plugins.android.application)
}

android {
  namespace = "vn.edu.eaut.flick"
  compileSdk = 34

  defaultConfig {
    applicationId = "vn.edu.eaut.flick"
    minSdk = 24
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"
    buildFeatures.buildConfig = true
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
}

dependencies {

  implementation(libs.appcompat)
  implementation(libs.material)
  implementation(libs.activity)
  implementation(libs.constraintlayout)
  implementation(libs.retrofit)
  implementation(libs.converter.gson)
  implementation(libs.picasso)
  implementation(libs.flexbox)
  implementation(libs.media3.exoplayer)
  implementation(libs.media3.ui)
  implementation(libs.media3.common)
}
