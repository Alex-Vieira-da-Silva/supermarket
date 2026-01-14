@echo off
echo ============================================
echo INICIANDO DEPLOY NAS DUAS EC2
echo ============================================

set KEY="C:\Users\alexv\Downloads\Projeto\Projeto no Intellij\Key_Projeto.pem"
set EC2_1=ec2-user@34.234.225.183
set EC2_2=ec2-user@54.221.12.29

echo Compilando projeto...
mvn clean package -DskipTests

echo Enviando arquivos para EC2-APP-JAVA-1...
scp -i %KEY% target\supermarket-0.0.1-SNAPSHOT.jar %EC2_1%:/home/ec2-user/supermarket/
scp -i %KEY% Dockerfile %EC2_1%:/home/ec2-user/supermarket/
scp -i %KEY% docker-compose.yml %EC2_1%:/home/ec2-user/supermarket/

echo Atualizando container na EC2-APP-JAVA-1...
ssh -i %KEY% %EC2_1% "cd supermarket; docker-compose down; docker-compose up -d --build"

echo Enviando arquivos para EC2-APP-JAVA-2...
scp -i %KEY% target\supermarket-0.0.1-SNAPSHOT.jar %EC2_2%:/home/ec2-user/supermarket/
scp -i %KEY% Dockerfile %EC2_2%:/home/ec2-user/supermarket/
scp -i %KEY% docker-compose.yml %EC2_2%:/home/ec2-user/supermarket/

echo Atualizando container na EC2-APP-JAVA-2...
ssh -i %KEY% %EC2_2% "cd supermarket; docker-compose down; docker-compose up -d --build"

echo ============================================
echo DEPLOY FINALIZADO NAS DUAS INSTANCIAS
echo ============================================
pause