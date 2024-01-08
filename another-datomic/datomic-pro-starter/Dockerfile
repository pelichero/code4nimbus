FROM pointslope/datomic-pro-starter:0.9.5561

CMD ["config/dev-transactor.properties"]

COPY . /data

ADD restore-db.sh /usr/local/bin/restore-db