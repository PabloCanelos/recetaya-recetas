# recetaya-recetas
# RecetaYa — Recetas

Microservicio responsable de registrar las recetas médicas y administrar su estado durante la reserva de stock y la dispensación.

## Contexto del proyecto

RecetaYa tiene cuatro microservicios Spring Boot: Autenticación y Usuarios, Recetas, Inventario y Dispensación. Este repositorio contiene únicamente **Recetas** y su base de datos.

Cuando el médico emite una receta, este servicio debe registrarla inmediatamente como `PENDIENTE_RESERVA`. La reserva de stock se solicita después mediante el flujo:

**Recetas → Amazon SQS → AWS Lambda → Inventario**

La emisión de la receta no debe esperar la respuesta de Inventario.

## Configuración acordada

| Concepto | Valor |
|---|---|
| Motor de base de datos | MySQL Server 8.4 |
| Base de datos propia | `db_recetas` |
| Puerto local del microservicio | `8082` |
| Puerto reservado para la entrada local | `8080` |

El puerto `8082` corresponde a Spring Boot. La conexión a MySQL se configura por separado.

## Responsabilidades

Este microservicio debe:

1. Permitir al médico emitir una receta con paciente, medicamentos, cantidades y sucursal.
2. Guardar la receta y sus detalles con estado inicial `PENDIENTE_RESERVA`.
3. Generar la solicitud de reserva para Amazon SQS sin hacer esperar al médico a que termine la reserva.
4. Actualizar **su propio estado** cuando Inventario comunique el resultado de la reserva.
5. Permitir consultar recetas, incluidas las que tienen stock reservado.
6. Actualizar la receta a `DISPENSADA` cuando el flujo de entrega quede confirmado.

La arquitectura entregada muestra `POST /recetas` y `GET /recetas?estado=STOCK_RESERVADO`. Las rutas de comunicación interna y los formatos de sus mensajes deberán acordarse antes de integrar los servicios.

## Clases de entidad y modelo relacional

Crear **dos clases de entidad**: `Receta` y `DetalleReceta`. Cada una corresponde a una tabla de `db_recetas`.

### Clase `Receta` — tabla `RECETA`

| Atributo Java | Tipo Java | Columna MySQL | Tipo MySQL | Restricción |
|---|---|---|---|---|
| `id` | `Integer` | `id_receta` | `INT` | Clave primaria, autoincremental |
| `idMedico` | `Integer` | `id_medico` | `INT` | Obligatorio; ID de un usuario del servicio de Autenticación y Usuarios |
| `pacienteNombre` | `String` | `paciente_nombre` | `VARCHAR(150)` | Obligatorio |
| `idSucursal` | `Integer` | `id_sucursal` | `INT` | Obligatorio; ID de una sucursal de Inventario |
| `fechaEmision` | `LocalDateTime` | `fecha_emision` | `DATETIME` | Obligatorio |
| `estado` | `EstadoReceta` | `estado` | `VARCHAR(32)` | Obligatorio; guardar el nombre del estado como texto |

### Clase `DetalleReceta` — tabla `DETALLE_RECETA`

| Atributo Java | Tipo Java | Columna MySQL | Tipo MySQL | Restricción |
|---|---|---|---|---|
| `id` | `Integer` | `id_detalle_receta` | `INT` | Clave primaria, autoincremental |
| `receta` | `Receta` | `id_receta` | `INT` | Clave foránea obligatoria a `RECETA.id_receta` |
| `idMedicamento` | `Integer` | `id_medicamento` | `INT` | Obligatorio; ID de un medicamento de Inventario |
| `cantidad` | `Integer` | `cantidad` | `INT` | Obligatorio y mayor que cero |

**Relación:** una `Receta` tiene uno o más `DetalleReceta`. Cada detalle pertenece a una receta. La clave foránea está en `DETALLE_RECETA.id_receta`.

`idMedico`, `idSucursal` e `idMedicamento` son identificadores de datos pertenecientes a **otros microservicios**. Se almacenan como `INT`, pero no se crean claves foráneas entre sus bases de datos y `db_recetas`.

## Estados de la receta

Crear el enum Java `EstadoReceta` con estos nombres exactos, tomados del diagrama de arquitectura entregado:

| Estado | Significado |
|---|---|
| `PENDIENTE_RESERVA` | La receta fue aceptada y espera el resultado de la reserva. |
| `STOCK_RESERVADO` | Inventario confirmó la reserva. |
| `RESERVA_RECHAZADA` | La reserva no pudo realizarse, por ejemplo, por falta de stock. |
| `DISPENSADA` | La entrega fue confirmada. |

El flujo previsto es:

- Al emitir: `PENDIENTE_RESERVA`.
- Si Inventario confirma: `STOCK_RESERVADO`.
- Si Inventario rechaza: `RESERVA_RECHAZADA`.
- Tras dispensar una receta con stock reservado: `DISPENSADA`.

**Solo Recetas modifica el estado guardado en `RECETA`.** Los demás servicios le comunican el resultado mediante la integración acordada.

## Reglas de implementación compartidas

- Usar `INT` en MySQL e `Integer` en Java para todos los ID y cantidades. No usar `BIGINT` ni `Long`.
- Configurar `server.port=8082`.
- Al recibir una emisión válida, guardar la receta con sus detalles y estado `PENDIENTE_RESERVA` sin esperar la reserva de stock.
- La solicitud enviada a SQS debe identificar la receta, la sucursal y los medicamentos con sus cantidades. Su formato exacto se acordará con Inventario antes de implementar la integración.
- Prever una forma de reintentar la publicación si la receta se guarda pero ocurre un fallo antes de enviar el mensaje a SQS. El mecanismo concreto aún debe acordarse.
- No guardar tablas de usuarios, sucursales, medicamentos ni stock en `db_recetas`.
- No agregar precios ni campos `FLOAT`: el caso oficial no contempla cálculos monetarios.
- Mantener las credenciales y demás valores secretos fuera del repositorio.

## Alcance de lo acordado

**Exigido por el caso y los requisitos entregados:** emitir recetas con paciente, medicamentos y sucursal; registrarlas inmediatamente como pendientes de reserva; solicitar la reserva de forma asíncrona sin bloquear al médico; permitir consultar recetas con stock reservado y reflejar el resultado de la reserva.

**Definido por el diagrama entregado:** los nombres `PENDIENTE_RESERVA`, `STOCK_RESERVADO`, `RESERVA_RECHAZADA` y `DISPENSADA`, además de las rutas públicas de recetas indicadas arriba.

**Definido por la propuesta del equipo para implementarlo:** tablas `RECETA` y `DETALLE_RECETA`, sus columnas y relación, clases Java y tipos de datos indicados arriba, MySQL Server 8.4 y puerto local `8082`.

Recetas administra la emisión y los estados. **Inventario** administra las existencias y las reservas; **Dispensación** registra la entrega.
