# ms-leagues - Documentacion tecnica

## 1. Contexto y objetivo del microservicio

`ms-leagues` expone informacion de ligas de futbol consumiendo API-SPORTS y aplicando reglas de seguridad JWT para endpoints protegidos.

En el estado actual del proyecto, este microservicio implementa completamente la HU-10 (`GET /api/leagues` con filtros y cache en memoria).

## 2. Alcance funcional implementado

### Endpoint
- `GET /api/leagues`

### Filtros soportados
- `country` (opcional)
- `season` (opcional)

### Reglas de acceso
- Requiere `Authorization: Bearer <token>`.
- Sin JWT o con JWT invalido responde `401`.

### Fuente de datos
- API-SPORTS (`https://v3.football.api-sports.io/leagues`)
- Header requerido en cada request saliente: `x-apisports-key`

### Transformacion de respuesta
La salida se mapea al contrato interno:
- `id`
- `name`
- `type`
- `country`
- `logo`
- `currentSeason`
- `startDate`
- `endDate`

## 3. Arquitectura interna

### Capa Controller
- `LeagueController`
  - Expone `GET /api/leagues`
  - Recibe query params opcionales y delega al servicio

### Capa Service
- `LeagueService`
  - Orquesta consulta a API externa
  - Aplica filtrado defensivo por `country` y `season`
  - Realiza transformacion de DTO externo a DTO de salida
  - Implementa cache con clave compuesta `country|season`

### Capa Client (Outbound)
- `ApiSportsLeaguesClient` (OpenFeign)
  - Encapsula llamada a `/leagues`
- `ApiSportsFeignConfig`
  - Interceptor para inyectar `x-apisports-key`

### Seguridad
- `SecurityConfig`
  - Protege `/api/leagues/**`
  - Permite `/actuator/health`, `/actuator/info`, Swagger/OpenAPI
- `JwtAuthenticationFilter`
  - Valida Bearer token por request
  - Corta flujo con 401 cuando corresponde
- `JwtService`
  - Parsea y valida firma JWT

### Cache
- `CacheConfig`
  - `ConcurrentMapCache` en memoria
  - Cache nombrada: `leaguesByFilters`

## 4. Estructura de paquetes relevante

- `com.Equipo07_SportPulseMS.ms_leagues.controller`
- `com.Equipo07_SportPulseMS.ms_leagues.service`
- `com.Equipo07_SportPulseMS.ms_leagues.client`
- `com.Equipo07_SportPulseMS.ms_leagues.client.dto`
- `com.Equipo07_SportPulseMS.ms_leagues.dto`
- `com.Equipo07_SportPulseMS.ms_leagues.config`
- `com.Equipo07_SportPulseMS.ms_leagues.security`

## 5. Configuracion requerida

Definida en `application.properties`:

- `apisports.base-url=https://v3.football.api-sports.io`
- `apisports.key=${APISPORTS_KEY:}`
- `jwt.secret=${JWT_SECRET:default-jwt-secret-key-default-jwt-secret-key-12345}`

Variables de entorno recomendadas en runtime:
- `APISPORTS_KEY`
- `JWT_SECRET`

## 6. Dependencias tecnicas clave

- Spring Boot Web MVC
- Spring Security
- Spring Cache
- Spring Cloud OpenFeign
- JJWT (`jjwt-api`, `jjwt-impl`, `jjwt-jackson`)

## 7. Pruebas implementadas

- `LeagueServiceTest`
  - caso sin filtros
  - caso filtro por `country`
  - caso filtro por `season`
- `JwtAuthenticationFilterTest`
  - 401 sin token
  - flujo permitido con token valido
- `MsLeaguesApplicationTests`
  - carga de contexto

Comando de ejecucion local:

```bash
./mvnw.cmd test
```

## 8. Decisiones de diseno

- Se usa Feign por consistencia con la arquitectura declarada del proyecto.
- Se aplica filtrado defensivo local ademas del query param externo para robustez funcional.
- Se implementa cache in-memory simple para cumplir HU-10 con baja complejidad operativa.
- Se separan DTO externos e internos para desacoplar contrato publico del proveedor.

## 9. Puntos de extension para futuras historias

- HU-11 (`GET /api/leagues/{leagueId}`): reutilizar `ApiSportsLeaguesClient` agregando flujo de detalle y excepcion `LEAGUE_NOT_FOUND`.
- Estandarizar errores con `@RestControllerAdvice` especifico (hoy hay handler generico).
- Incorporar TTL real en cache (ej. Caffeine) si el equipo define politicas de expiracion.
- Alinear validacion JWT con implementacion final de `ms-auth` para garantizar compatibilidad de firma/claims.

## 10. Riesgos y consideraciones de integracion

- La validacion JWT debe estar alineada con la estrategia final de firmado de `ms-auth`.
- La API externa puede variar comportamiento de filtros; por eso se mantiene filtrado local defensivo.
- El limite del plan gratuito requiere monitorear cache hit-rate para evitar agotar requests diarios.
