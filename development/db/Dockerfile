FROM postgres:13-alpine

# Copy SQL for setting up words table and importing words
COPY lingo_words.sql /data/lingo_words.sql
COPY trainer_schema.sql /data/trainer_schema.sql

# Copy bash scripts to run when building
COPY create_users.sh /docker-entrypoint-initdb.d/01-create_users.sh
COPY create_dbs.sh /docker-entrypoint-initdb.d/02-create_dbs.sh
COPY import_words.sh /docker-entrypoint-initdb.d/03-import_words.sh
COPY insert_trainer_schema.sh /docker-entrypoint-initdb.d/04-insert_trainer_schema.sh

# Guarantee Linux file endings
RUN sed -i -e 's/\r$//' /docker-entrypoint-initdb.d/01-create_users.sh && \
    sed -i -e 's/\r$//' /docker-entrypoint-initdb.d/02-create_dbs.sh && \
    sed -i -e 's/\r$//' /docker-entrypoint-initdb.d/03-import_words.sh && \
    sed -i -e 's/\r$//' /docker-entrypoint-initdb.d/04-insert_trainer_schema.sh
