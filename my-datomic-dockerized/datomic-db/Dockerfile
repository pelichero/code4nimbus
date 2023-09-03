FROM openjdk:11

COPY .. /datomic

EXPOSE 4334 4335 4336

CMD ["/datomic/datomic-pro-1.0.6735/bin/transactor config/samples/dev-transactor-template.properties"]