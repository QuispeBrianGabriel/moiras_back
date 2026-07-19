DOCKER_GID := $(shell stat -c '%g' /var/run/docker.sock)
DC := DOCKER_GID=$(DOCKER_GID) docker compose
SERVICE := backend

RESET   := \033[0m
BOLD    := \033[1m
RED     := \033[0;31m
GREEN   := \033[0;32m
YELLOW  := \033[0;33m
CYAN    := \033[0;36m
MAGENTA := \033[0;35m

.DEFAULT_GOAL := help
.PHONY: start run up stop clean clear remove reset nuke help

start: up
run: up
up:
	@printf "$(GREEN)$(BOLD)🚀 Levantando entorno...$(RESET)\n"
	@$(DC) up -d --build
	@printf "$(GREEN)✅ Entrando al contenedor$(RESET)\n"
	@$(DC) exec $(SERVICE) bash

stop:
	@printf "$(GREEN)🛑 Deteniendo entorno...$(RESET)\n"
	@$(DC) stop
	@printf "$(GREEN)✅ Contenedor detenido$(RESET)\n"

clean: clear
clear:
	@printf "$(YELLOW)$(BOLD)🧹 Limpiando el entorno...$(RESET)\n"
	@$(DC) down
	@printf "$(GREEN)✅ Contenedor removido$(RESET)\n"

remove:
	@printf "$(MAGENTA)$(BOLD)🗑️  Limpiando imágenes del entorno...$(RESET)\n"
	@$(DC) down --rmi local
	@printf "$(GREEN)✅ Imágenes eliminadas$(RESET)\n"

reset:
	@printf "$(MAGENTA)$(BOLD)🌀 Limpiando volumenes del entorno...$(RESET)\n"
	@$(DC) down --volumes
	@printf "$(GREEN)✅ Volumenes eliminados$(RESET)\n"

nuke:
	@printf "$(RED)$(BOLD)☢️  Destruyendo el entorno por completo...$(RESET)\n"
	@$(DC) down --rmi local --volumes --remove-orphans
	@printf "$(GREEN)✅ Entorno recién clonado$(RESET)\n"

help:
	@printf "\n$(BOLD)$(CYAN)🐳 Moiras Front — Entorno de desarrollo$(RESET)\n\n"
	@printf "  $(GREEN)up$(RESET)       🚀 Levanta el contenedor\n"
	@printf "  $(GREEN)start$(RESET)    🔻 Alias de $(GREEN)up$(RESET)\n"
	@printf "  $(GREEN)run$(RESET)      🔻 Alias de $(GREEN)up$(RESET)\n"
	@printf "  $(GREEN)stop$(RESET)     🛑 Detiene el contenedor\n"
	@printf "  $(YELLOW)clear$(RESET)    🧹 Elimina el contenedor\n"
	@printf "  $(YELLOW)clean$(RESET)    🔻 Alias de $(GREEN)clear$(RESET)\n"
	@printf "  $(MAGENTA)remove$(RESET)   🗑️  Elimina las imágenes\n"
	@printf "  $(MAGENTA)reset$(RESET)    🌀 Elimina los volúmenes\n"
	@printf "  $(RED)nuke$(RESET)     ☢️  Elimina TODO\n"
	@printf "  $(CYAN)help$(RESET)     ❓ Muestra esta ayuda\n"
	@printf "\n"
