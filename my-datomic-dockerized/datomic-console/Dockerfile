FROM openjdk:11

COPY .. /datomic

EXPOSE 8080

CMD ["/datomic/datomic-pro-1.0.6735/bin/console -p 8080 dev datomic:dev://database:4334/"]