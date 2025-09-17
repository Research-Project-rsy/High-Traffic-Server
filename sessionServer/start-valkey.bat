@echo off
echo Starting Valkey Session Cluster (Primary + 2 Replicas)...

REM Docker Compose 실행
docker-compose -f docker-compose.yml up -d

echo Waiting 5 seconds for services to start...
timeout /t 5 /nobreak > nul

REM 상태 확인
docker-compose ps

echo Valkey cluster started!
pause
