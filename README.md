# TrackYard - Sistema de Gerenciamento de Pátios

**TrackYard** é uma aplicação Java desenvolvida com o objetivo de gerenciar a organização das motos da Mottu dentro dos pátios, ajudando a evitar a perda inesperada de motos.

## 🚀 Funcionalidades

- **Gerenciamento de pátios** - CRUD completo de pátios
- **Gerenciamento de motos** - CRUD completo de motos com validações
- **Pontos de leitura** - Áreas específicas dentro dos pátios (8 tipos pré-definidos)
- **Movimentações** - Registro de onde cada moto está localizada
- **API REST** - Endpoints para integração com frontend
- **Validações** - Garantia de consistência dos dados
- **Deploy no Render** - Aplicação disponível na nuvem

## 🌐 API em Produção

A aplicação está disponível em: **https://trackyard.onrender.com**

### 🔗 Endpoints Principais:
- **Listar pátios**: `GET /api/patios`
- **Motos por pátio**: `GET /api/patios/{id}/motos`
- **Valores válidos**: `GET /api/enums/modelos-motos` e `GET /api/enums/pontos-leitura`

## Pré-requisitos

- **Java 17.** ou superior.
- **Maven**.
- Uma IDE.
- **Postman** ou outro cliente HTTP para testar os endpoints.

## Configuração e Execução

### 1. Clonar o Repositório

Clone o projeto para sua máquina local:

```bash
git clone https://github.com/LuigiBerzaghi/Sprint1Java.git
cd Sprint1Java/trackyard
```

### 2. Build e Execução

Compile e execute a aplicação usando Maven:

```bash
mvn clean install
mvn spring-boot:run
```

### 3. Acessar o Banco H2

O projeto usa o banco de dados H2 em memória. Acesse em:

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:trackyard`
- Username: `sa`
- Password: (deixe em branco)

## Estrutura do Projeto

- `com.mottu.trackyard.controller`: Contém os controladores REST.
- `com.mottu.trackyard.service`: Contém os serviços.
- `com.mottu.trackyard.entity`: Contém as entidades modelo.
- `com.mottu.trackyard.repository`: Contém os repositórios JPA.
- `com.mottu.trackyard.dto`: Contém os DTOs para transferência de dados.
- `com.mottu.trackyard.exception`: Contém a classe que centraliza o tratamento de exceções.

## 📚 Documentação da API

### 🏢 **Pátios** (`/api/patios`)

| Método | Endpoint | Descrição | Request Body | Response |
|--------|----------|-----------|--------------|----------|
| **GET** | `/api/patios` | Lista todos os pátios (paginado) | - | `200 OK` - Lista de pátios |
| **GET** | `/api/patios/{id}` | Busca pátio por ID | - | `200 OK` - Dados do pátio |
| **GET** | `/api/patios/{id}/motos` | **🎯 Pátio com motos e pontos** | - | `200 OK` - Pátio + motos + pontos |
| **POST** | `/api/patios` | Cria novo pátio | `{"idPatio": 1, "nome": "Pátio Central", "telefone": "(11)99999-9999", "endereco": "Rua das Flores, 123"}` | `201 Created` |
| **PUT** | `/api/patios/{id}` | Atualiza pátio | `{"idPatio": 1, "nome": "Pátio Central", "telefone": "(11)99999-9999", "endereco": "Rua das Flores, 123"}` | `200 OK` |
| **DELETE** | `/api/patios/{id}` | Deleta pátio | - | `200 OK` - Mensagem de sucesso |

### 🏍️ **Motos** (`/api/motos`)

| Método | Endpoint | Descrição | Request Body | Response |
|--------|----------|-----------|--------------|----------|
| **GET** | `/api/motos` | Lista todas as motos (paginado) | - | `200 OK` - Lista de motos |
| **GET** | `/api/motos/{id}` | Busca moto por ID | - | `200 OK` - Dados da moto |
| **GET** | `/api/motos/placa/{placa}` | Busca moto por placa | - | `200 OK` - Dados da moto |
| **GET** | `/api/motos/{id}/historico` | Histórico de movimentações | - | `200 OK` - Lista de movimentações |
| **POST** | `/api/motos` | Cria nova moto | `{"idMoto": "MOTO001", "modelo": "Pop", "placa": "ABC-1234"}` | `201 Created` |
| **PUT** | `/api/motos/{id}` | Atualiza moto | `{"idMoto": "MOTO001", "modelo": "Sport", "placa": "ABC-1234"}` | `200 OK` |
| **DELETE** | `/api/motos/{id}` | Deleta moto | - | `200 OK` - Mensagem de sucesso |

### 📍 **Pontos de Leitura** (`/api/pontos-leitura`)

| Método | Endpoint | Descrição | Request Body | Response |
|--------|----------|-----------|--------------|----------|
| **GET** | `/api/pontos-leitura` | Lista pontos (paginado) | - | `200 OK` - Lista de pontos |
| **GET** | `/api/pontos-leitura/{id}` | Busca ponto por ID | - | `200 OK` - Dados do ponto |
| **POST** | `/api/pontos-leitura` | Cria novo ponto | `{"idPonto": 1, "idPatio": 1, "nomePonto": "pendência", "descricao": "Área de pendências"}` | `201 Created` |
| **PUT** | `/api/pontos-leitura/{id}` | Atualiza ponto | `{"idPonto": 1, "idPatio": 1, "nomePonto": "reparos simples", "descricao": "Área de reparos"}` | `200 OK` |
| **DELETE** | `/api/pontos-leitura/{id}` | Deleta ponto | - | `200 OK` - Mensagem de sucesso |

### 🚛 **Movimentações** (`/api/movimentacoes`)

| Método | Endpoint | Descrição | Request Body | Response |
|--------|----------|-----------|--------------|----------|
| **GET** | `/api/movimentacoes/{id}` | Busca movimentação por ID | - | `200 OK` - Dados da movimentação |
| **POST** | `/api/movimentacoes` | Registra movimentação | `{"idMoto": "MOTO001", "idPonto": 1}` | `201 Created` |

### 🔧 **Enums/Valores Válidos** (`/api/enums`)

| Método | Endpoint | Descrição | Response |
|--------|----------|-----------|----------|
| **GET** | `/api/enums/modelos-motos` | Lista modelos válidos | `["Pop", "Sport", "E"]` |
| **GET** | `/api/enums/pontos-leitura` | Lista pontos válidos | `["defeito motor", "dano estrutural", "minha mottu", "agendamento", "pendência", "reparos simples", "para alugar", "sem placa"]` |

## 🎯 **Exemplo de Uso para Frontend**

### 1. **Listar Pátios**
```javascript
const response = await fetch('https://trackyard.onrender.com/api/patios');
const data = await response.json();
console.log(data.content); // Lista de pátios
```

### 2. **Buscar Motos de um Pátio**
```javascript
const response = await fetch('https://trackyard.onrender.com/api/patios/1/motos');
const patioComMotos = await response.json();
console.log(patioComMotos.motos); // Lista de motos com pontos
```

### 3. **Obter Valores Válidos**
```javascript
// Modelos de motos
const modelos = await fetch('https://trackyard.onrender.com/api/enums/modelos-motos')
  .then(r => r.json());

