plugins {
    java
}

group = "com.takoyaki3466"
version = "1.5.1"

repositories {
    mavenLocal()
    mavenCentral()
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

val bigVersion = "1.0"
val betaOrRelease = "release"
val smallVersion = "0"

dependencies {
    compileOnly("jp.takoa.mojibattle:mojibattle-api:v$bigVersion-$betaOrRelease-$smallVersion")
}
