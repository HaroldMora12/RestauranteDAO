-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 09-03-2026 a las 23:23:51
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `restaurante`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clientes`
--

CREATE TABLE `clientes` (
  `id_cliente` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `telefono` varchar(15) NOT NULL,
  `correo` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;

--
-- Volcado de datos para la tabla `clientes`
--

INSERT INTO `clientes` (`id_cliente`, `nombre`, `telefono`, `correo`) VALUES
(1, 'juan mora', '3154187427', 'asdfawe@sdafgsr.com'),
(2, 'juan', '31231', 'nosdfef'),
(4, 'ederec', '31231', 'nosdfefdd'),
(5, 'ederecd', '31231', 'nosdfefddd');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empleado`
--

CREATE TABLE `empleado` (
  `id_empleado` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `cargo` varchar(50) NOT NULL,
  `salario` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;

--
-- Volcado de datos para la tabla `empleado`
--

INSERT INTO `empleado` (`id_empleado`, `nombre`, `cargo`, `salario`) VALUES
(1, 'Juan P?rez', 'Mesero', 12000.00),
(2, 'Ana Mart?nez', 'Mesera', 3500.00),
(3, 'Carlos G?mez', 'Chef', 5000.00),
(4, 'Luisa P?rez', 'Cajera', 3200.00),
(5, 'Roberto D?az', 'Administrador', 6000.00);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `mesa`
--

CREATE TABLE `mesa` (
  `id_mesa` int(11) NOT NULL,
  `numero` int(11) NOT NULL,
  `capacidad` int(11) NOT NULL,
  `estado` enum('Disponible','Ocupada','Reservada','Mantenimiento') DEFAULT 'Disponible'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `mesa`
--

INSERT INTO `mesa` (`id_mesa`, `numero`, `capacidad`, `estado`) VALUES
(1, 1, 4, 'Disponible'),
(7, 2, 2, 'Ocupada'),
(8, 3, 6, 'Reservada'),
(9, 4, 4, 'Disponible'),
(10, 5, 8, 'Mantenimiento');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `orden`
--

CREATE TABLE `orden` (
  `id_orden` int(11) NOT NULL,
  `fecha` datetime DEFAULT current_timestamp(),
  `total` double NOT NULL DEFAULT 0,
  `estado` varchar(30) NOT NULL,
  `id_cliente` int(11) DEFAULT NULL,
  `id_empleado` int(11) DEFAULT NULL,
  `id_mesa` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;

--
-- Volcado de datos para la tabla `orden`
--

INSERT INTO `orden` (`id_orden`, `fecha`, `total`, `estado`, `id_cliente`, `id_empleado`, `id_mesa`) VALUES
(7, '2025-04-06 14:50:40', 10000, 'entregada', 2, 3, 10),
(17, '2025-04-06 18:16:15', 0, 'preparacion', 5, 3, 10),
(19, '2026-02-16 18:57:48', 5000, 'preparacion', 1, 1, 1),
(20, '2026-02-16 19:04:05', 67500, 'preparacion', 5, 1, 1),
(21, '2026-02-24 18:43:45', 5000, 'preparacion', 1, 1, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `orden_producto`
--

CREATE TABLE `orden_producto` (
  `id_orden` int(11) NOT NULL,
  `id_producto` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `precio_unitario` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;

--
-- Volcado de datos para la tabla `orden_producto`
--

INSERT INTO `orden_producto` (`id_orden`, `id_producto`, `cantidad`, `precio_unitario`) VALUES
(7, 1, 2, 0.00),
(7, 2, 2, 5000.00),
(19, 1, 1, 5000.00),
(20, 1, 1, 5000.00),
(20, 5, 5, 12500.00),
(21, 1, 1, 5000.00);

--
-- Disparadores `orden_producto`
--
DELIMITER $$
CREATE TRIGGER `actualizar_total_delete` AFTER DELETE ON `orden_producto` FOR EACH ROW BEGIN
  UPDATE orden
  SET total = (
    SELECT IFNULL(SUM(precio_unitario * cantidad), 0)
    FROM orden_producto
    WHERE id_orden = OLD.id_orden
  )
  WHERE id_orden = OLD.id_orden;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `actualizar_total_insert` AFTER INSERT ON `orden_producto` FOR EACH ROW BEGIN
  UPDATE orden
  SET total = (
    SELECT SUM(precio_unitario * cantidad)
    FROM orden_producto
    WHERE id_orden = NEW.id_orden
  )
  WHERE id_orden = NEW.id_orden;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `actualizar_total_update` AFTER UPDATE ON `orden_producto` FOR EACH ROW BEGIN
  UPDATE orden
  SET total = (
    SELECT SUM(precio_unitario * cantidad)
    FROM orden_producto
    WHERE id_orden = NEW.id_orden
  )
  WHERE id_orden = NEW.id_orden;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productos`
--

CREATE TABLE `productos` (
  `id_producto` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `categoria` varchar(50) NOT NULL,
  `precio` decimal(10,2) NOT NULL CHECK (`precio` > 0),
  `disponibilidad` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;

--
-- Volcado de datos para la tabla `productos`
--

INSERT INTO `productos` (`id_producto`, `nombre`, `categoria`, `precio`, `disponibilidad`) VALUES
(1, 'hamburguesa doble', 'comida', 5000.00, 1),
(2, 'hamburguesa triple', 'comida', 5000.00, 1),
(4, 'perro caliente', 'comida', 2000.00, 1),
(5, 'salchipapa', 'comida', 12500.00, 1);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `clientes`
--
ALTER TABLE `clientes`
  ADD PRIMARY KEY (`id_cliente`),
  ADD UNIQUE KEY `correo` (`correo`);

--
-- Indices de la tabla `empleado`
--
ALTER TABLE `empleado`
  ADD PRIMARY KEY (`id_empleado`);

--
-- Indices de la tabla `mesa`
--
ALTER TABLE `mesa`
  ADD PRIMARY KEY (`id_mesa`),
  ADD UNIQUE KEY `numero` (`numero`);

--
-- Indices de la tabla `orden`
--
ALTER TABLE `orden`
  ADD PRIMARY KEY (`id_orden`),
  ADD KEY `id_cliente` (`id_cliente`),
  ADD KEY `id_empleado` (`id_empleado`),
  ADD KEY `id_mesa` (`id_mesa`);

--
-- Indices de la tabla `orden_producto`
--
ALTER TABLE `orden_producto`
  ADD PRIMARY KEY (`id_orden`,`id_producto`),
  ADD KEY `id_producto` (`id_producto`);

--
-- Indices de la tabla `productos`
--
ALTER TABLE `productos`
  ADD PRIMARY KEY (`id_producto`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `clientes`
--
ALTER TABLE `clientes`
  MODIFY `id_cliente` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `empleado`
--
ALTER TABLE `empleado`
  MODIFY `id_empleado` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `mesa`
--
ALTER TABLE `mesa`
  MODIFY `id_mesa` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `orden`
--
ALTER TABLE `orden`
  MODIFY `id_orden` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT de la tabla `productos`
--
ALTER TABLE `productos`
  MODIFY `id_producto` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `orden`
--
ALTER TABLE `orden`
  ADD CONSTRAINT `orden_ibfk_1` FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id_cliente`) ON DELETE SET NULL,
  ADD CONSTRAINT `orden_ibfk_2` FOREIGN KEY (`id_empleado`) REFERENCES `empleado` (`id_empleado`) ON DELETE SET NULL,
  ADD CONSTRAINT `orden_ibfk_3` FOREIGN KEY (`id_mesa`) REFERENCES `mesa` (`id_mesa`) ON DELETE SET NULL;

--
-- Filtros para la tabla `orden_producto`
--
ALTER TABLE `orden_producto`
  ADD CONSTRAINT `orden_producto_ibfk_1` FOREIGN KEY (`id_orden`) REFERENCES `orden` (`id_orden`) ON DELETE CASCADE,
  ADD CONSTRAINT `orden_producto_ibfk_2` FOREIGN KEY (`id_producto`) REFERENCES `productos` (`id_producto`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
