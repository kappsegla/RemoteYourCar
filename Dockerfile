FROM adoptopenjdk:16-jre
RUN adduser --system --group spring
USER spring:spring
ARG DEPENDENCY=target/dependency
EXPOSE 8080
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","se.iths.remoteyourcar.RemoteYourCarApplication"]

#https://spring.io/guides/gs/spring-boot-docker/
#Run mvn package
#mkdir -p target/dependency
#cd target/dependency
#jar -xf ../*.jar
#cd back to project root
#docker build -t remotecar/spring-boot-docker .