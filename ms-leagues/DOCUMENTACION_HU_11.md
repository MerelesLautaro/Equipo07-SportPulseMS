# ms-leagues - Documentacion HU-11

## 1. Contexto y objetivo

La HU-11 agrega el endpoint de detalle de liga en `ms-leagues` para consultar una liga puntual por ID, incluyendo temporadas disponibles y temporada actual.

La implementacion se hizo respetando la especificacion del proyecto:
- endpoint protegido con JWT,
- consumo de API-SPORTS,
- respuesta 404 con `LEAGUE_NOT_FOUND` cuando la liga no existe.

## 2. Alcance funcional implementado

### Endpoint
- `GET /api/leagues/{leagueId}`

### Reglas de acceso
- Requiere `Authorization: Bearer <token>`.
- Sin JWT o con JWT invalido responde `401` (segun configuracion de seguridad del microservicio).

### Respuesta esperada (contrato interno)
- `id`
- `name`
- `type`
- `country`
- `logo`
- `seasons` (lista de anios)
- `currentSeason`:
  - `year`
  - `startDate`
  - `endDate`
  - `current`

### Error funcional
- Si no existe la liga: `404` con
  - `error: LEAGUE_NOT_FOUND`
  - `message: No existe una liga con el ID proporcionado`
  - `timestamp`

## 3. Componentes agregados o extendidos

### Controller
- `LeagueController`
  - Se agrega `GET /api/leagues/{leagueId}`
  - Delega en `LeagueService#getLeagueById`

### Service
- `LeagueService`
  - Nuevo metodo `getLeagueById(Integer leagueId)`
  - Consulta API-SPORTS via Feign con query `id`
  - Mapea datos al DTO de detalle
  - Lanza `LeagueNotFoundException` si no encuentra la liga
  - Cachea por ID para evitar llamadas repetidas

### DTOs
- `LeagueDetailResponse`
- `LeagueCurrentSeasonResponse`

### Excepciones y manejo de errores
- `LeagueNotFoundException`
- `ApiExceptionHandler`
  - Nuevo handler para mapear `LeagueNotFoundException` a 404 + `LEAGUE_NOT_FOUND`

### Cache
- `CacheConfig`
  - Se agrega cache `leagueById`

## 4. Flujo tecnico de la request

1. Cliente invoca `GET /api/leagues/{leagueId}` con Bearer token.
2. Seguridad valida JWT (`SecurityConfig` + `JwtAuthenticationFilter`).
3. Controller delega en `LeagueService#getLeagueById`.
4. Service consulta `ApiSportsLeaguesClient` (`/leagues?id={leagueId}`).
5. Si no hay resultado, lanza `LeagueNotFoundException`.
6. Si hay resultado, transforma DTO externo a `LeagueDetailResponse`.
7. Se devuelve respuesta 200 con detalle de la liga.

## 5. Decisiones tecnicas y por que

- Se reutiliza Feign ya existente para mantener coherencia con HU-10 y con la arquitectura del proyecto.
- Se separa DTO externo (proveedor) de DTO interno (respuesta propia) para desacoplar contratos.
- Se implementa excepcion de dominio para hacer explicito el caso funcional de liga no encontrada.
- Se agrega cache en memoria por ID para reducir latencia y consumo de API externa.
- Se mantiene la misma politica de seguridad para toda la ruta `/api/leagues/**`, evitando divergencias entre endpoints del mismo recurso.

## 6. Archivos principales involucrados

- `ms-leagues/src/main/java/com/Equipo07_SportPulseMS/ms_leagues/controller/LeagueController.java`
- `ms-leagues/src/main/java/com/Equipo07_SportPulseMS/ms_leagues/service/LeagueService.java`
- `ms-leagues/src/main/java/com/Equipo07_SportPulseMS/ms_leagues/dto/LeagueDetailResponse.java`
- `ms-leagues/src/main/java/com/Equipo07_SportPulseMS/ms_leagues/dto/LeagueCurrentSeasonResponse.java`
- `ms-leagues/src/main/java/com/Equipo07_SportPulseMS/ms_leagues/exception/LeagueNotFoundException.java`
- `ms-leagues/src/main/java/com/Equipo07_SportPulseMS/ms_leagues/controller/ApiExceptionHandler.java`
- `ms-leagues/src/main/java/com/Equipo07_SportPulseMS/ms_leagues/config/CacheConfig.java`

## 7. Pruebas relacionadas

- `LeagueServiceTest`
  - valida mapeo del detalle de liga
  - valida que el caso no encontrado lanza `LeagueNotFoundException`
- `ApiExceptionHandlerTest`
  - valida traduccion a 404 con `LEAGUE_NOT_FOUND`
- `JwtAuthenticationFilterTest`
  - asegura comportamiento de autenticacion para rutas protegidas

Comando recomendado:

```bash
./mvnw.cmd test
```

## 8. Relacion con HU-10

HU-11 se construyo sobre la base de HU-10:
- mismo cliente Feign a API-SPORTS,
- misma estrategia de seguridad JWT,
- misma logica de mapeo y separacion de DTOs,
- extension de cache para escenario de detalle por ID.
