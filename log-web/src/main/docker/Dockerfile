FROM 1024265676/ubuntu:2.0
ADD log-web-1.0-SNAPSHOT.jar app.jar
ENV configuri =""
ENTRYPOINT ["sh","-c","java -jar app.jar --spring.cloud.config.uri=$configuri"]
EXPOSE 9504
