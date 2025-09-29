#!/bin/bash

# Script de inicialização do banco de dados
# Cria o diretório persistente se não existir

echo "Inicializando banco de dados H2..."

# Criar diretório para dados persistentes
mkdir -p /persistent/data

# Definir permissões adequadas
chmod 755 /persistent/data

echo "Diretório de dados criado: /persistent/data"
echo "Banco de dados H2 configurado para persistência"
