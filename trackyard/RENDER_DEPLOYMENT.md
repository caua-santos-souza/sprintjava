# Deploy no Render - TrackYard

## Configuração para Persistência de Dados

Este projeto está configurado para usar H2 com armazenamento persistente no Render.

### Arquivos de Configuração

1. **`application-render.properties`** - Configuração específica para o Render
2. **`render.yaml`** - Configuração do serviço no Render
3. **`scripts/init-db.sh`** - Script de inicialização do banco

### Passos para Deploy

#### 1. Configuração no Render Dashboard

1. Acesse o [Render Dashboard](https://dashboard.render.com)
2. Clique em "New +" → "Web Service"
3. Conecte seu repositório GitHub
4. Configure o serviço:

**Configurações Básicas:**
- **Name**: `trackyard`
- **Environment**: `Java`
- **Build Command**: `./mvnw clean package -DskipTests`
- **Start Command**: `java -Dspring.profiles.active=render -jar target/trackyard-0.0.1-SNAPSHOT.jar`

**Variáveis de Ambiente:**
- `SPRING_PROFILES_ACTIVE` = `render`
- `JAVA_OPTS` = `-Xmx512m -Xms256m`

#### 2. Configurar Volume Persistente

1. No dashboard do Render, vá para seu serviço
2. Clique em "Settings" → "Disks"
3. Adicione um novo disco:
   - **Name**: `trackyard-data`
   - **Mount Path**: `/persistent`
   - **Size**: `1 GB`

#### 3. Deploy

1. Clique em "Deploy" no dashboard
2. O Render irá:
   - Fazer build da aplicação
   - Montar o volume persistente
   - Iniciar a aplicação com o perfil `render`

### Verificação

Após o deploy, você pode acessar:
- **API**: `https://seu-app.onrender.com`
- **H2 Console**: `https://seu-app.onrender.com/h2-console`

### Dados Persistentes

Os dados do H2 serão salvos em `/persistent/data/trackyard-db.mv.db` e persistirão entre deploys.

### Troubleshooting

**Problema**: Dados perdidos após deploy
**Solução**: Verifique se o volume persistente está montado corretamente em `/persistent`

**Problema**: Erro de permissão
**Solução**: O script `init-db.sh` cria o diretório com as permissões corretas

**Problema**: Aplicação não inicia
**Solução**: Verifique os logs no dashboard do Render e confirme se o perfil `render` está ativo
