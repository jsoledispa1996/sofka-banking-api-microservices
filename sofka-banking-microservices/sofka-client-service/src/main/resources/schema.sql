CREATE TABLE ba_personas (
    pe_id_persona INT AUTO_INCREMENT PRIMARY KEY,
    pe_nombre VARCHAR(100) NOT NULL,
    pe_genero VARCHAR(20) NOT NULL,
    pe_edad INT NOT NULL,
    pe_identificacion VARCHAR(20) NOT NULL,
    pe_direccion VARCHAR(255) NOT NULL,
    pe_telefono VARCHAR(20) NOT NULL
);

CREATE TABLE ba_clientes (
    cl_id_persona INT PRIMARY KEY,
    cl_id_cliente VARCHAR(20) UNIQUE NOT NULL,
    cl_contrasena VARCHAR(255) NOT NULL,
    cl_estado BOOLEAN NOT NULL,
    FOREIGN KEY (cl_id_persona) REFERENCES ba_personas(pe_id_persona)
);