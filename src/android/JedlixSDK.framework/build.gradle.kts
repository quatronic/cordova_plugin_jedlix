// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("io.github.gradle-nexus.publish-plugin") version "1.3.0"
    id("org.jmailen.kotlinter") version "3.14.0"
}

android {
    compileSdkVersion = 33
    namespace="com.jedlix.sdk"

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFile("proguard-rules.pro")
    }

    buildTypes {
        release {
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

    dependencies {
        val androidCoreVersion: String by project
        val appCompatVersion: String by project
        val materialVersion: String by project
        val serializationVersion: String by project
        val ktorVersion: String by project
        val androidxActivityVersion: String by project
        val androidxLifecycleVersion: String by project
        val androidxBrowserVersion: String by project

        implementation("androidx.core:core-ktx:$androidCoreVersion")
        implementation("androidx.appcompat:appcompat:$appCompatVersion")
        implementation("com.google.android.material:material:$materialVersion")

        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")

        implementation("io.ktor:ktor-client-core:$ktorVersion")
        implementation("io.ktor:ktor-client-android:$ktorVersion")
        implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
        implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
        implementation("io.ktor:ktor-client-logging:$ktorVersion")

        implementation("androidx.activity:activity-ktx:$androidxActivityVersion")
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:$androidxLifecycleVersion")
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$androidxLifecycleVersion")
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:$androidxLifecycleVersion")
        implementation("androidx.browser:browser:$androidxBrowserVersion")

        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.1.5")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    }
}
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        val androidGradlePluginVersion: String by project
        val kotlinVersion: String by project
        val dokkaVersion: String by project
        classpath("com.android.tools.build:gradle:$androidGradlePluginVersion")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:$dokkaVersion")
    }
}

val signingKeyId: String? by project
val signingPassword: String? by project
val signingSecretKeyRingFile: String? by project
val ossrhUsername: String? by project
val ossrhPassword: String? by project
val sonatypeStagingProfileId: String? by project

// Stub secrets to let the project sync and build without the publication values set up
ext["signing.keyId"] = signingKeyId ?: System.getenv("SIGNING_KEY_ID")
ext["signing.password"] = signingPassword ?: System.getenv("SIGNING_PASSWORD")
ext["signing.secretKeyRingFile"] = signingSecretKeyRingFile ?: System.getenv("SIGNING_SECRET_KEY_RING_FILE")
ext["ossrhUsername"] = ossrhUsername ?: System.getenv("OSSRH_USERNAME")
ext["sonatypeStagingProfileId"] = sonatypeStagingProfileId ?: System.getenv("SONATYPE_STAGING_PROFILE_ID")

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
            username.set(ossrhUsername)
            password.set(ossrhPassword)
            stagingProfileId.set(sonatypeStagingProfileId)
        }
    }
}

allprojects {
    apply(plugin = "org.jmailen.kotlinter")

    // Workaround for Kapt not setting the proper JVM target
    // See https://youtrack.jetbrains.com/issue/KT-55947/Unable-to-set-kapt-jvm-target-version
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = "11"
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
