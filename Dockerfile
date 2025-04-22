FROM gradle:8.5-jdk17 AS build

# Копируем исходники
WORKDIR /app
COPY . .

# Собираем приложение (предполагается, что используется Gradle)
RUN gradle clean build -x test

# --- Stage 2: Minimal runtime image ---
FROM eclipse-temurin:17-jre

# Задаем рабочую директорию
WORKDIR /app

# Копируем собранный JAR из предыдущего stage
COPY --from=build /app/build/libs/*.jar app.jar

# Открываем порт (по необходимости)
EXPOSE 8080

# Команда запуска
ENTRYPOINT ["java", "-jar", "app.jar"]