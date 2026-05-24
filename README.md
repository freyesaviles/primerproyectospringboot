# Tienda Web Express

Aplicación web académica de comercio electrónico construida con `Spring Boot`, `Thymeleaf`, `PostgreSQL`, `HttpSession` y `Bootstrap 5`.

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

## Stack principal

- `Spring Boot`
- `Spring MVC`
- `Thymeleaf`
- `Spring Data JPA`
- `PostgreSQL`
- `HttpSession`
- `Bootstrap 5`

## Funcionalidad principal

- Página de inicio con branding, hero y productos destacados
- Catálogo dinámico con búsqueda por nombre, filtro por categoría y consulta combinada
- Mantenimiento visual de filtros seleccionados y opción para limpiar filtros
- Detalle rápido dentro del catálogo
- Vista de detalle por producto
- Carrito de compras en servidor usando `HttpSession`
- Agregar productos al carrito desde catálogo y detalle
- Contador global de productos en el header
- Vista `/carrito` con nombre, precio, cantidad, subtotal y total general
- Eliminación de productos desde el carrito
- Checkout del carrito con captura de nombre, correo y comentario o dirección
- Validación de stock al agregar al carrito y al confirmar el checkout
- Flujo de pedido directo en `/pedido` para compra rápida de un solo producto
- Persistencia real en `pedido` y `detalle_pedido`
- Creación de pedidos con múltiples productos desde el carrito
- Descuento de stock al confirmar el pedido
- Confirmación visual del pedido registrado
- Interfaz responsiva con `Bootstrap 5` y estilos personalizados

## Rutas principales

- `/` inicio con productos destacados
- `/catalogo` catálogo con filtros
- `/productos/{id}` detalle de producto
- `/pedido` formulario de pedido directo
- `/carrito` resumen del carrito
- `/carrito/checkout` checkout del carrito
- `/pedido/confirmacion/{id}` confirmación del pedido

## Flujo de compra actual

### Compra directa

1. El usuario entra a `/pedido` o llega desde el detalle de un producto.
2. Selecciona producto y cantidad.
3. Completa sus datos.
4. El sistema valida stock, guarda el pedido y descuenta inventario.

### Compra con carrito

1. El usuario agrega productos desde `/catalogo` o `/productos/{id}`.
2. El sistema guarda el carrito en `HttpSession`.
3. En `/carrito` puede revisar cantidades, subtotales y total.
4. El usuario elimina productos si lo necesita.
5. En `/carrito/checkout` completa sus datos y confirma.
6. El sistema crea un solo pedido con múltiples detalles, descuenta stock y limpia el carrito.

