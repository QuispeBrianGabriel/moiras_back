import java.util.Properties

tasks.register("version") {

    group = "versioning"
    description = "Incrementa la versión. Uso: ./gradlew version -Ptype=patch|minor|major"

    doLast {

        val type = findProperty("type")?.toString()
            ?: throw GradleException(
                "Uso: ./gradlew version -Ptype=patch|minor|major"
            )

        val file = file("gradle.properties")

        val properties = Properties()

        file.inputStream().use {
            properties.load(it)
        }

        val current = properties.getProperty("version")

        val snapshot = current.endsWith("-SNAPSHOT")

        val clean = current.removeSuffix("-SNAPSHOT")

        val numbers = clean
            .split(".")
            .map { it.toInt() }
            .toMutableList()

        when (type) {

            "patch" -> {
                numbers[2]++
            }

            "minor" -> {
                numbers[1]++
                numbers[2] = 0
            }

            "major" -> {
                numbers[0]++
                numbers[1] = 0
                numbers[2] = 0
            }

            else ->
                throw GradleException("Tipo inválido: $type")
        }

        val next =
            numbers.joinToString(".") +
                    if (snapshot) "-SNAPSHOT" else ""

        properties["version"] = next

        file.outputStream().use {
            properties.store(it, null)
        }

        println()
        println("Versión: $current → $next")
        println()

        fun runCommand(vararg command: String) {
            val process = ProcessBuilder(*command)
                .directory(file.parentFile)
                .redirectErrorStream(true)
                .start()

            val output = process.inputStream.bufferedReader().readText()
            val exitCode = process.waitFor()

            if (exitCode != 0) {
                throw GradleException(
                    "Comando falló (${command.joinToString(" ")}):\n$output"
                )
            }
        }

        runCommand("git", "add", "gradle.properties")
        runCommand("git", "commit", "-m", "Version $next 🎉")

        println("✅ Commit creado: Version $next 🎉")
        println()
    }
}
