@echo off
sqlite3 WsLocaPart.db ".read sqlite_script.sql"
echo Données chargées dans la BD 