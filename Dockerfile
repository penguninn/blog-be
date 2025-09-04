FROM maven:3.9-amazoncorretto-21 AS build
WORKDIR /app

COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn -B -DskipTests dependency:go-offline

COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -B -DskipTests clean package

RUN java -Djarmode=layertools -jar target/*.jar extract

FROM amazoncorretto:21-alpine AS runtime
RUN apk add --no-cache tzdata

WORKDIR /app

COPY --from=build /app/dependencies/ ./lib/
COPY --from=build /app/snapshot-dependencies/ ./lib/
COPY --from=build /app/application/ ./

RUN addgroup -S app && adduser -S app -G app
USER app

ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75 -XX:+UseStringDeduplication"

ENTRYPOINT ["java","-cp","/app:/app/lib/*","com.daviddai.blog.BlogApplication"]
