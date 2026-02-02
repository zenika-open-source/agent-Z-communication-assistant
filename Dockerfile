FROM eclipse-temurin:21-jre

WORKDIR /deployments

COPY target/quarkus-app/lib/ /deployments/lib/
COPY target/quarkus-app/*.jar /deployments/
COPY target/quarkus-app/app/ /deployments/app/
COPY target/quarkus-app/quarkus/ /deployments/quarkus/
COPY images/templates/ /deployments/images/templates/

EXPOSE 8080
EXPOSE 8501

# Create directories for generated content and uploads
RUN mkdir -p /deployments/generated /deployments/images/uploads

ENTRYPOINT ["java", "-Dquarkus.http.host=0.0.0.0", "-Djava.util.logging.manager=org.jboss.logmanager.LogManager", "-jar", "/deployments/quarkus-run.jar"]
CMD ["ui"]