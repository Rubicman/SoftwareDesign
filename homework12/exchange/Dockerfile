FROM openjdk:17-oracle
COPY . /app
WORKDIR /app
RUN ./gradlew --no-daemon distTar
RUN tar -xf build/distributions/exchange.tar
EXPOSE 8081
CMD ["exchange/bin/exchange"]