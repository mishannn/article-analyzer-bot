# Этап 1: Сборка проекта
FROM gradle:8.5-jdk21 AS build

# ARG для памяти и флагов — можно задавать при сборке
ARG JVM_MAX_HEAP="2g"
ARG JVM_MIN_HEAP="512m"
ARG GRADLE_FLAGS="--no-daemon"

ENV GRADLE_OPTS="-Xmx${JVM_MAX_HEAP} -Xms${JVM_MIN_HEAP}"

# Указываем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем файлы build.gradle.kts и settings.gradle.kts
COPY build.gradle.kts settings.gradle.kts ./

# Копируем исходный код приложения
COPY src ./src

# Собираем проект
RUN gradle installDist $GRADLE_FLAGS

# Этап 2: Создание финального образа
FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build /app/build/install/article-analyzer-bot/ ./

ENTRYPOINT ["./bin/article-analyzer-bot"]