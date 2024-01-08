FROM clojure:tools-deps-1.11.1.1237-jammy
ARG datomic_version=1.0.6726

ENV DATOMIC_VERSION $datomic_version
ENV DATOMIC_HOME /opt/datomic-pro-$datomic_version
ENV DATOMIC_DATA $DATOMIC_HOME/data

RUN apt-get update && apt-get install -y unzip && apt-get install -y wget

RUN wget https://datomic-pro-downloads.s3.amazonaws.com/${DATOMIC_VERSION}/datomic-pro-${DATOMIC_VERSION}.zip -O /tmp/datomic.zip \
  && unzip /tmp/datomic.zip -d /opt \
  && rm -f /tmp/datomic.zip

WORKDIR $DATOMIC_HOME
RUN echo DATOMIC HOME: $DATOMIC_HOME
COPY dev-transactor.properties config/dev-transactor.properties
COPY start.sh start.sh
RUN chmod 777 start.sh
ENTRYPOINT ["./start.sh"]

VOLUME $DATOMIC_DATA

EXPOSE 4334 4335 4336 8080