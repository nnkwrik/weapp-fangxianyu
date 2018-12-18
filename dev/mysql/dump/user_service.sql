-- MySQL dump 10.13  Distrib 5.7.24, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: user_service
-- ------------------------------------------------------
-- Server version	5.7.24-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `user_service`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `user_service` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `user_service`;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `open_id` varchar(32) DEFAULT '0',
  `nick_name` varchar(32) DEFAULT '',
  `avatar_url` varchar(200) DEFAULT '',
  `gender` int(1) DEFAULT NULL,
  `country` varchar(16) DEFAULT NULL,
  `province` varchar(16) DEFAULT NULL,
  `city` varchar(16) DEFAULT NULL,
  `language` varchar(5) DEFAULT NULL,
  `register_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_open_id_uindex` (`open_id`),
  KEY `open_id` (`open_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (3,'1','测试用户1','https://4.bp.blogspot.com/-gKPdnJWscyI/VCIkF3Po4DI/AAAAAAAAmjo/fAKkTMyf8hM/s170/monster01.png',NULL,NULL,NULL,NULL,NULL,'2018-11-23 09:19:02'),(4,'2','测试用户2','https://1.bp.blogspot.com/-3GUksHO3Sgc/VCIkF-PenoI/AAAAAAAAmjk/by_uxGIO7hU/s170/monster02.png',NULL,NULL,NULL,NULL,NULL,'2018-11-23 10:27:20'),(5,'3','测试用户3','https://4.bp.blogspot.com/-zyd_W4E6BjM/VCIkF1J8sII/AAAAAAAAmjg/X7j73gG6UFs/s170/monster03.png',NULL,NULL,NULL,NULL,NULL,'2018-11-23 10:27:20'),(6,'4','测试用户4','https://4.bp.blogspot.com/-CfSVFwYqpkQ/VCIkGwECcHI/AAAAAAAAmjs/Iksw2cv-43s/s170/monster04.png',NULL,NULL,NULL,NULL,NULL,'2018-11-24 07:49:00'),(9,'5','测试用户5','https://3.bp.blogspot.com/-uF9VsXtCfz0/VCIkHKJUTSI/AAAAAAAAmjw/Zmw2VGrZyyY/s170/monster05.png',NULL,NULL,NULL,NULL,NULL,'2018-11-24 23:48:04'),(10,'6','测试用户6','https://4.bp.blogspot.com/-e-2KBynpWWM/VCIkIH5TMaI/AAAAAAAAmkM/P-wz_w_BaKI/s170/monster06.png',NULL,NULL,NULL,NULL,NULL,'2018-12-09 07:24:24'),(11,'7','测试用户7','https://1.bp.blogspot.com/-Q6gxY-WglOo/VCIkIUdDUkI/AAAAAAAAmkI/jk6SAjSdezo/s170/monster07.png',NULL,NULL,NULL,NULL,NULL,'2018-12-09 07:24:24'),(12,'8','测试用户8','https://4.bp.blogspot.com/-R7qkuOoCs1k/VCIkIbJW75I/AAAAAAAAmkE/pvE5zePq6Tk/s170/monster08.png',NULL,NULL,NULL,NULL,NULL,'2018-12-09 07:24:24'),(13,'9','测试用户9','https://4.bp.blogspot.com/-8DEnZirlVa0/VCIkJQVrQaI/AAAAAAAAmkQ/sYReYG-5cho/s170/monster09.png',NULL,NULL,NULL,NULL,NULL,'2018-12-09 07:24:24'),(14,'10','测试用户10','https://3.bp.blogspot.com/-66UXoSvwaOc/VCIkJiqQVMI/AAAAAAAAmkU/8lvbCe4sz9s/s170/monster10.png',NULL,NULL,NULL,NULL,NULL,'2018-12-09 07:24:24'),(15,'11','测试用户11','https://4.bp.blogspot.com/-HRxya4zR5dU/VCIkKKcirWI/AAAAAAAAmkY/ksdUUIJJdcA/s170/monster11.png',NULL,NULL,NULL,NULL,NULL,'2018-12-09 07:24:24'),(16,'12','测试用户12','https://3.bp.blogspot.com/---bC5f3l67Y/VCIkKXEkX8I/AAAAAAAAmkc/xOSiXCTwebk/s170/monster12.png',NULL,NULL,NULL,NULL,NULL,'2018-12-09 07:24:24');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-12-18 13:42:05
