<h1 align="center">
  Word Count Map Reduce
</h1>

<p align="center">
  <a href="#" target="blank">
    <img src="https://cdn.icon-icons.com/icons2/2699/PNG/512/apache_hadoop_logo_icon_169586.png" width="200" alt="Nest Logo" />
  </a>
</p>

<p align="center">
  Simple Word Count application with Map Reduce.
</p>

## About The Project
Project of the discipline `Arquitetura de Big Data e DW BI` of the 4º semester of the course of [`Technology in Big Data for Business`][big_data_course] at [`FATEC Ipiranga`][fatec_ipiranga]. Supervised by [`Antonio Guardado`](mailto:antonio.guardado01@fatec.sp.gov.br).

### Portuguese description
A ideia do projeto é realizar o processo de contagem de palavras, utilizando de um modelo de programação executado no Hadoop (MapReduce).
<br><br>
O MapReduce é um modelo de programação que permite o processamento de dados massivos em um algoritmo paralelo e distribuído,
geralmente em um cluster de computadores.
<br><br>
Link para a atividade que foi realizada: [atividade][atividade_url]


## Built With
- [Java 8][java8]
- [Docker v20.10.8][docker]
- [Apache Maven 3.8.6][maven]

## Getting Started
For the use of the project, some prerequisites will be necessary.

### Prerequisites (Windows)
* Java 8
  1. You can download here: [Java][java_url]
  2. Here is a step-by-step installation tutorial. [(Tutorial)][java_tutorial_url]
* Docker
  1. You can download here: [Docker][docker_url]
  2. Here is a step-by-step installation tutorial. [(Tutorial)][docker_tutorial_url]
* Maven
  1. You can download here: [Maven][maven_url]
  2. Here is a step-by-step installation tutorial. [(Tutorial)][maven_tutorial_url]

<br>

### Installation and usage
1. Clone the repository to create the containers
    ```sh
    git clone https://github.com/big-data-europe/docker-hadoop.git
    ```
2. Inside the repository folder, run:
    ```sh
    docker compose up -d
    ```
3. Copy all data files to `/tmp` container folder:
    ```sh
    docker cp ./src/main/resources/data/ namenode:/tmp/data/
    ```
4. Let's compile our project to create a `.jar` file
    ```sh
    mvn clean install
    ```
5. If all goes well, we will copy the `.jar` file to our container
    ```sh
    docker cp ./target/word-count-map-reduce-1.0-SNAPSHOT.jar namenode:/tmp/
    ```
6. Create a new terminal and run:
    ```sh
    docker exec -it namenode /bin/bash
    ```
7. Create `user/root` folder
    ```sh
    hdfs dfs -mkdir -p /user/root
    ```
8. Create `input` folder
    ```sh
    hdfs dfs -mkdir /user/root/input
    ```
9. Now put `data` files into `input` folder
    ```sh
    hdfs dfs -put /tmp/data/ /user/root/input
    ```
10. Execute `.jar` file and wait to complete the process
    ```sh
    hadoop jar /tmp/word-count-map-reduce-1.0-SNAPSHOT.jar input/data output
    ```
11. When finished, we will copy the content to a `.json` file
    ```sh
    hdfs dfs -cat /user/root/output/part-r-00000 > /tmp/output.json
    ```
12. Finally, let's exit the container's terminal
    ```sh
    exit
    ```
13. Let's copy the output.json file to our machine
    ```sh
    docker cp namenode:/tmp/output.json ./src/main/resources/result
    ```
14. Done, the whole process has been completed 🎉

<br>

## Roadmap
- [x] Create Docker Container
- [x] Create Map Java Class
  - [x] Split Line
  - [x] Remove Accent
  - [x] Put the words inside a single object
- [x] Create Reduce Java Class
  - [x] Sum repeated words
  - [x] Return JSON object
- [x] Copy data to Docker Container
- [x] Run

## Acknowledgments
* [Building Hadoop Cluster Using Docker | Apache Hadoop Cluster Using Docker Compose][docker_hadoop_tutorial_video]
* [Setting up Hadoop with Docker and using MapReduce framework][docker_hadoop_tutorial_article]

## Contributors
| [<div><img width=115 src="https://avatars.githubusercontent.com/u/54884313?v=4"><br><sub>Alexandre Ferreira de Lima</sub></div>][arekushi] |
| :---: |

<!-- [Build With] -->
[java8]: https://www.oracle.com/br/java/technologies/javase/javase8-archive-downloads.html
[docker]: https://docs.docker.com/engine/release-notes/
[maven]: https://maven.apache.org/download.cgi

<!-- [Some links] -->
[fatec_ipiranga]: https://fatecipiranga.edu.br/
[big_data_course]: https://fatecipiranga.edu.br/curso-superior-de-tecnologia-em-big-data-para-negocios/

[docker_url]: https://www.docker.com/products/docker-desktop/
[docker_tutorial_url]: https://runnable.com/docker/install-docker-on-windows-10
[maven_url]: https://maven.apache.org/download.cgi
[maven_tutorial_url]: https://phoenixnap.com/kb/install-maven-windows
[java_url]: https://www.oracle.com/br/java/technologies/javase/javase8-archive-downloads.html
[java_tutorial_url]: https://shaileshjha.com/step-by-step-how-to-download-and-install-java-se-jdk-8-on-windows-10/

[atividade_url]: https://arekushi.notion.site/Atividade-Map-Reduce-55e4e0e8037b4bcfadb8aa54c3dc51ee

<!-- Acknowledgments -->
[docker_hadoop_tutorial_video]: https://www.youtube.com/watch?v=dLTI2HN9Ejg
[docker_hadoop_tutorial_article]: https://medium.com/@guillermovc/setting-up-hadoop-with-docker-and-using-mapreduce-framework-c1cd125d4f7b

<!-- [Constributors] -->
[arekushi]: https://github.com/Arekushi
