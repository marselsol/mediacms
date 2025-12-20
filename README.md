
## MediaCMS

Бэкенд CMS для мультиформатного медиа-портала (статьи, видео, подкасты).

### Быстрый запуск (Docker Compose)
1) Склонируйте проект на компьютер.
2) Поднимите сервисы:
```
docker compose up -d
```
Postgres будет на 5432, Redis на 6379.
Да, я понимаю что `.env` хранить не безопасно в GitHub — это добавлено исключительно для простоты запуска.

### Локальный запуск приложения
```
./mvnw spring-boot:run
```
Приложение доступно на `http://localhost:8080`.

### Swagger/OpenAPI
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api/docs`

### Сценарий проверки (curl)
1) Поднять Docker Compose:
```
docker compose up -d
```
2) Регистрация:
```
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"editor\",\"password\":\"secret123\"}"
```
3) Логин (в ответе `token`, он уже содержит префикс `Bearer `):
```
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"editor\",\"password\":\"secret123\"}"
```
4) Создать статью (используйте `token` из шага 3):
```
curl -X POST http://localhost:8080/api/articles \
  -H "Content-Type: application/json" \
  -H "Authorization: <token>" \
  -d "{\"title\":\"Hello\",\"text\":\"Body\",\"author\":\"Author\"}"
```
5) Получить статью:
```
curl http://localhost:8080/api/articles/<articleId>
```
6) Отправить просмотр:
```
curl -X POST http://localhost:8080/api/analytics/view/article/<articleId>
```
7) Получить top-10:
```
curl http://localhost:8080/api/analytics/top/article?limit=10
```
