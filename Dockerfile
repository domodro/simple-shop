FROM openjdk:14

COPY ./build/libs/simple-shop-1.0-SNAPSHOT.jar /simple-shop/simple-shop.jar
WORKDIR /simple-shop

CMD java -Xmx512M -jar /simple-shop/simple-shop.jar