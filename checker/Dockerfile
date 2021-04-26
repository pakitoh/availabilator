FROM adoptopenjdk:16-jre-hotspot
RUN mkdir /opt/app
WORKDIR /opt/app
COPY target/web-availability-checker.jar /opt/app
COPY client.keystore.p12 /opt/app
COPY client.truststore.jks /opt/app
ENTRYPOINT ["java", "-jar", "/opt/app/web-availability-checker.jar"]
CMD ["https://aiven.io"]