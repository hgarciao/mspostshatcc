FROM openjdk:8-jre-alpine

ENV JHIPSTER_SLEEP 0

ENV PROFILE dev

ADD ./target/*.war /app.war

RUN sh -c 'touch /app.war'
EXPOSE 8081
CMD echo "The application will start in ${JHIPSTER_SLEEP}s..." && \
    sleep ${JHIPSTER_SLEEP} && \
    java -Djava.security.egd=file:/dev/./urandom -jar /app.war --spring.profiles.active=${PROFILE}