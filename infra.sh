#!/bin/bash
set -euo pipefail

# Defaults - override via ENV
RM="${RM:-559093}"
LOCATION="${LOCATION:-eastus}"
RG="${RG:-rg-cp4-rm${RM}}"
ACR="${ACR:-acrcp4rm${RM}}"

echo "==> Criando infraestrutura Azure"
echo "   - RM: $RM"
echo "   - Location: $LOCATION"
echo "   - Resource Group: $RG"
echo "   - ACR: $ACR"
echo ""

# 0. Verifica login az
if ! az account show > /dev/null 2>&1; then
  echo "❌ Você não está logado no Azure. Rode: az login"
  exit 1
fi
echo "✅ Logado no Azure"

# 1. Criar Resource Group
echo "==> Criando Resource Group: $RG..."
az group create --name "$RG" --location "$LOCATION" --output table
echo "✅ Resource Group criado (ou já existia)."

# 2. Criar ACR (Azure Container Registry)
echo "==> Criando ACR: $ACR..."
az acr create --resource-group "$RG" --name "$ACR" --sku Basic --admin-enabled true --location "$LOCATION" --output table
echo "✅ ACR criado"

# 3. Mostrar informações úteis
echo ""
echo "🔎 Informações geradas:"
echo "   RG=$RG"
echo "   ACR=$ACR"
echo "   ACR_LOGIN_SERVER=${ACR}.azurecr.io"
echo ""
echo "Pronto. Agora você pode:"
echo " - Buildar a imagem em trackyard/ e dar push para ${ACR}.azurecr.io"
echo " - Ou executar o ./deploy.sh que usa esses mesmos nomes"