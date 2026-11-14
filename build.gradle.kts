import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.language.jvm.tasks.ProcessResources



val EXPANSION_ID = "smallcaps"
val EXPANSION_NAME = "SmallCaps"
val EXPANSION_VERSION = "1.0.4"
val EXPANSION_AUTHOR = "Colbster937"
val EXPANSION_BUKKIT_MAIN = "dev.colbster937.papi.expansions.smallcaps.SmallCapsBukkitPlugin"



plugins {
  java
}

repositories {
  mavenCentral()
  maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
  maven("https://repo.helpch.at/releases/")
  maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
  compileOnly("org.bukkit:bukkit:1.8-R0.1-SNAPSHOT")
  compileOnly("me.clip:placeholderapi:2.11.7")
  testImplementation("org.junit.jupiter:junit-jupiter:5.11.4")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

sourceSets {
  named("main") {
    java.srcDir("./src/main/java")
  }
}

val EXPAND = mapOf(
  "expansion_id" to EXPANSION_ID,
  "expansion_name" to EXPANSION_NAME,
  "expansion_version" to EXPANSION_VERSION,
  "expansion_author" to EXPANSION_AUTHOR,
  "expansion_bukkit_main" to EXPANSION_BUKKIT_MAIN
)

tasks.register<Copy>("generateExpansionSource") {
  from("./src/main/java")
  into(layout.buildDirectory.dir("generated/sources/expansion/main"))
  include("**/${EXPANSION_NAME}PAPIExpansion.java")
  expand(EXPAND)
}

tasks.named<ProcessResources>("processResources") {
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
  outputs.upToDateWhen { false }
  filesMatching(listOf("plugin.yml", "bungee.yml", "velocity-plugin.yml")) {
    expand(EXPAND)
  }
}

@Suppress("DEPRECATION")
tasks.named<JavaCompile>("compileJava") {
  dependsOn(tasks.named("generateExpansionSource"))
  options.encoding = "UTF-8"
  options.release.set(8)

  source =
    fileTree("./src/main/java") {
      exclude("**/${EXPANSION_NAME}PAPIExpansion.java")
    } +
    fileTree(layout.buildDirectory.dir("generated/sources/expansion/main")) {
      include("**/${EXPANSION_NAME}PAPIExpansion.java")
    }
}

tasks.test {
  useJUnitPlatform()
}

tasks.jar {
  archiveFileName.set("PAPI-Expansion-$EXPANSION_NAME-$EXPANSION_VERSION.jar")
}
