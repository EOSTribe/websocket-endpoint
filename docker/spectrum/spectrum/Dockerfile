FROM openjdk:8-jre-alpine
#RUN apt-get install git && \
#    git clone https://github.com/EOSTribe/websocket-endpoint && \
#    cd websocket-endpoint && \
#    git checkout  elastic-publisher && \
#    gradle clean build
COPY build/spectrum-0.1.0.jar /usr/app/
ENTRYPOINT ["java", "-jar", "/usr/app/spectrum-0.1.0.jar"]