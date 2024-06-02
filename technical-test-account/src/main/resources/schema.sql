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