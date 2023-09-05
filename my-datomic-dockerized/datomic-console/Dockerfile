FROM openjdk:11 as base

ENV DATOMIC_VERSION 1.0.6735

RUN wget https://datomic-pro-downloads.s3.amazonaws.com/1.0.6735/datomic-pro-${DATOMIC_VERSION}.zip -qO /tmp/datomic.zip \
  && unzip /tmp/datomic.zip \
  && rm /tmp/datomic.zip \
  && mv /datomic-pro-${DATOMIC_VERSION} /datomic

WORKDIR /datomic

RUN cp config/samples/dev-transactor-template.properties transactor.properties

RUN sed "s/host=localhost/host=0.0.0.0/" -i transactor.properties
RUN sed "s/# storage-access=local/storage-access=remote/" -i transactor.properties
RUN sed "s/# storage-datomic-password=/storage-datomic-password=datomic/" -i transactor.properties
RUN sed "s/# storage-admin-password=/storage-admin-password=admin/" -i transactor.properties

CMD ["bin/console", "-p", "8080" ,"dev", "datomic:dev://datomic-db:4334/mbrainz-1968-1973?password=datomic"]

EXPOSE 8080