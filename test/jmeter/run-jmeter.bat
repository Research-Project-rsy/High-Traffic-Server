@echo off
cd /d "%~dp0"
docker-compose run --rm jmeter %*
