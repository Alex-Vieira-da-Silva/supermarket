@echo off
echo ============================================
echo  INICIANDO DEPLOY PARA EC2-APP
echo ============================================

echo.
echo 🔨 Compilando projeto...
mvn clean package -DskipTests

echo.
echo 📦 Enviando JAR para EC2...
scp -i "C:\Users\alexv\Downloads\Projeto\Projeto no IntelliJ\Key_Projeto.pem" ^
target\supermarket-0.0.1-SNAPSHOT.jar ^
ec2-user@34.234.225.183:/home/ec2-user/

echo.
echo 📄 Enviando Dockerfile para EC2...
scp -i "C:\Users\alexv\Downloads\Projeto\Projeto no IntelliJ\Key_Projeto.pem" ^
Dockerfile ^
ec2-user@34.234.225.183:/home/ec2-user/

echo.
echo 🚀 Conectando na EC2 para atualizar container...
ssh -i "C:\Users\alexv\Downloads\Projeto\Projeto no IntelliJ\Key_Projeto.pem" ec2-user@34.234.225.183 ^
"docker rm -f supermarket 2>/dev/null; \
 docker build -t supermarket .; \
 docker run -d -p 8080:8080 --restart always --name supermarket supermarket"

echo.
echo ============================================
echo  DEPLOY FINALIZADO COM SUCESSO
echo ============================================
pause