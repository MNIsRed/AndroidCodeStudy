plugins {
    id("java-library")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies{
    api(libs.kspSymbolsProcessor)
    api(libs.kotlinpoet)
    api(libs.kotlinpoetKsp)
}