# x2h

Command line utility to convert YAML to HOCON

## Usage examples:

- Read from stdin, write to stdout: just `x2h`
- Convert a file to stdout: `cat my-file.yaml | x2h`
- Convert using input file and output file `x2h -i in-file.yaml -o out-file.conf`

```
Options:
-i, --input=<path>   Optional input file containing yaml. If omitted, stdin
is used.
-o, --output=<path>  Optional output file. If omitted, stdout is used.
-h, --help           Show this message and exit
```

## Releases

You can always find the current release under [releases](https://github.com/sne11ius/x2h/releases).

## Build

### Basic

JDK 21 is required to build this.

```shell
# Create a runnable .jar file @ build/libs/x2h.jar
./gradlew build
```

### Native

You can use graalvm to create a native binary

```shell
native-image --no-fallback -jar build/libs/x2h.jar
```

### Compress

Since the generated binary is fairly large, you might want to compress it

```shell
upx --best x2h
```

## License

Licensed under the EUPL. See [LICENSE](./LICENSE) file.
