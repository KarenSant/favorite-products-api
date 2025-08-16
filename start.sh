#!/bin/bash

# ==========================
# Script para iniciar a aplicação
# ==========================

# Verifica se o arquivo .env existe e carrega as variáveis
if [ -f .env ]; then
  echo "Carregando variáveis do arquivo .env..."
  export $(grep -v '^#' .env | xargs)
else
  echo "Erro: Arquivo .env não encontrado."
  echo "Por favor, copie .env.example para .env e configure as variáveis necessárias."
  exit 1
fi

# Inicia a aplicação com o Maven Wrapper
echo "Iniciando a aplicação..."
./mvnw spring-boot:run

# Verifica se o comando anterior foi executado com sucesso
if [ $? -ne 0 ]; then
  echo "Erro ao iniciar a aplicação. Verifique os logs para mais detalhes."
  exit 1
fi

echo "Aplicação iniciada com sucesso."