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
--
--
--
CREATE TABLE tipo_cuenta (
    id INT PRIMARY KEY,
    descripcion VARCHAR(100) NOT NULL
);

CREATE TABLE cuenta (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    numero VARCHAR(30) UNIQUE NOT NULL,
    id_tipo_cuenta INT NULL,
    saldo_inicial FLOAT NULL,
    estado BOOLEAN NOT NULL,
    id_cliente INT NULL,
    saldo_actual FLOAT NULL,
    CONSTRAINT fk_cuenta_y_tipo_cuenta FOREIGN KEY (id_tipo_cuenta) REFERENCES tipo_cuenta (id)
);

CREATE TABLE tipo_movimiento (
    id INT PRIMARY KEY,
    descripcion VARCHAR(100) NOT NULL
);

CREATE TABLE movimiento (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_cuenta BIGINT NOT NULL,
    id_tipo_movimiento INT NOT NULL,
    valor FLOAT NOT NULL,
    fecha DATE NOT NULL,
    CONSTRAINT fk_movimiento_y_cuenta FOREIGN KEY (id_cuenta) REFERENCES cuenta (id),
    CONSTRAINT fk_movimiento_y_tipo_movimiento FOREIGN KEY (id_tipo_movimiento) REFERENCES tipo_movimiento (id)
);

INSERT INTO genero (id, descripcion) VALUES (1, 'Masculino');
INSERT INTO genero (id, descripcion) VALUES (2, 'Femenino');
INSERT INTO genero (id, descripcion) VALUES (3, 'Otros');

INSERT INTO tipo_cuenta (id, descripcion) VALUES (1, 'Ahorros');
INSERT INTO tipo_cuenta (id, descripcion) VALUES (2, 'Corriente');

INSERT INTO tipo_movimiento (id, descripcion) VALUES (1, 'Retiro');
INSERT INTO tipo_movimiento (id, descripcion) VALUES (2, 'Deposito');