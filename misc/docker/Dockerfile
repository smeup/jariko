FROM openjdk:8-alpine

ENV pgmDir /opt/rpgJavaInterpreter

RUN mkdir -p ${pgmDir}

COPY rpgJavaInterpreter-core/build/libs/rpgJavaInterpreter-core-all.jar ${pgmDir}
COPY rpgJavaInterpreter-core/src/test/resources/HELLO.rpgle ${pgmDir}
COPY rpgJavaInterpreter-core/src/test/resources/CALCFIB.rpgle ${pgmDir}

WORKDIR ${pgmDir}

ENTRYPOINT ["java", "-jar",  "rpgJavaInterpreter-core-all.jar"]