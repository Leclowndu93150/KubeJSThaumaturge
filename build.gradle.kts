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
                compileOnly("curse.maven:kubejs-238086:8495199")
                compileOnly("curse.maven:rhino-416294:8463898")
                compileOnly("com.leclowndu93150:thaumaturge:0.1.4")

                runtimeOnly("curse.maven:kubejs-238086:8495199")
                runtimeOnly("curse.maven:rhino-416294:8463898")
                runtimeOnly("com.leclowndu93150:thaumaturge:0.1.4")
                runtimeOnly("curse.maven:terrablender-neoforge-940057:8046313")
                runtimeOnly("dev.latvian.mods:better-advanced-tooltips:2601.1.0-build.9")
            }
        }
    }

    version("1.21.1") {
        neoforge {
            loaderVersion = "21.1.244"
            loaderVersionRange = "[4,)"
        }
    }

}
