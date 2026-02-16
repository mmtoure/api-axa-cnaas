# Build Stage
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

# Runtime stage
# Utilisation de 'jre' au lieu de 'jdk' pour réduire la taille de l'image (environ -200Mo)
FROM eclipse-temurin:17-jre-jammy
ARG PROFILE=dev
ARG APP_VERSION=1.0.0

WORKDIR /app
# Correction : on renomme explicitement le JAR généré en app.jar
COPY --from=build /app/target/*.jar app.jar

# On passe le profil Spring en variable d'environnement
ENV SPRING_PROFILES_ACTIVE=${PROFILE}

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]