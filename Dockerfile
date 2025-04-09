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

ENV MONGO_USER=root \
    MONGO_PASSWORD=1234 \
    MONGO_HOST=mongodb \
    MONGO_PORT=27017 \
    MONGO_DATABASE=blog \
    MONGO_URI=mongodb+srv://admin:admin123@guide-cluster.ewwjg.mongodb.net/?retryWrites=true&w=majority&appName=Guide-Cluster \
    JWT_SECRET=d50d1cab322c6f8ba9d82e8765c5e47b0dde58f4b288b24d07c05eca4458e87e40ef773a02434725d020f35eff4a33e0c83c4addd80ec5a7c7a9239b8d137a59 \
    JWT_EXPIRATION=86400000 \
    JWT_REFRESH_EXPIRATION=604800000

EXPOSE 8080
ENTRYPOINT ["java", "-cp", "/app:/app/lib/*", "com.daviddai.blog.BlogApplication"]