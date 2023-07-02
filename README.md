# flink-examples

```sh
$ mvn archetype:generate \
    -DarchetypeGroupId=org.apache.flink \
    -DarchetypeArtifactId=flink-quickstart-java \
    -DarchetypeVersion=1.17.1 \
    -DgroupId=io.github.potterhe.streamingwithflink \
    -DartifactId=flink-examples \
    -Dversion=0.1 \
    -DinteractiveMode=false
```

## idea 里调试 WordCount

```shell
nc -l 9999
```

### mac m1

Open Module Settings -> Dependencies
    Add JARs or Directiries -> lib/flink-dist.1.17.jar
    Module SDK: zulu 1.8


./bin/flink run --app-config-file ./myapp.yaml -c io.github.potterhe.streamingwithflink.WordCount 

[应用程序参数处理](https://nightlies.apache.org/flink/flink-docs-master/zh/docs/dev/datastream/application_parameters/)