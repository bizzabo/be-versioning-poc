FROM openjdk:11-jdk-stretch

ARG SRC_BUILD_DIR=build/libs/*

ENV PROFILE=dev
ENV SERVICE_NAME=springexample
ENV APP_HOME=/usr/local/lib/bizzabo/${SERVICE_NAME}
ENV JAVA_OPTS="$JAVA_OPTS -Dreactor.netty.http.server.accessLogEnabled=true"

RUN groupadd -r app && useradd -r -g app app

RUN mkdir -p ${APP_HOME}
WORKDIR ${APP_HOME}

COPY --chown=app:app ${SRC_BUILD_DIR} ${APP_HOME}/
USER app

ENTRYPOINT ["./springexample.jar"]
CMD ["--release=${RELEASE}", "--spring.profiles.active=${PROFILE}"]
