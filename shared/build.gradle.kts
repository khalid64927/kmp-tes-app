import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

plugins {
    alias(libs.plugins.org.jetbrains.kotlin.multiplatform.plugin)
    alias(libs.plugins.com.android.library.plugin)
    alias(libs.plugins.org.jetbrains.compose.plugin)
    alias(libs.plugins.org.jetbrains.kotlin.serialization.plugin)
    // TODO: check why alias is not working
    //alias(libs.plugins.org.jetbrains.kotlin.native.cocoapods.plugin)
    kotlin("native.cocoapods")
    id("com.codingfeline.buildkonfig")
    id("com.squareup.sqldelight")
    id("dev.icerock.mobile.multiplatform-resources")
    id("maven-publish")
    id("signing")
}

kotlin {
    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
        sourceSets {
            getByName("androidMain").kotlin.srcDirs("build/generated/moko/androidMain/src")
        }
    }

    targets.withType(org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget::class.java).all {
        binaries.withType(org.jetbrains.kotlin.gradle.plugin.mpp.Framework::class.java).all {
            export(libs.dev.icerock.moko.mvvm.core.get())
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()
    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            isStatic = true
            baseName = "shared"
            if (System.getenv("XCODE_VERSION_MAJOR") == "1500") {
                linkerOpts += "-ld64"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(compose.animation)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                implementation(libs.bundles.sqldelight)
                implementation(libs.org.jetbrains.kotlinx.datetime)
                implementation(libs.bundles.ktor)
                implementation(libs.org.jetbrains.kotlinx.coroutines.core)
                implementation(libs.media.kamel.image)
                implementation(libs.bundles.datastore)
                implementation(libs.io.insert.koin.core)
                api(libs.bundles.moko.mvvm)
                api(libs.bundles.moko.resources)
                api(libs.bundles.moko.permissions)

                api(libs.moe.tlaster.precompose)
                implementation(libs.touchlab.kermit)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.org.jetbrains.kotlinx.coroutines.test)
                implementation(libs.kotest.assertions)
                implementation(libs.turbine)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.com.squareup.sqldelight.android.driver)
                implementation(libs.androidx.appcompat)
                implementation(libs.androidx.activity.compose)
                implementation(libs.io.ktor.client.android)
                implementation(libs.io.ktor.client.okhttp)
                implementation(libs.io.insert.koin.android)
                implementation(libs.com.google.android.gms.play.services.location)
                // TODO: check why preview is not working in androidMain
                implementation(compose.preview)
            }
        }
        val androidUnitTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependencies {
                implementation(libs.com.squareup.sqldelight.native.driver)
                implementation(libs.io.ktor.client.darwin)
            }
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    namespace = "com.multiplatform.app.android"
    compileSdk = 34
    defaultConfig {
        minSdk = 21
    }
    compileOptions {
        // For AGP 4.1+
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.7"
    }
}

sqldelight {
    database("AppDatabase") {
        packageName = "com.multiplatform.app.database"
        sourceFolders = listOf("sqldelight")
    }
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.2.2")
    implementation(libs.androidx.core)
    testImplementation("junit:junit:4.12")
}

multiplatformResources {
    multiplatformResourcesPackage = "com.multiplatform.app" // required
}

buildkonfig {
    packageName = "com.multiplatform.app"
    val prepaidUatUrl: String = gradleLocalProperties(rootDir).getProperty("prepaidUatUrl")
    val prepaidProductionUrl: String = gradleLocalProperties(rootDir).getProperty("prepaidProductionUrl")
    // clientId and clientSecret
    val prepaidUatClientId: String = gradleLocalProperties(rootDir).getProperty("prepaidUatClientId")
    val prepaidUatClientSecret: String = gradleLocalProperties(rootDir).getProperty("prepaidUatClientSecret")
    val prepaidProdClientId: String = gradleLocalProperties(rootDir).getProperty("prepaidProdClientId")
    val prepaidProdClientSecret: String = gradleLocalProperties(rootDir).getProperty("prepaidProdClientSecret")


    defaultConfigs {
        // set values for default
        buildConfigField(STRING, "FLAVOR", "uat")
        buildConfigField(STRING, "PREPAID_URL", prepaidUatUrl)
        buildConfigField(STRING, "PREPAID_CLIENT_ID", prepaidUatClientId)
        buildConfigField(STRING, "PREPAID_CLIENT_SECRET", prepaidUatClientSecret)

    }

    defaultConfigs("uat") {
        // set values for UAT FLAVOR
        buildConfigField(STRING, "FLAVOR", "uat")
        buildConfigField(STRING, "PREPAID_URL", prepaidUatUrl)
        buildConfigField(STRING, "PREPAID_CLIENT_ID", prepaidUatClientId)
        buildConfigField(STRING, "PREPAID_CLIENT_SECRET", prepaidUatClientSecret)
    }

    defaultConfigs("production") {
        // set values for Production FLAVOR
        buildConfigField(STRING, "FLAVOR", "production")
        buildConfigField(STRING, "PREPAID_URL", prepaidProductionUrl)
        buildConfigField(STRING, "PREPAID_CLIENT_ID", prepaidProdClientId)
        buildConfigField(STRING, "PREPAID_CLIENT_SECRET", prepaidProdClientSecret)
    }
}
