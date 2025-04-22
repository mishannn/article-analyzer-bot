# Этап 1: Сборка проекта
FROM gradle:8.5-jdk21 AS build

# Указываем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем файлы build.gradle.kts и settings.gradle.kts
COPY build.gradle.kts settings.gradle.kts ./

# Копируем исходный код приложения
COPY src ./src

# Собираем проект
RUN gradle shadowJar --no-daemon

# Этап 2: Создание финального образа
FROM openjdk:21-jdk-slim

# Указываем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем собранный JAR-файл из первого этапа
COPY --from=build /app/build/libs/*.jar app.jar

# Команда для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]