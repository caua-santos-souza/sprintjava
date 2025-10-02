## 👥 Identificação do Grupo

- Cauã Dos Santos Souza (RM559093)
- Luigi Berzaghi Hernandes Sespedes (RM555516)
- Guilherme Pelissari Feitosa (RM558445)

---

## 🗺️ Diagrama de Arquitetura

![Diagrama detalhado da solução](images/diagrama_detalhado.png)

---

## 🚀 Deploy na Nuvem Azure - Tutorial Completo

### 📋 Pré-requisitos
- Azure CLI instalado
- Docker instalado
- Git Bash (Windows) ou terminal Linux/Mac
- Conta Azure ativa
- Credenciais Oracle da faculdade

### 🔧 Configuração Inicial

1. **Clone o repositório:**
   ```bash
   git clone <url-do-repo>
   cd Sprint1Java
   ```

2. **Faça login no Azure:**
   ```bash
   az login
   ```

3. **Configure suas credenciais Oracle (opcional):**
   ```bash
   # Opção 1: Variáveis de ambiente
   export ORACLE_USER="rm559093"
   export ORACLE_PASSWORD="fiap"
   
   # Opção 2: Editar o arquivo deploy.sh (linhas 12-13)
   ```

### 🚀 Deploy Automático (Recomendado)

**Execute um único comando que faz tudo automaticamente:**

```bash
./deploy.sh
```

Este script executa automaticamente:
- ✅ Verifica login no Azure
- ✅ Cria Resource Group e ACR
- ✅ Faz build da aplicação Docker
- ✅ Faz push para Azure Container Registry
- ✅ Deploy da aplicação na Azure
- ✅ Testa se está funcionando
- ✅ Mostra URL e rotas disponíveis

### 🔧 Deploy Manual (Passo a Passo)

Se preferir executar cada etapa manualmente:

1. **Criar infraestrutura:**
   ```bash
   ./infra.sh
   ```

2. **Build da aplicação:**
   ```bash
   cd trackyard
   docker build -t appcp4:latest .
   ```

3. **Push para ACR:**
   ```bash
   az acr login --name acrcp4rm559093
   docker tag appcp4:latest acrcp4rm559093.azurecr.io/appcp4:latest
   docker push acrcp4rm559093.azurecr.io/appcp4:latest
   ```

4. **Deploy da aplicação:**
   ```bash
   cd ..
   $password = az acr credential show --name acrcp4rm559093 --query "passwords[0].value" -o tsv
   az container create --resource-group rg-cp4-rm559093 --name aci-app-cp4-rm559093 --image acrcp4rm559093.azurecr.io/appcp4:latest --registry-login-server acrcp4rm559093.azurecr.io --registry-username acrcp4rm559093 --registry-password $password --cpu 1 --memory 2 --os-type Linux --ports 8080 --ip-address public --restart-policy OnFailure --environment-variables DB_USER=rm559093 DB_PASSWORD=fiap ORACLE_HOST=oracle.fiap.com.br ORACLE_PORT=1521 ORACLE_SID=orcl
   ```

### 🌐 Acessando a Aplicação

Após o deploy, você receberá um IP público. Acesse:

- **URL Base**: `http://SEU_IP:8080`
- **API Pátios**: `http://SEU_IP:8080/api/patios`
- **API Motos**: `http://SEU_IP:8080/api/motos`
- **API Movimentações**: `http://SEU_IP:8080/api/movimentacoes`
- **API Pontos de Leitura**: `http://SEU_IP:8080/api/pontos-leitura`
- **API Enums**: `http://SEU_IP:8080/api/enums`

### 🔍 Comandos Úteis

```bash
# Ver IP da aplicação
az container show --resource-group rg-cp4-rm559093 --name aci-app-cp4-rm559093 --query "ipAddress.ip" -o tsv

# Ver logs da aplicação
az container logs --resource-group rg-cp4-rm559093 --name aci-app-cp4-rm559093

# Ver status do container
az container show --resource-group rg-cp4-rm559093 --name aci-app-cp4-rm559093 --query "containers[0].instanceView.currentState"

# Deletar aplicação
az container delete --resource-group rg-cp4-rm559093 --name aci-app-cp4-rm559093 --yes

# Deletar todos os recursos
az group delete --name rg-cp4-rm559093 --yes --no-wait
```

## 🗄️ Configuração do Banco

A aplicação está configurada para usar:
- **Oracle Database**: oracle.fiap.com.br:1521/orcl
- **Usuário**: rm559093
- **Senha**: fiap
- **Profile**: azure (Oracle)

### ⚠️ Troubleshooting

**Problema**: Erro de conexão Oracle
- Verifique se as credenciais estão corretas
- Confirme se o Oracle da faculdade está acessível

**Problema**: Container não inicia
- Verifique os logs: `az container logs --resource-group rg-cp4-rm559093 --name aci-app-cp4-rm559093`
- Confirme se o build foi bem-sucedido

**Problema**: API não responde
- Aguarde alguns minutos para inicialização
- Verifique se o IP está correto
- Teste com: `curl http://SEU_IP:8080/api/patios`

---

## 🧪 Como Validar o Banco

- A aplicação conecta ao Oracle da faculdade (oracle.fiap.com.br:1521/orcl).
- Use ferramentas como DBeaver, SQL Developer ou Oracle SQL Developer para conectar ao Oracle.
- As credenciais são configuradas no script `deploy.sh` ou via variáveis de ambiente.
- Para desenvolvimento local, a aplicação usa H2 (profile padrão).

---

## 🖼️ Prints de Funcionamento

![Tela da aplicação rodando 2](images/print2.png)
![Tela da aplicação rodando 3](images/print3.png)
![Tela da aplicação rodando 4](images/print4.png)
![Tela da aplicação rodando 5](images/print5.png)
![Tela da aplicação rodando 6](images/print6.png)

---

## 🧹 Limpeza dos Recursos

Para evitar custos, exclua tudo ao final:
```bash
az group delete --name rg-cp4-rm559093 --yes --no-wait
```

---

## ℹ️ Observações

- Todos os comandos devem ser executados via terminal.
- O Dockerfile, scripts e código estão nas pastas correspondentes.

---
