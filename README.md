# ⚽ SportPulseMS – Arquitectura de Microservicios

Sistema backend basado en microservicios para la gestión de ligas, equipos, partidos y estadísticas deportivas, con un gateway centralizado y agregación de datos en tiempo real.

---

## 📄 Enunciado del proyecto

- https://drive.google.com/file/d/16g2bRSjS-4pIgwQzfLQg7jHKgTU3eOck/view?usp=sharing
---

## 🧩 Microservicios del sistema

| Microservicio       | Puerto | Responsabilidad                                      |
|--------------------|--------|------------------------------------------------------|
| ms-gateway         | 8080   | Punto de entrada, routing, circuit breaker           |
| ms-auth            | 8081   | Registro, login y JWT                                |
| ms-leagues         | 8082   | Ligas, países y temporadas                           |
| ms-teams           | 8083   | Equipos e información                                |
| ms-fixtures        | 8085   | Partidos y calendarios                               |
| ms-standings       | 8086   | Clasificaciones                                      |
| ms-notifications   | 8088   | Suscripciones y alertas                              |
| ms-dashboard       | 8089   | Agregación de datos                                  |

---

## 🐳 Requisitos

- Docker
- Docker Compose

---

## ⚙️ Configuración

### 1. Clonar el repositorio

```bash
git clone https://github.com/MerelesLautaro/Equipo07-SportPulseMS.git
cd Equipo07-SportPulseMS
```

### 2. Crear archivo .env
```bash
cp .env.example .env
```

### 3. Completar variables en .env
```bash
APISPORTS_KEY=

POSTGRES_USER=
POSTGRES_PASSWORD=

JWT_SECRET=
JWT_EXPIRATION=

INTERNAL_SECRET=
```

### 🚀 Ejecución

Levantar todos los servicios:

```bash
docker-compose up --build
```

### Documentación (Swagger)

🌐 Gateway

```bash
http://localhost:8080/api/gateway/webjars/swagger-ui/index.html#
```

### 🔧 Microservicios

Acceso general:

```bash
http://localhost:8080/api/{servicio}/swagger-ui/index.html
```


- [Fixtures](http://localhost:8080/api/fixtures/swagger-ui/index.html)
- [Standings](http://localhost:8080/api/standings/swagger-ui/index.html)
- [Leagues](http://localhost:8080/api/leagues/swagger-ui/index.html)
- [Teams](http://localhost:8080/api/teams/swagger-ui/index.html)
- [Auth](http://localhost:8080/api/auth/swagger-ui/index.html)
- [Notifications](http://localhost:8080/api/notifications/swagger-ui/index.html)
- [Dashboard](http://localhost:8080/api/dashboard/swagger-ui/index.html)


### 👥 Colaboradores
- Lautaro Mereles → https://github.com/MerelesLautaro
- Alberto Cruz → https://github.com/alberto-cruz-mtz
- Joaquín → https://github.com/Joaquin5362656
