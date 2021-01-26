
object Versions {
    const val GradlePlugin = "7.0.0-alpha04"
    const val KotlinGradlePlugin = "1.4.21"
    const val Kotlin = "1.4.21"

    const val CoreKtx = "1.3.2"
    const val AppCompat = "1.2.0"
    const val Material = "1.2.1"
    const val AsyncLayoutInflater = "1.0.0"
}

object Config {
    const val compileSdkVersion = 30
    const val buildToolsVersion = "30.0.3"
    const val minSdkVersion = 23
    const val targetSdkVersion = 30

    const val GradlePlugin = "com.android.tools.build:gradle:${Versions.GradlePlugin}"
    const val KotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KotlinGradlePlugin}"
    const val Kotlin = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.Kotlin}"
}

object AndroidX {
    const val CoreKtx = "androidx.core:core-ktx:${Versions.CoreKtx}"
    const val AppCompat = "androidx.appcompat:appcompat:${Versions.AppCompat}"
    const val Material = "com.google.android.material:material:${Versions.Material}"
    const val AsyncLayoutInflater = "androidx.asynclayoutinflater:asynclayoutinflater:${Versions.AsyncLayoutInflater}"
}