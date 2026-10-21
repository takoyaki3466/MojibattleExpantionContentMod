plugins {
    java
}

group = "com.takoyaki3466"
version = "1.4.1"

repositories {
    mavenLocal()
    mavenCentral()
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

val bigVersion = "0.9"
val betaOrRelease = "beta"
val smallVersion = "2"

dependencies {
    compileOnly("jp.takoa.mojibattle:mojibattle-api:v$bigVersion-$betaOrRelease-$smallVersion")
}