// Pontos de leitura
const pontos = await fetch('https://trackyard.onrender.com/api/enums/pontos-leitura')
  .then(r => r.json());
```

## ⚠️ **Validações**

- **Modelos de motos**: Apenas `Pop`, `Sport`, `E`
- **Pontos de leitura**: Apenas os 8 tipos pré-definidos
- **Campos obrigatórios**: Todos os campos marcados com `@NotBlank` ou `@NotNull`

## 🧪 **Testes e Validação**

### **Teste Online (Recomendado)**
A API está disponível em produção, você pode testar diretamente:

```bash
# Listar pátios
curl https://trackyard.onrender.com/api/patios

# Buscar motos do pátio 1
curl https://trackyard.onrender.com/api/patios/1/motos

# Obter valores válidos
curl https://trackyard.onrender.com/api/enums/modelos-motos
curl https://trackyard.onrender.com/api/enums/pontos-leitura
```

### **Teste Local**
Para testar localmente:

1. **Execute a aplicação**:
   ```bash
   mvn spring-boot:run
   ```

2. **Acesse o H2 Console**:
   - URL: `http://localhost:8080/h2-console`
   - JDBC URL: `jdbc:h2:mem:trackyard`
   - Username: `sa`
   - Password: (deixe em branco)

3. **Teste com Postman**:
   - Base URL: `http://localhost:8080`
   - Coleção disponível: [TrackYard API Tests](https://bold-zodiac-707210.postman.co/workspace/Personal-Workspace~4701d561-f092-46f6-a63c-0560d2fd1507/collection/39387306-06cd5d63-7cab-4aaf-9c69-e5983de04042?action=share&creator=39387306)

## 🚀 **Deploy no Render**

A aplicação está configurada para deploy automático no Render:

- **URL de Produção**: `https://trackyard.onrender.com`
- **Banco de Dados**: H2 em memória (dados recarregados a cada startup)
- **Configuração**: Docker com Java 17
- **Plano**: Gratuito (aplicação "dorme" após 15 min de inatividade)

## Integrantes

- RM555516 - Luigi Berzaghi Hernandes Sespedes
- RM559093 - Cauã Dos Santos Souza
- RM558445 - Guilherme Pelissari Feitosa
