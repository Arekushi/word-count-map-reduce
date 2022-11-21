```sh
mvn clean install
```

<br>

```sh
docker exec -it namenode /bin/bash
```
<br>

```sh
hdfs dfs -ls /
```

<br>

```sh
hdfs dfs -mkdir -p /user/root
```

<br>

```sh
docker cp ./target/word-count-map-reduce-1.0-SNAPSHOT.jar namenode:/tmp/
```

<br>

```sh
docker cp ./src/main/resources/test/input-test.txt namenode:/tmp/
```

<br>

```sh
docker cp ./src/main/resources/data/ namenode:/tmp/data/
```

<br>

```sh
hdfs dfs -mkdir /user/root/input
```

<br>

```sh
hdfs dfs -put input-test.txt /user/root/input
```

<br>

```sh
hdfs dfs -put /tmp/data/ /user/root/input
```

<br>

```sh
hadoop jar /tmp/word-count-map-reduce-1.0-SNAPSHOT.jar input/data output
```

<br>

```sh
hdfs dfs -cat /user/root/output/part-r-00000 > /tmp/output.json
```

<br>

```sh
docker cp namenode:/tmp/output.json ./src/main/resources/result
```
