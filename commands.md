```
mvn clean install
```

<br>

```
docker exec -it namenode /bin/bash
```
<br>

```
hdfs dfs -ls /
```

<br>

```
hdfs dfs -mkdir -p /user/root
```

<br>

```
docker cp ./target/word-count-map-reduce-1.0-SNAPSHOT.jar namenode:/tmp/
```

<br>

```
docker cp ./src/main/resources/test/input-test.txt namenode:/tmp/
```

<br>

```
docker cp ./src/main/resources/data/ namenode:/tmp/data/
```

<br>

```
hdfs dfs -mkdir /user/root/input
```

<br>

```
hdfs dfs -put input-test.txt /user/root/input
```

<br>

```
hdfs dfs -put /tmp/data/* /user/root/input
```

<br>

```
hadoop jar /tmp/word-count-map-reduce-1.0-SNAPSHOT.jar input/data output
```

<br>

```
hdfs dfs -cat /user/root/output/part-r-00000 > /tmp/output.json
```

<br>

```
docker cp namenode:/tmp/output.json ./src/main/resources/result
```
