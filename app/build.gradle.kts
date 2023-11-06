plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.lin_li_tranimg"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.lin_li_tranimg"
        minSdk = 24
        targetSdk = 33
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
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
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
    val kotlinVersion = "ktx:2.6.2"
    // ViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-$kotlinVersion") // Use the latest version
    // LiveData
    implementation ("androidx.lifecycle:lifecycle-livedata-$kotlinVersion") // Use the latest version
    // Lifecycle runtime for collecting flows in the ViewModel
    implementation ("androidx.lifecycle:lifecycle-runtime-$kotlinVersion") // Use the latest version
    // Compose ViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2") // Use the latest version

    implementation("com.cmoney.backend2:backend2:5.58.0")
    implementation("com.cmoney.logdatarecorder:logdatarecorder-data:5.5.0")
    implementation("com.cmoney.logdatarecorder:logdatarecorder-domain:5.5.0")

    // If you're using navigation-compose, you might need to add this as well
    implementation ("androidx.navigation:navigation-compose:2.5.3") // Use the latest version
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}