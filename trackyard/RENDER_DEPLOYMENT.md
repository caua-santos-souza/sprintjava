# Deploy no Render - TrackYard (Plano Gratuito)

## Configuração para H2 em Memória

Este projeto está configurado para usar H2 em memória no plano gratuito do Render, com dados de exemplo carregados a cada startup.

### Arquivos de Configuração

1. **`application-render.properties`** - Configuração específica para o Render
2. **`render.yaml`** - Configuração do serviço no Render
3. **`data.sql`** - Dados de exemplo carregados a cada startup
4. **`Dockerfile`** - Configuração Docker para deploy

### Passos para Deploy

#### 1. Configuração no Render Dashboard

1. Acesse o [Render Dashboard](https://dashboard.render.com)
2. Clique em "New +" → "Web Service"
3. Conecte seu repositório GitHub
4. Configure o serviço:

**Configurações Básicas:**
- **Name**: `trackyard`
- **Environment**: `Docker` (ou `Java` se aparecer)
- **Dockerfile Path**: `./Dockerfile` (se escolher Docker)
- **Build Command**: `./mvnw clean package -DskipTests` (se escolher Java)
- **Start Command**: `java -Dspring.profiles.active=render -jar target/trackyard-0.0.1-SNAPSHOT.jar` (se escolher Java)

**Variáveis de Ambiente:**
- `SPRING_PROFILES_ACTIVE` = `render`
- `JAVA_OPTS` = `-Xmx512m -Xms256m`

#### 2. Deploy

1. Clique em "Deploy" no dashboard
2. O Render irá:
   - Fazer build da aplicação usando Docker
   - Iniciar a aplicação com o perfil `render`
   - Carregar dados de exemplo automaticamente

### Verificação

Após o deploy, você pode acessar:
- **API**: `https://seu-app.onrender.com`
- **H2 Console**: `https://seu-app.onrender.com/h2-console`

### Dados de Exemplo

A aplicação carrega automaticamente dados de exemplo a cada startup:
- 3 Pátios (Central, Norte, Sul)
- 4 Pontos de Leitura
- 4 Motos com diferentes status
- 4 Movimentações de exemplo

### ⚠️ Limitações do Plano Gratuito

- **Dados não persistem** entre deploys (são recarregados a cada startup)
- **Aplicação "dorme"** após 15 minutos de inatividade
- **Primeira requisição** após dormir pode demorar alguns segundos

### Troubleshooting

**Problema**: Dados perdidos após deploy
**Solução**: Normal no plano gratuito - dados são recarregados automaticamente

**Problema**: Aplicação não inicia
**Solução**: Verifique os logs no dashboard do Render e confirme se o perfil `render` está ativo

**Problema**: Aplicação demora para responder
**Solução**: Normal no plano gratuito - aplicação "acorda" na primeira requisição
