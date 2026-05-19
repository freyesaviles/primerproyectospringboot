CREATE DATABASE tienda_web_express
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1;

DROP TABLE IF EXISTS detalle_pedido;
DROP TABLE IF EXISTS pedido;
DROP TABLE IF EXISTS producto;
DROP TABLE IF EXISTS categoria;

CREATE TABLE categoria (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE producto (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    precio NUMERIC(10,2) NOT NULL CHECK (precio >= 0),
    imagen VARCHAR(255),
    stock INTEGER NOT NULL DEFAULT 0 CHECK (stock >= 0),
    categoria_id BIGINT NOT NULL,
    CONSTRAINT fk_producto_categoria
        FOREIGN KEY (categoria_id)
        REFERENCES categoria(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

CREATE TABLE pedido (
    id BIGSERIAL PRIMARY KEY,
    nombre_cliente VARCHAR(150) NOT NULL,
    correo VARCHAR(150) NOT NULL,
    comentario TEXT,
    fecha_pedido TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total NUMERIC(10,2) NOT NULL DEFAULT 0 CHECK (total >= 0)
);

CREATE TABLE detalle_pedido (
    id BIGSERIAL PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad INTEGER NOT NULL CHECK (cantidad > 0),
    precio_unitario NUMERIC(10,2) NOT NULL CHECK (precio_unitario >= 0),
    subtotal NUMERIC(10,2) NOT NULL CHECK (subtotal >= 0),
    CONSTRAINT fk_detalle_pedido
        FOREIGN KEY (pedido_id)
        REFERENCES pedido(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_detalle_producto
        FOREIGN KEY (producto_id)
        REFERENCES producto(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

INSERT INTO categoria (nombre) VALUES
('Tecnología'),
('Ropa'),
('Hogar'),
('Accesorios');

INSERT INTO producto (nombre, descripcion, precio, imagen, stock, categoria_id) VALUES
('Mouse inalámbrico', 'Mouse ergonómico con conexión USB.', 12.99, 'mouse.jpg', 25, 1),
('Teclado mecánico', 'Teclado mecánico con iluminación LED.', 39.99, 'teclado.jpg', 15, 1),
('Camiseta básica', 'Camiseta de algodón disponible en varias tallas.', 9.50, 'camiseta.jpg', 40, 2),
('Taza personalizada', 'Taza de cerámica ideal para regalos.', 6.75, 'taza.jpg', 30, 3),
('Mochila urbana', 'Mochila resistente para uso diario.', 24.99, 'mochila.jpg', 20, 4);

INSERT INTO pedido (nombre_cliente, correo, comentario, total)
VALUES ('Cliente de prueba', 'cliente@correo.com', 'Pedido generado como dato inicial.', 52.98);

INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad, precio_unitario, subtotal) VALUES
(1, 1, 1, 12.99, 12.99),
(1, 2, 1, 39.99, 39.99);
