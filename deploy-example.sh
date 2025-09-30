#!/bin/bash
# Exemplo de como configurar as credenciais Oracle antes do deploy

# Configure suas credenciais Oracle da faculdade
export ORACLE_USER="rm559093"
export ORACLE_PASSWORD="fiap"

# Opcional: configure outros parâmetros se necessário
export ORACLE_HOST="oracle.fiap.com.br"
export ORACLE_PORT="1521"
export ORACLE_SID="orcl"

# Execute o deploy
./deploy.sh
