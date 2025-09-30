#!/bin/bash
set -e

RM=559093
TAG=latest
RG=rg-cp4-rm${RM}
ACR=acrcp4rm${RM}
APP_CONTAINER=aci-app-cp4-rm${RM}
APP_IMG=$ACR.azurecr.io/appcp4:${TAG}

# Configurações do Oracle (credenciais da faculdade)
ORACLE_USER=${ORACLE_USER:-"SEU_USIUARIO_AQUI"}
ORACLE_PASSWORD=${ORACLE_PASSWORD:-"SUA_SENHA_AQUI"}
ORACLE_HOST=${ORACLE_HOST:-"oracle.fiap.com.br"}
ORACLE_PORT=${ORACLE_PORT:-"1521"}
ORACLE_SID=${ORACLE_SID:-"orcl"}

echo "🚀 Iniciando deploy completo da aplicação TrackYard na Azure..."
echo "📋 Configurações:"
echo "   - RM: $RM"
echo "   - Resource Group: $RG"
echo "   - ACR: $ACR"
echo "   - Oracle: $ORACLE_USER@$ORACLE_HOST:$ORACLE_PORT/$ORACLE_SID"
echo ""

# 1. Verificar login no Azure
echo "==> 1. Verificando login no Azure..."
if ! az account show > /dev/null 2>&1; then
    echo "❌ Não está logado no Azure. Execute: az login"
    exit 1
fi
echo "✅ Logado no Azure"

# 2. Criar infraestrutura
echo ""
echo "==> 2. Criando infraestrutura Azure..."
az group create --name $RG --location eastus --output table
az acr create --resource-group $RG --name $ACR --sku Basic --admin-enabled true --location eastus --output table
echo "✅ Infraestrutura criada"

# 3. Build da aplicação
echo ""
echo "==> 3. Fazendo build da aplicação..."
cd trackyard
echo "📦 Buildando imagem Docker..."
docker build -t appcp4:${TAG} .
echo "✅ Build concluído"

# 4. Push para ACR
echo ""
echo "==> 4. Fazendo push para Azure Container Registry..."
az acr login --name $ACR
docker tag appcp4:${TAG} $APP_IMG
docker push $APP_IMG
echo "✅ Push concluído"

# 5. Voltar para diretório raiz
cd ..

# 6. Limpar containers antigos
echo ""
echo "==> 5. Limpando containers antigos..."
az container delete --resource-group $RG --name $APP_CONTAINER --yes --no-wait 2>/dev/null || true
echo "✅ Limpeza concluída"

# 7. Obter credenciais do ACR
echo ""
echo "==> 6. Obtendo credenciais do ACR..."
ACR_USER=$(az acr credential show --name $ACR --query "username" -o tsv)
ACR_PASS=$(az acr credential show --name $ACR --query "passwords[0].value" -o tsv)
echo "✅ Credenciais obtidas"

# 8. Deploy da aplicação
echo ""
echo "==> 7. Fazendo deploy da aplicação..."
az container create \
  --resource-group $RG \
  --name $APP_CONTAINER \
  --image $APP_IMG \
  --registry-login-server $ACR.azurecr.io \
  --registry-username $ACR_USER \
  --registry-password $ACR_PASS \
  --cpu 1 \
  --memory 2 \
  --os-type Linux \
  --ports 8080 \
  --ip-address public \
  --restart-policy OnFailure \
  --environment-variables \
    DB_USER=$ORACLE_USER \
    DB_PASSWORD=$ORACLE_PASSWORD \
    ORACLE_HOST=$ORACLE_HOST \
    ORACLE_PORT=$ORACLE_PORT \
    ORACLE_SID=$ORACLE_SID \
  --output table

# 9. Obter IP da aplicação
echo ""
echo "==> 8. Obtendo IP da aplicação..."
APP_IP=$(az container show --resource-group $RG --name $APP_CONTAINER --query ipAddress.ip -o tsv)

# 10. Aguardar inicialização
echo ""
echo "==> 9. Aguardando inicialização da aplicação..."
sleep 30

# 11. Testar aplicação
echo ""
echo "==> 10. Testando aplicação..."
if curl -s -f "http://$APP_IP:8080/api/patios" > /dev/null; then
    echo "✅ Aplicação funcionando!"
else
    echo "⚠️  Aplicação pode estar inicializando ainda..."
fi

# 12. Mostrar resultados
echo ""
echo "🎉 DEPLOY CONCLUÍDO COM SUCESSO!"
echo "=================================="
echo "🌐 URL da aplicação: http://$APP_IP:8080"
echo "📋 Rotas disponíveis:"
echo "   - Pátios: http://$APP_IP:8080/api/patios"
echo "   - Motos: http://$APP_IP:8080/api/motos"
echo "   - Movimentações: http://$APP_IP:8080/api/movimentacoes"
echo "   - Pontos de Leitura: http://$APP_IP:8080/api/pontos-leitura"
echo "   - Enums: http://$APP_IP:8080/api/enums"
echo ""
echo "🔧 Comandos úteis:"
echo "   - Ver logs: az container logs --resource-group $RG --name $APP_CONTAINER"
echo "   - Ver status: az container show --resource-group $RG --name $APP_CONTAINER --query 'containers[0].instanceView.currentState'"
echo "   - Deletar: az container delete --resource-group $RG --name $APP_CONTAINER --yes"
echo ""
echo "📝 Configuração Oracle:"
echo "   - Host: $ORACLE_HOST:$ORACLE_PORT/$ORACLE_SID"
echo "   - Usuário: $ORACLE_USER"
echo "   - Senha: $ORACLE_PASSWORD"
