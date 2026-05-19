# Tienda Web Express

Aplicación web académica de comercio electrónico construida con `Spring Boot`, `Thymeleaf` y `PostgreSQL`.

## Requisitos

- Java 21 o superior
- PostgreSQL con la base `tienda_web_express`

## Base de datos

La aplicación asume que la base y las tablas ya existen. El script de referencia está en:

- [database/tienda_web_express.sql](database/tienda_web_express.sql)

Por defecto la app usa:

- `DB_URL=jdbc:postgresql://localhost:5432/tienda_web_express`
- `DB_USERNAME=freyes`
- `DB_PASSWORD=` vacío

## Ejecutar

```bash
./mvnw spring-boot:run
```

## Funcionalidad principal

- Página de inicio con branding, hero y productos destacados
- Catálogo dinámico con filtro por categoría y detalle rápido
- Vista de detalle por producto
- Contador local de productos seleccionados con `sessionStorage`
- Formulario de pedido con validación en cliente y servidor
- Persistencia real en `pedido` y `detalle_pedido`
- Descuento de stock al confirmar el pedido
