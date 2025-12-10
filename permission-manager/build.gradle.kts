plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("maven-publish")
}

/**
 * JitPack 用的 maven-publish 配置
 */
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                // groupId 建议保持和之前 base 一致
                groupId = "com.github.yangxiaobinhaoshuai.Fundamental"
                // 以后依赖时的 artifactId
                artifactId = "permission_manager"
                // 这个 version 写不写都行，但建议和 tag 一致，方便你自己看
                version = libs.versions.tagVersion.get()

                // Android Library 要从 release 组件导出
                from(components["release"])
            }
        }
    }
}

android {
    namespace = "io.github.wukeji.permission_manager"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}