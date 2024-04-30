FROM gradle:8.6.0-jdk21-graal AS GRADLE_CACHE

RUN mkdir -p /home/gradle/cache_home

ENV GRADLE_USER_HOME /home/gradle/cache_home
COPY build.gradle /home/gradle/java-code/
COPY settings.gradle /home/gradle/java-code/

WORKDIR /home/gradle/java-code
RUN gradle clean build -i --stacktrace

FROM gradle:8.6.0-jdk21-graal AS GRADLE_BUILD

COPY --from=GRADLE_CACHE /home/gradle/cache_home /home/gradle/.gradle
COPY . /usr/src/code
WORKDIR /usr/src/code

USER root
RUN gradle bootJar -i --stacktrace

FROM ghcr.io/graalvm/graalvm-community:21

COPY --from=GRADLE_BUILD /usr/src/code/build/libs/*.jar /root/app/app.jar

# set the startup command to execute the jar
CMD ["java", "-jar", "/root/app/app.jar", "-Dlog4j2.contextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector"]