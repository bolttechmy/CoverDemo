plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
        jacoco
}
jacoco {
    toolVersion = "0.8.11"
}
project.afterEvaluate {
    setupAndroidReporting()
}


fun setupAndroidReporting() {
    val buildTypes = listOf("debug")

    buildTypes.forEach { buildTypeName ->
        val sourceName = buildTypeName
        val testTaskName = "test${sourceName.capitalize()}UnitTest"
        println("Task -> $testTaskName")

        tasks.register<JacocoReport>("${testTaskName}Coverage") {
            dependsOn(tasks.findByName(testTaskName))

            group = "Reporting"
            description =
                "Generate Jacoco coverage reports on the ${buildTypeName.capitalize()} build."

            reports {
                xml.required.set(true)
                csv.required.set(false)
                html.required.set(true)
            }

            val fileFilter = listOf(
                // android
                "**/R.class",
                "**/R$*.class",
                "**/BuildConfig.*",
                "**/Manifest*.*",
                "**/*Test*.*",
                "android/**/*.*",
                // kotlin
                "**/*MapperImpl*.*",
                "**/*\$ViewInjector*.*",
                "**/*\$ViewBinder*.*",
                "**/BuildConfig.*",
                "**/*Component*.*",
                "**/*BR*.*",
                "**/Manifest*.*",
                "**/*\$Lambda$*.*",
                "**/*Companion*.*",
                "**/*Module*.*",
                "**/*Dagger*.*",
                "**/*Hilt*.*",
                "**/*MembersInjector*.*",
                "**/*_MembersInjector.class",
                "**/*_Factory*.*",
                "**/*_Provide*Factory*.*",
                // sealed and data classes
                "**/*\$Result.*",
                "**/*\$Result$*.*",
                // adapters generated by moshi
                "**/*JsonAdapter.*",
                "**/*Activity*",
                "**/di/**",
                "**/hilt*/**",
                // TODO Remove once UI and instrumented tests are added
                "**/entrypoint/**",
                "**/designsystem/**",
                "**/*Screen*.*",
                "**/*NavGraph*.*",
                "**/*Destinations*.*",
                "**/common/**",
                "**/*Extensions*.*",
            )

            val javaTree = fileTree("${project.buildDir}/intermediates/javac/$buildTypeName/classes") {
                exclude(fileFilter)
            }
            val kotlinTree = fileTree("${project.buildDir}/tmp/kotlin-classes/$buildTypeName") {
                exclude(fileFilter)
            }
            classDirectories.setFrom(files(javaTree, kotlinTree))

            executionData.setFrom(files("${project.buildDir}/jacoco/$testTaskName.exec"))
            val coverageSourceDirs = listOf(
                "${project.projectDir}/src/main/java",
                "${project.projectDir}/src/$buildTypeName/java"
            )

            sourceDirectories.setFrom(files(coverageSourceDirs))
            additionalSourceDirs.setFrom(files(coverageSourceDirs))
        }
    }
}

android {
    namespace = "io.bolttech.coverdemo"
    compileSdk = 34

    defaultConfig {
        applicationId = "io.bolttech.coverdemo"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {

        debug {
            enableUnitTestCoverage = true
            isMinifyEnabled = false
        }
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}