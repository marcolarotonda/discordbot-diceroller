FROM amazoncorretto:17-alpine

ARG BASE_PATH=development/java/discordbot-diceroller/
ARG MVN_FOLDER_PATH=$BASE_PATH".mvn"
ARG MVN_CMD_PATH=$BASE_PATH"mvnw"
ARG POM_PATH=$BASE_PATH"pom.xml"
ARG SRC_PATH=$BASE_PATH"src"

WORKDIR /app
COPY .m2/settings.xml /root/.m2/
COPY $MVN_FOLDER_PATH ./.mvn
COPY $MVN_CMD_PATH $POM_PATH ./

RUN ./mvnw compile

COPY $SRC_PATH ./src

CMD ["./mvnw", "spring-boot:run"]