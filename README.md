# Instalación

## 1. Clonar repositorio

```bash
git clone https://github.com/MerelesLautaro/Equipo07-SportPulseMS.git
```

## 2. Configurar la API Key de API-Sports en el archivo .env
```bash
https://dashboard.api-football.com/profile?access
```
## 3. Ejecutar con Docker
```bash
docker-compose up --build
```

## Health Check
```bash
GET /actuator/health
```

### Endpoints
```bash
http://localhost:8080/actuator/health
http://localhost:8081/actuator/health
http://localhost:8082/actuator/health
http://localhost:8083/actuator/health
http://localhost:8085/actuator/health
http://localhost:8086/actuator/health
http://localhost:8088/actuator/health
http://localhost:8089/actuator/health
```
