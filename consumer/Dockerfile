FROM adoptopenjdk:16-jre-hotspot
RUN mkdir /opt/app
WORKDIR /opt/app
COPY target/db-writer.jar /opt/app
COPY client.keystore.p12 /opt/app
COPY client.truststore.jks /opt/app
ENTRYPOINT ["java", "-jar", "/opt/app/db-writer.jar"]