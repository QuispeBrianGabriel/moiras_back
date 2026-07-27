DC := docker compose -f docker/compose.yml
SERVICE := backend

.PHONY: start stop

start:
	$(DC) up -d --build
	$(DC) exec $(SERVICE) bash

stop:
	$(DC) down --volumes --remove-orphans
