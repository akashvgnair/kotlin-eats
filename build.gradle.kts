plugins {
    kotlin("jvm") version "2.2.10" apply false
}

group = "com.akashvgnair"
version = "1.0-SNAPSHOT"

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenCentral()
    }

    dependencies {
        "testImplementation"(kotlin("test"))
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    extensions.configure<org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension> {
        jvmToolchain(21)
    }
}

tasks.register("dependencyGraph") {
    description = "Generates a Graphviz .dot file of module dependencies"
    group = "reporting"

    doLast {
        val dot = StringBuilder()
        dot.appendLine("digraph {")
        dot.appendLine("  rankdir=LR;")
        dot.appendLine("  node [shape=box, style=filled, fillcolor=\"#E8E8E8\"];")

        val regex = Regex("""project\(":(.+?)"\)""")

        subprojects.forEach { proj ->
            val buildFile = proj.buildFile
            if (buildFile.exists()) {
                regex.findAll(buildFile.readText()).forEach { match ->
                    val to = match.groupValues[1]
                    dot.appendLine("  \"${proj.name}\" -> \"$to\";")
                }
            }
        }

        dot.appendLine("}")

        val outputFile = file("build/reports/dependency-graph.dot")
        outputFile.parentFile.mkdirs()
        outputFile.writeText(dot.toString())
        println("DOT file: ${outputFile.absolutePath}")
        println("Paste contents at https://dreampuf.github.io/GraphvizOnline/")
    }
}