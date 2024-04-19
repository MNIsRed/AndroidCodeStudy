plugins {
    id("java-library")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies{
    implementation(libs.kspSymbolsProcessor)
    implementation(libs.kotlinpoet)
    implementation(libs.kotlinpoetKsp)
}