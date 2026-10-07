plugins {
    id("dev.prism")
    id("com.diffplug.spotless") version "8.0.0"
}

repositories {
    mavenCentral()
}

spotless {
    java {
        target("versions/*/src/**/*.java")
        importOrder("")
        palantirJavaFormat()
    }
}

group = "com.leclowndu93150"
version = "1.0.0"

prism {
    metadata {
        modId = "kubejs_thaumaturge"
        name = "KubeJS Thaumaturge"
        description = "A Minecraft mod."
        license = "MIT"
    }

    curseMaven()
    modrinthMaven()
    maven("Leclown", "https://maven.leclowndu93150.dev/releases")
    maven("Latvian", "https://maven.latvian.dev/releases")
    maven("IllusiveSoulworks", "https://maven.theillusivec4.top/")
    maven("BlameJared", "https://maven.blamejared.com/")

    version("26.1.2") {
        neoforge {
            loaderVersion = "26.1.2.87"
            loaderVersionRange = "[4,)"

            dependencies {
                compileOnly("curse.maven:kubejs-238086:8843756")
                compileOnly("curse.maven:rhino-416294:8463898")
                localJar("libs/thaumaturge-26.1.2-NeoForge-BETA-0.2.1.jar")

                runtimeOnly("curse.maven:kubejs-238086:8843756")
                runtimeOnly("curse.maven:rhino-416294:8463898")
                localJar("libs/thaumaturge-26.1.2-NeoForge-BETA-0.2.1.jar", "runtimeOnly")
                runtimeOnly("maven.modrinth:XaDC71GB:84kuUpr4")
                runtimeOnly("top.theillusivec4.curios:curios-neoforge:15.0.0-beta.2+26.1.2")
                runtimeOnly("dev.latvian.mods:better-advanced-tooltips:2601.1.0-build.9")
            }
        }
    }

    version("1.21.1") {
        neoforge {
            loaderVersion = "21.1.244"
            loaderVersionRange = "[4,)"

            dependencies {
                compileOnly("org.jspecify:jspecify:1.0.0")
                compileOnly("curse.maven:kubejs-238086:8843626")
                compileOnly("curse.maven:rhino-416294:8463898")
                localJar("libs/thaumaturge-1.21.1-NeoForge-BETA-0.4.6.jar")

                runtimeOnly("curse.maven:kubejs-238086:8843626")
                runtimeOnly("curse.maven:rhino-416294:8463898")
                localJar("libs/thaumaturge-1.21.1-NeoForge-BETA-0.4.6.jar", "runtimeOnly")
                runtimeOnly("maven.modrinth:lithostitched:1.8.0-neoforge-21.1")
                runtimeOnly("top.theillusivec4.curios:curios-neoforge:9.5.1+1.21.1")
                runtimeOnly("curse.maven:better-advanced-tooltips-1637623:8576077")
            }
        }
    }

}
