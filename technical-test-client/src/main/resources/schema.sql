CREATE TABLE genero (
    id INT PRIMARY KEY,
    descripcion VARCHAR(100) NOT NULL
);

CREATE TABLE cliente (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    id_genero INT NULL,
    edad INT NULL,
    identificacion VARCHAR(30) NULL,
    direccion VARCHAR(500) NULL,
    telefono VARCHAR(30) NULL,
    contrasena VARCHAR(100) NOT NULL,
    estado BOOLEAN NOT NULL,
    CONSTRAINT fk_cliente_y_genero FOREIGN KEY (id_genero) REFERENCES genero (id)
);