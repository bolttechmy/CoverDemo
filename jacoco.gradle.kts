// jacoco.gradle.kts
apply(plugin = "jacoco")

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn(tasks["testDebugUnitTest"])

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val fileFilter = listOf(
            "**/R.class",
            "**/R$*.class",
            "**/BuildConfig.*",
            "**/Manifest*.*",
            "**/*Test*.*"
    )
    val debugTree = fileTree(mapOf("dir" to "$buildDir/intermediates/javac/debug", "excludes" to fileFilter))
    val mainSrc = "$projectDir/src/main/java"

    sourceDirectories.setFrom(files(mainSrc))
    classDirectories.setFrom(files(debugTree))
    executionData.setFrom(fileTree(mapOf("dir" to "$buildDir", "includes" to listOf(
            "jacoco/testDebugUnitTest.exec",
            "outputs/code-coverage/connected/*coverage.ec"
    ))))
}
