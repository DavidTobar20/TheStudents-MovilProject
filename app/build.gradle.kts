plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dagger)
    alias(libs.plugins.kotlin.serialization)
    // KSP genera el código de Hilt (kapt no es compatible con el Kotlin integrado de AGP 9)
    alias(libs.plugins.google.ksp)
    //Messaging
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

android {
    namespace = "com.example.thestudents"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.example.thestudents"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)

    //Hilt
    implementation(libs.dagger.hilt)
    implementation(libs.hilt.compose.navigation)  // hiltViewModel() en composables
    ksp(libs.dagger.compiler)

    //Firebase
    implementation(platform(libs.firebase.bom))   // el BOM alinea las versiones de abajo
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.storage)

    //Coil
    implementation("io.coil-kt:coil-compose:2.4.0")



    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
