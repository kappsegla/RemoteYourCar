FROM eclipse-temurin:17-jre-alpine
RUN adduser --system --group spring
USER spring:spring
#ARG DEPENDENCY=target/dependency
EXPOSE 8080
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
#HEALTHCHECK --start-period=30s --interval=5s --timeout=3s --retries=3 CMD curl --request GET http://localhost:8080/actuator/health || exit 1
#https://gist.github.com/radimih/e16c0e4d1b15f1ee2e77c420e35362ab
#COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
#COPY ${DEPENDENCY}/META-INF /app/META-INF
#COPY ${DEPENDENCY}/BOOT-INF/classes /app
#ENTRYPOINT ["java","-cp","app:app/lib/*","se.iths.remoteyourcar.RemoteYourCarApplication"]

#https://spring.io/guides/gs/spring-boot-docker/
#Run mvn package
#mkdir -p target/dependency
#cd target/dependency
#jar -xf ../*.jar
#cd back to project root
#docker build -t remotecar/spring-boot-docker .
