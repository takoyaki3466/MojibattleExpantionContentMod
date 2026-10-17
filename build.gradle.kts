plugins {
    java
}

group = "com.takoyaki3466"
version = "1.3.0"

repositories {
    mavenLocal()
    mavenCentral()
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

dependencies {
    compileOnly("jp.takoa.mojibattle:mojibattle-api:0.8.0")
}
