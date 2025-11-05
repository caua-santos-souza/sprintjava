#!/bin/bash
set -e

RM=SEU_RM_OU_ID_AQUI
TAG=latest
LOCATION=brazilsouth
RG=rg-cp4-rm${RM}
ACR=acrcp4rm${RM}
APP_NAME=trackyard
ENVIRONMENT=trackyard-env
APP_IMG=$ACR.azurecr.io/appcp4:${TAG}

# Configurações do Oracle (credenciais da faculdade)
ORACLE_USER=${ORACLE_USER:-"SEU_USUARIO_AQUI"}
ORACLE_PASSWORD=${ORACLE_PASSWORD:-"SUA_SENHA_AQUI"}
ORACLE_HOST=${ORACLE_HOST:-"oracle.fiap.com.br"}
ORACLE_PORT=${ORACLE_PORT:-"1521"}
ORACLE_SID=${ORACLE_SID:-"orcl"}

echo "🚀 Iniciando deploy completo da aplicação TrackYard na Azure..."
echo "📋 Configurações:"
echo "   - RM: $RM"
echo "   - Location: $LOCATION (Brasil)"
echo "   - Resource Group: $RG"
echo "   - ACR: $ACR"
echo "   - App Name: $APP_NAME"
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
az group create --name $RG --location $LOCATION --output table
az acr create --resource-group $RG --name $ACR --sku Basic --admin-enabled true --location $LOCATION --output table

# Verificar se Azure Container Apps extension está instalada
echo "==> Verificando extensão Azure Container Apps..."
if ! az extension show --name containerapp > /dev/null 2>&1; then
    echo "Instalando extensão Azure Container Apps..."
    az extension add --name containerapp
fi

# Registrar provider necessário
echo "==> Registrando providers Azure..."
az provider register --namespace Microsoft.App --wait
az provider register --namespace Microsoft.OperationalInsights --wait

# Criar Log Analytics Workspace (necessário para Container Apps)
LOG_ANALYTICS_WORKSPACE="${APP_NAME}-logs"
echo "==> Verificando Log Analytics Workspace..."
if az monitor log-analytics workspace show --resource-group $RG --workspace-name $LOG_ANALYTICS_WORKSPACE > /dev/null 2>&1; then
    echo "✅ Log Analytics Workspace já existe"
else
    echo "==> Criando Log Analytics Workspace..."
    az monitor log-analytics workspace create \
      --resource-group $RG \
      --workspace-name $LOG_ANALYTICS_WORKSPACE \
      --location $LOCATION \
      --output none
    echo "✅ Log Analytics Workspace criado"
fi

# Obter workspace ID e shared key
echo "==> Obtendo credenciais do Log Analytics..."
LOG_ANALYTICS_WORKSPACE_ID=$(az monitor log-analytics workspace show \
  --resource-group $RG \
  --workspace-name $LOG_ANALYTICS_WORKSPACE \
  --query customerId -o tsv)
LOG_ANALYTICS_SHARED_KEY=$(az monitor log-analytics workspace get-shared-keys \
  --resource-group $RG \
  --workspace-name $LOG_ANALYTICS_WORKSPACE \
  --query primarySharedKey -o tsv)

# Criar Container Apps Environment
echo "==> Verificando Container Apps Environment..."
if az containerapp env show --name $ENVIRONMENT --resource-group $RG > /dev/null 2>&1; then
    echo "✅ Container Apps Environment já existe"
else
    echo "==> Criando Container Apps Environment..."
    az containerapp env create \
      --name $ENVIRONMENT \
      --resource-group $RG \
      --location $LOCATION \
      --logs-workspace-id $LOG_ANALYTICS_WORKSPACE_ID \
      --logs-workspace-key $LOG_ANALYTICS_SHARED_KEY \
      --output table
    echo "✅ Container Apps Environment criado"
fi

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

# 6. Limpar app antigo (se existir)
echo ""
echo "==> 5. Verificando app existente..."
az containerapp delete --name $APP_NAME --resource-group $RG --yes 2>/dev/null || true
echo "✅ Limpeza concluída"

# 7. Obter credenciais do ACR
echo ""
echo "==> 6. Obtendo credenciais do ACR..."
ACR_USER=$(az acr credential show --name $ACR --query "username" -o tsv)
ACR_PASS=$(az acr credential show --name $ACR --query "passwords[0].value" -o tsv)
echo "✅ Credenciais obtidas"

# 8. Deploy da aplicação no Container Apps
echo ""
echo "==> 7. Fazendo deploy da aplicação no Azure Container Apps..."
az containerapp create \
  --name $APP_NAME \
  --resource-group $RG \
  --environment $ENVIRONMENT \
  --image $APP_IMG \
  --registry-server $ACR.azurecr.io \
  --registry-username $ACR_USER \
  --registry-password $ACR_PASS \
  --target-port 8080 \
  --ingress external \
  --cpu 1.0 \
  --memory 2.0Gi \
  --min-replicas 1 \
  --max-replicas 1 \
  --env-vars \
    DB_USER=$ORACLE_USER \
    DB_PASSWORD=$ORACLE_PASSWORD \
    ORACLE_HOST=$ORACLE_HOST \
    ORACLE_PORT=$ORACLE_PORT \
    ORACLE_SID=$ORACLE_SID \
  --output table

# 9. Obter URL fixa da aplicação
echo ""
echo "==> 8. Obtendo URL fixa da aplicação..."
APP_URL=$(az containerapp show --name $APP_NAME --resource-group $RG --query properties.configuration.ingress.fqdn -o tsv)

# 10. Aguardar inicialização
echo ""
echo "==> 9. Aguardando inicialização da aplicação..."
sleep 30

# 11. Testar aplicação
echo ""
echo "==> 10. Testando aplicação..."
sleep 15
if curl -s -f "https://$APP_URL/api/patios" > /dev/null; then
    echo "✅ Aplicação funcionando!"
else
    echo "⚠️  Aplicação pode estar inicializando ainda..."
    echo "   Aguarde alguns minutos e tente novamente."
fi

# 12. Mostrar resultados
echo ""
echo "🎉 DEPLOY CONCLUÍDO COM SUCESSO!"
echo "=================================="
echo "🌐 URL FIXA da aplicação (não muda ao reiniciar):"
echo "   https://$APP_URL"
echo ""
echo "📋 Rotas disponíveis:"
echo "   - Pátios: https://$APP_URL/api/patios"
echo "   - Motos: https://$APP_URL/api/motos"
echo "   - Movimentações: https://$APP_URL/api/movimentacoes"
echo "   - Pontos de Leitura: https://$APP_URL/api/pontos-leitura"
echo "   - Enums: https://$APP_URL/api/enums"
echo ""
echo "🔧 Comandos úteis:"
echo "   - Ver logs: az containerapp logs show --name $APP_NAME --resource-group $RG --follow"
echo "   - Ver status: az containerapp show --name $APP_NAME --resource-group $RG --query properties.runningStatus"
echo "   - Ver revisões: az containerapp revision list --name $APP_NAME --resource-group $RG"
echo "   - Deletar: az containerapp delete --name $APP_NAME --resource-group $RG --yes"
echo ""
echo "📝 Configuração Oracle:"
echo "   - Host: $ORACLE_HOST:$ORACLE_PORT/$ORACLE_SID"
echo "   - Usuário: $ORACLE_USER"
echo "   - Senha: $ORACLE_PASSWORD"
