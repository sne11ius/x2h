import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigRenderOptions
import io.github.projectmapk.jackson.module.kogera.registerKotlinModule

class X2K : CliktCommand(
    help = """Convert from YAML to HOCON format.
        |If run without arguments, reads from stdin and writes to stdout.
        |Examples:
        |
        |Convert a file to stdout: `cat my-file.yaml | x2h`
        |
        |Convert using input file and output file `x2h -i in-file.yaml -o out-file.conf`
        """.trimMargin(),
    epilog = """
        |Project info @ https://github.com/sne11ius/x2h
        |
        |Built from revision $gitHash (https://github.com/sne11ius/x2h/commit/${gitHash})
        """.trimMargin()
) {
    val inputFile by option(
        "--input",
        "-i",
        help = "Optional input file containing yaml. If omitted, stdin is used."
    ).file(mustExist = true, canBeDir = false)
    val outputFile by option("--output", "-o", help = "Optional output file. If omitted, stdout is used.").file(
        mustExist = false,
        canBeDir = false
    )

    override fun run() {
        val input: String = inputFile?.readText() ?: generateSequence(::readLine).joinToString("\n")
        val jsonString = jsonMapper.writeValueAsString(yamlMapper.readTree(input))
        val hoconString = prettyPrint(jsonString)
        val out = outputFile?.outputStream() ?: System.out
        out.use { it.bufferedWriter().use { w -> w.write(hoconString) } }
    }
}

fun main(args: Array<String>) {
    X2K().main(args)
}

val yamlMapper = ObjectMapper(YAMLFactory()).registerKotlinModule()
val jsonMapper = ObjectMapper()

val options: ConfigRenderOptions = ConfigRenderOptions
    .defaults()
    .setJson(false)
    .setOriginComments(false)
    .setComments(false)
    .setFormatted(true)

fun prettyPrint(input: String): String {
    val config = ConfigFactory.parseString(input)
    return config.root().render(options)
}

val gitHash = GVersion.GIT_SHA.take(6)
