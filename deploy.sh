#!/bin/bash

DOCKER_HOST=nitro
DOCKER_LOCAL_STORAGE=/docker-storage

APP_NAME=springcrud
APP_VOLUME_DIR=$DOCKER_LOCAL_STORAGE/$APP_NAME
APP_LOGS_DIR=$APP_VOLUME_DIR/logs
APP_TCP_PORT=8500
APP_HOSTNAME=$APP_NAME
TOMCAT_CONTAINER_DIR=/opt/tomcat


if [[ $(hostname) != $DOCKER_HOST ]]; then
    echo -e "\n\n  ERRO: Servidor de implantação esperado: \"$DOCKER_HOST\". Servidor atual: \"$(hostname)\"\n\n"
	exit 0
fi

if [ ! -d "$DOCKER_LOCAL_STORAGE" ]; then
    echo -e "\n\n  ERRO: Diretório de armazenamento local do Docker não encontrado: \"$DOCKER_LOCAL_STORAGE\"\n\n"
	exit 0
fi

set -x \
&& sudo mkdir -p $APP_VOLUME_DIR \
&& sudo mkdir -p $APP_LOGS_DIR \
&& sudo chown -Rf 1000:1000 $DOCKER_LOCAL_STORAGE \
&& sudo chown -Rf 1000:1000 $DOCKER_LOCAL_STORAGE/* \
&& sudo docker rm -f $APP_NAME || true \
&& sudo docker rmi -f $APP_NAME || true \
&& sudo docker build -t $APP_NAME . \
&& sudo docker run --name $APP_NAME -h $APP_HOSTNAME -d -v $APP_LOGS_DIR:$TOMCAT_CONTAINER_DIR/logs --restart always -p $APP_TCP_PORT:8443 $APP_NAME \
&& echo -e "\n\n\n\n\n\n\n\n\n\n\n\n                                   Pressionar Ctrl+C para sair da exibição dos logs...\n\n\n" \
&& sudo docker logs -f $APP_NAME
