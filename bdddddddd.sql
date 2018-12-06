-- --------------------------------------------------------
-- Host:                         localhost
-- Versión del servidor:         5.7.19 - MySQL Community Server (GPL)
-- SO del servidor:              Win64
-- HeidiSQL Versión:             9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Volcando estructura de base de datos para supertec
CREATE DATABASE IF NOT EXISTS `supertec` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `supertec`;

-- Volcando estructura para tabla supertec.cliente
CREATE TABLE IF NOT EXISTS `cliente` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Rut` varchar(11) DEFAULT NULL,
  `Nombre` varchar(50) DEFAULT NULL,
  `Usuario` varchar(50) DEFAULT NULL,
  `Contrasenia` varchar(50) DEFAULT NULL,
  `Fecha_Nacimiento` date DEFAULT NULL,
  `Correo` varchar(50) DEFAULT NULL,
  `Telefono` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Rut` (`Rut`),
  UNIQUE KEY `Usuario` (`Usuario`),
  UNIQUE KEY `Correo` (`Correo`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla supertec.cliente: ~0 rows (aproximadamente)
/*!40000 ALTER TABLE `cliente` DISABLE KEYS */;
/*!40000 ALTER TABLE `cliente` ENABLE KEYS */;

-- Volcando estructura para tabla supertec.comuna
CREATE TABLE IF NOT EXISTS `comuna` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Codigo` varchar(50) DEFAULT NULL,
  `Nombre` varchar(50) DEFAULT NULL,
  `Region` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_comuna_region` (`Region`),
  CONSTRAINT `FK_comuna_region` FOREIGN KEY (`Region`) REFERENCES `region` (`Id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla supertec.comuna: ~0 rows (aproximadamente)
/*!40000 ALTER TABLE `comuna` DISABLE KEYS */;
/*!40000 ALTER TABLE `comuna` ENABLE KEYS */;

-- Volcando estructura para tabla supertec.local
CREATE TABLE IF NOT EXISTS `local` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(50) DEFAULT NULL,
  `Ubicacion` varchar(100) DEFAULT NULL,
  `Comuna` int(11) DEFAULT NULL,
  `Tecnico` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK__comuna` (`Comuna`),
  KEY `FK_Local_tecnico` (`Tecnico`),
  CONSTRAINT `FK_Local_tecnico` FOREIGN KEY (`Tecnico`) REFERENCES `tecnico` (`Id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `FK__comuna` FOREIGN KEY (`Comuna`) REFERENCES `comuna` (`Id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla supertec.local: ~0 rows (aproximadamente)
/*!40000 ALTER TABLE `local` DISABLE KEYS */;
/*!40000 ALTER TABLE `local` ENABLE KEYS */;

-- Volcando estructura para tabla supertec.region
CREATE TABLE IF NOT EXISTS `region` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Codigo` varchar(50) DEFAULT NULL,
  `Nombre` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla supertec.region: ~0 rows (aproximadamente)
/*!40000 ALTER TABLE `region` DISABLE KEYS */;
/*!40000 ALTER TABLE `region` ENABLE KEYS */;

-- Volcando estructura para tabla supertec.solicitud
CREATE TABLE IF NOT EXISTS `solicitud` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Caracteristicas` varchar(200) DEFAULT NULL,
  `Estado` varchar(50) DEFAULT NULL,
  `Cliente` int(11) DEFAULT NULL,
  `Tecnico` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_solicitud_cliente` (`Cliente`),
  KEY `FK_solicitud_tecnico` (`Tecnico`),
  CONSTRAINT `FK_solicitud_cliente` FOREIGN KEY (`Cliente`) REFERENCES `cliente` (`Id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `FK_solicitud_tecnico` FOREIGN KEY (`Tecnico`) REFERENCES `tecnico` (`Id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla supertec.solicitud: ~0 rows (aproximadamente)
/*!40000 ALTER TABLE `solicitud` DISABLE KEYS */;
/*!40000 ALTER TABLE `solicitud` ENABLE KEYS */;

-- Volcando estructura para tabla supertec.tecnico
CREATE TABLE IF NOT EXISTS `tecnico` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Rut` varchar(50) DEFAULT NULL,
  `Correo` varchar(50) DEFAULT NULL,
  `Nombre` varchar(50) DEFAULT NULL,
  `Usuario` varchar(50) DEFAULT NULL,
  `Contrasenia` varchar(50) DEFAULT NULL,
  `Especialidad` int(11) DEFAULT NULL,
  `Fecha_Nacimiento` date DEFAULT NULL,
  `Telefono` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Usuario` (`Usuario`),
  UNIQUE KEY `Rut` (`Rut`),
  UNIQUE KEY `Correo` (`Correo`),
  KEY `FK_tecnico_tipo_tecnico` (`Especialidad`),
  CONSTRAINT `FK_tecnico_tipo_tecnico` FOREIGN KEY (`Especialidad`) REFERENCES `tipo_tecnico` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla supertec.tecnico: ~0 rows (aproximadamente)
/*!40000 ALTER TABLE `tecnico` DISABLE KEYS */;
/*!40000 ALTER TABLE `tecnico` ENABLE KEYS */;

-- Volcando estructura para tabla supertec.tipo_tecnico
CREATE TABLE IF NOT EXISTS `tipo_tecnico` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Especialidad` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla supertec.tipo_tecnico: ~1 rows (aproximadamente)
/*!40000 ALTER TABLE `tipo_tecnico` DISABLE KEYS */;
INSERT INTO `tipo_tecnico` (`Id`, `Especialidad`) VALUES
	(1, 'Electronico');
/*!40000 ALTER TABLE `tipo_tecnico` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
