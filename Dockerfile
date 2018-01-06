FROM openjdk:8-jre-alpine

RUN mkdir /app

WORKDIR /app

ADD ./api/target /app

EXPOSE 8083

CMD ["java", "-server", "-cp", "classes:dependency/*", "com.kumuluz.ee.EeApplication"]