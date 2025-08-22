FROM maven:3.9-amazoncorretto-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../blog-0.0.1-SNAPSHOT.jar)

FROM amazoncorretto:21-alpine as runtime
RUN apk add --no-cache tzdata

WORKDIR /app
COPY --from=build /app/target/dependency/BOOT-INF/lib /app/lib
COPY --from=build /app/target/dependency/META-INF /app/META-INF
COPY --from=build /app/target/dependency/BOOT-INF/classes /app

EXPOSE 8080
ENTRYPOINT ["java", "-cp", "/app:/app/lib/*", "com.daviddai.blog.BlogApplication"]