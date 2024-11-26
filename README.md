<p align="center"></p>

# InSight

## Descrição
InSight é uma aplicação que permite ao usuário acompanhar tópicos de interesse nas redes sociais,
sites de notícias e pesquisas no Google. A plataforma coleta, analisa e organiza informações 
relevantes e gera resumos inteligentes via LLM (Large Language Model), além de oferecer um 
áudio estilo podcast e newsletters diárias com as atualizações mais importantes.

## Tecnologias Utilizadas

- **Java**: Base da lógica do sistema.
- **Spring Boot**:  Framework para desenvolvimento e integração de serviços Java.
- **Python**: Suporte para tarefas de automação e IA.
- **PostgreSQL**: Banco de dados relacional.
- **Bun**: Runtime JavaScript rápido.
- **Vue.js**: Framework para interfaces interativas.
- **Node JS**: Ambiente para back-end escalável.
- **Pydantic**: Validação de dados estruturados em Python.
- **FastAPI**: Framework para criar APIs de alta performance.
- **OpenAI**: Plataforma de IA para geração de resumos.


## Regras de negócio do projeto
- **RN1**: Cadastro de usuário com nome, e-mail e senha.
- **RN2**: Configuração de tópicos de interesse e filtros personalizados.
- **RN3**: Coleta periódica de dados conforme preferências do usuário.
- **RN4**: Geração de resumos filtrados com LLM.
- **RN5**: Opção de converter resumos em áudio estilo podcast.
- **RN6**: Envio de newsletter diária com resumos dos tópicos escolhidos.
- **RN7**: Limite de dados e resumos diários por usuário.
- **RN8**: Autenticação obrigatória com criptografia de dados.
- **RN9**: Gerenciamento seguro de tópicos e preferências.
- **RN10**: Notificação ao usuário sobre erros de conexão ou atrasos.


## Instalação
### Pré-Requisitos
- **Java Development Kit (JDK)**: Certifique-se de ter a versão 11 ou superior.
- **IDE**: IntelliJ IDEA ou Eclipse.
- **Maven**: Para gerenciamento de dependências.
- **Python 3.x**: Para rodar scripts de suporte.
- **Node.js e npm**: Necessários para o front-end com Vue.js.
- **Vue CLI**: Instale com npm install -g @vue/cli.
- **PostgreSQL**: Instale e configure uma instância.
- **Git**: Para controle de versão e clonagem do repositório.
- **OpenAI**: Configure uma chave para acesso à API.
- **Ambiente Virtual de Python (opcional)**: Use venv para isolar dependências.

