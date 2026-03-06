#!/bin/bash

echo "============================================"
echo " INICIANDO DEPLOY COMPLETO (DB + APP + LB)"
echo "============================================"

KEY="/home/ec2-user/Key_project.pem"

EC2_DB="ec2-user@10.0.132.158"
EC2_APP1="ec2-user@10.0.136.157"
EC2_APP2="ec2-user@10.0.138.135"
EC2_LB="ec2-user@10.0.3.208"

DIR_DB="/home/ec2-user/mysql"
DIR_APP="/home/ec2-user/supermarket"
DIR_LB="/home/ec2-user"

echo "============================================"
echo " CRIANDO DIRETÓRIOS REMOTOS"
echo "============================================"

ssh -i "$KEY" $EC2_DB "mkdir -p $DIR_DB"
ssh -i "$KEY" $EC2_APP1 "mkdir -p $DIR_APP"
ssh -i "$KEY" $EC2_APP2 "mkdir -p $DIR_APP"
ssh -i "$KEY" $EC2_LB "mkdir -p $DIR_LB"

echo "============================================"
echo " ENVIANDO docker-compose-db.yml PARA EC2-DB"
echo "============================================"

scp -i "$KEY" docker-compose-db.yml $EC2_DB:$DIR_DB/docker-compose.yml

ssh -i "$KEY" $EC2_DB "cd $DIR_DB && docker-compose down && docker-compose up -d"

echo "============================================"
echo " ENVIANDO ARQUIVOS PARA EC2-APP-JAVA-1"
echo "============================================"

scp -i "$KEY" supermarket-0.0.1-SNAPSHOT.jar $EC2_APP1:$DIR_APP/
scp -i "$KEY" Dockerfile $EC2_APP1:$DIR_APP/
scp -i "$KEY" docker-compose-app.yml $EC2_APP1:$DIR_APP/docker-compose.yml

ssh -i "$KEY" $EC2_APP1 "
    cd $DIR_APP &&
    docker-compose down || true &&
    docker-compose up -d --build
"

echo "============================================"
echo " ENVIANDO ARQUIVOS PARA EC2-APP-JAVA-2"
echo "============================================"

scp -i "$KEY" supermarket-0.0.1-SNAPSHOT.jar $EC2_APP2:$DIR_APP/
scp -i "$KEY" Dockerfile $EC2_APP2:$DIR_APP/
scp -i "$KEY" docker-compose-app.yml $EC2_APP2:$DIR_APP/docker-compose.yml

ssh -i "$KEY" $EC2_APP2 "
    cd $DIR_APP &&
    docker-compose down || true &&
    docker-compose up -d --build
"

echo "============================================"
echo " ENVIANDO nginx.conf PARA EC2-LB"
echo "============================================"

scp -i "$KEY" nginx.conf $EC2_LB:$DIR_LB/

ssh -i "$KEY" $EC2_LB "
    sudo cp /home/ec2-user/nginx.conf /etc/nginx/nginx.conf &&
    sudo systemctl restart nginx
"

echo "============================================"
echo " DEPLOY COMPLETO FINALIZADO COM SUCESSO"
echo "============================================"
