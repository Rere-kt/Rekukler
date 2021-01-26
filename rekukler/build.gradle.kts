plugins {
    id("com.android.library")
    kotlin("android")
    id("maven-publish")
}

android {
    compileSdkVersion(Config.compileSdkVersion)
    buildToolsVersion(Config.buildToolsVersion)

    defaultConfig {
        minSdkVersion(Config.minSdkVersion)
        targetSdkVersion(Config.targetSdkVersion)
        versionCode = 1
        versionName = "1.0"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
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
        viewBinding = true
    }
}

dependencies {
    implementation(Config.Kotlin)
    implementation(AndroidX.CoreKtx)
    implementation(AndroidX.AppCompat)
    implementation(AndroidX.Material)
    implementation(AndroidX.AsyncLayoutInflater)
}

afterEvaluate {
    publishing {
        publications {
            // Creates a Maven publication called "release".
            register("release", MavenPublication::class) {
                groupId = "com.github.Rere-kt"
                artifactId = "rekukler"
                version = "1.0"
            }
        }
    }
}