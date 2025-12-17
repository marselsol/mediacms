## MediaCMS

### Быстрый старт (Docker Compose)
- Скопируйте переменные окружения: `cp .env.example .env` и при необходимости обновите значения.
- Запустите инфраструктуру: `docker compose up -d` (Postgres доступен на 5432, Redis на 6379).

### Локальный запуск приложения
- Запустите: `./mvnw spring-boot:run` (или `mvn spring-boot:run`, если Maven установлен глобально).
- Приложение по умолчанию доступно на `http://localhost:8080`.

### Swagger/OpenAPI
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api/docs`
