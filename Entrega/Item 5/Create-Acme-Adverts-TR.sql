start transaction;


drop database if exists `Acme-Adverts`;
create database `Acme-Adverts`;

use `Acme-Adverts`;

create user 'acme-user'@'%' 
	identified by password '*4F10007AADA9EE3DBB2CC36575DFC6F4FDE27577';

create user 'acme-manager'@'%' 
	identified by password '*FDB8CD304EB2317D10C95D797A4BD7492560F55F';

grant select, insert, update, delete 
	on `Acme-Adverts`.* to 'acme-user'@'%';

grant select, insert, update, delete, create, drop, references, index, alter, 
        create temporary tables, lock tables, create view, create routine, 
        alter routine, execute, trigger, show view
    on `Acme-Adverts`.* to 'acme-manager'@'%';


-- MySQL dump 10.13  Distrib 5.5.29, for Win64 (x86)
--
-- Host: localhost    Database: Acme-Adverts
-- ------------------------------------------------------
-- Server version	5.5.29

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
-- Table structure for table `administrator`
--

DROP TABLE IF EXISTS `administrator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `administrator` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `middle_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `user_account` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_7ohwsa2usmvu0yxb44je2lge` (`user_account`),
  CONSTRAINT `FK_7ohwsa2usmvu0yxb44je2lge` FOREIGN KEY (`user_account`) REFERENCES `user_account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `administrator`
--

LOCK TABLES `administrator` WRITE;
/*!40000 ALTER TABLE `administrator` DISABLE KEYS */;
INSERT INTO `administrator` VALUES (51,0,'C/ Atlántica, 1 (Málaga)','josgamred@',NULL,'Antonio','+34611987654','https://www.malagahoy.es/2017/12/22/ocio/Antonio-Banderas-manana-Ayuntamiento-Malaga_1202290380_78709800_667x375.jpg','Banderas',45),(52,0,'C/ Atlántic city , 2 (Málaga)','anres@',NULL,'Antonio','+34611587654','http://es.web.img3.acsta.net/r_1280_720/pictures/15/03/24/09/51/124386.jpg','Resiner',46);
/*!40000 ALTER TABLE `administrator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `billboard_file`
--

DROP TABLE IF EXISTS `billboard_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `billboard_file` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `file_type` varchar(255) DEFAULT NULL,
  `contract` int(11) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_jx8ccpxjbg6jdvwvfwbf8kl7b` (`contract`),
  CONSTRAINT `FK_jx8ccpxjbg6jdvwvfwbf8kl7b` FOREIGN KEY (`contract`) REFERENCES `contract` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `billboard_file`
--

LOCK TABLES `billboard_file` WRITE;
/*!40000 ALTER TABLE `billboard_file` DISABLE KEYS */;
INSERT INTO `billboard_file` VALUES (71,0,'BILLBOARD',70,'https://i.ibb.co/kGgVz69/Untitled.png','In the right'),(72,0,'BILLBOARD',69,'https://i.ibb.co/kGgVz69/Untitled.png','In the right right');
/*!40000 ALTER TABLE `billboard_file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contract`
--

DROP TABLE IF EXISTS `contract`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contract` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `hash` varchar(255) DEFAULT NULL,
  `signed_customer` datetime DEFAULT NULL,
  `signed_manager` datetime DEFAULT NULL,
  `text` text,
  `request` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_2pn4l92oq6y9v4f92sgmdiciy` (`request`),
  CONSTRAINT `FK_2pn4l92oq6y9v4f92sgmdiciy` FOREIGN KEY (`request`) REFERENCES `request` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contract`
--

LOCK TABLES `contract` WRITE;
/*!40000 ALTER TABLE `contract` DISABLE KEYS */;
INSERT INTO `contract` VALUES (69,0,NULL,NULL,NULL,'Text predefined',64),(70,0,'5f71a4931538f5747141d0d2f5949355','2019-01-07 20:48:00','2019-01-07 19:48:00','Text predefined',65);
/*!40000 ALTER TABLE `contract` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `middle_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `user_account` int(11) NOT NULL,
  `cvv` int(11) DEFAULT NULL,
  `expiration_month` int(11) DEFAULT NULL,
  `expiration_year` int(11) DEFAULT NULL,
  `holder` varchar(255) DEFAULT NULL,
  `make` varchar(255) DEFAULT NULL,
  `number` varchar(255) DEFAULT NULL,
  `vat` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_mbvdes9ypo1yu76so76owiyqx` (`user_account`),
  CONSTRAINT `FK_mbvdes9ypo1yu76so76owiyqx` FOREIGN KEY (`user_account`) REFERENCES `user_account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (55,0,'C/ Atlalanta , 5 (Málaga)','ancar@ancar.com',NULL,'Antonio','+34611987254','https://ep01.epimg.net/elpais/imagenes/2018/01/24/gente/1516786992_958284_1516788998_noticia_normal.jpg','Carmona',47,177,2,22,'José Gamiz Redondo','VISA','4764042457713154','XX00000001'),(56,0,'C/ Alcala, 6 (Málaga)','anflo@anflo.com',NULL,'Antonio','+34611987854','https://i.avoz.es/default/2018/05/30/00121527696546907258717/Foto/antonioflores.jpg','Flores',48,366,11,20,'Carlos Albalat Heredia','MASTERCARD','4544963188930071','XX00000002');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `file`
--

DROP TABLE IF EXISTS `file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `file` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `file_type` varchar(255) DEFAULT NULL,
  `contract` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_5taqcu8wba19v5icnvfi1bhcd` (`contract`),
  CONSTRAINT `FK_5taqcu8wba19v5icnvfi1bhcd` FOREIGN KEY (`contract`) REFERENCES `contract` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file`
--

LOCK TABLES `file` WRITE;
/*!40000 ALTER TABLE `file` DISABLE KEYS */;
/*!40000 ALTER TABLE `file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `finder`
--

DROP TABLE IF EXISTS `finder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `finder` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `cache_update` datetime DEFAULT NULL,
  `key_word` varchar(255) DEFAULT NULL,
  `max_date` datetime DEFAULT NULL,
  `max_price` double DEFAULT NULL,
  `min_date` datetime DEFAULT NULL,
  `min_price` double DEFAULT NULL,
  `customer` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_i9i5l2bcvnc58jsb1fbufcowo` (`customer`),
  CONSTRAINT `FK_i9i5l2bcvnc58jsb1fbufcowo` FOREIGN KEY (`customer`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `finder`
--

LOCK TABLES `finder` WRITE;
/*!40000 ALTER TABLE `finder` DISABLE KEYS */;
INSERT INTO `finder` VALUES (86,0,NULL,NULL,NULL,NULL,NULL,NULL,55),(87,0,'2019-08-07 10:00:00',NULL,NULL,640,NULL,NULL,56);
/*!40000 ALTER TABLE `finder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `finder_packages`
--

DROP TABLE IF EXISTS `finder_packages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `finder_packages` (
  `finder` int(11) NOT NULL,
  `packages` int(11) NOT NULL,
  KEY `FK_ny3ef45n7dve6a230vxpbupge` (`packages`),
  KEY `FK_cijpc7wf9w396yt91f5s0rq6k` (`finder`),
  CONSTRAINT `FK_cijpc7wf9w396yt91f5s0rq6k` FOREIGN KEY (`finder`) REFERENCES `finder` (`id`),
  CONSTRAINT `FK_ny3ef45n7dve6a230vxpbupge` FOREIGN KEY (`packages`) REFERENCES `package` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `finder_packages`
--

LOCK TABLES `finder_packages` WRITE;
/*!40000 ALTER TABLE `finder_packages` DISABLE KEYS */;
/*!40000 ALTER TABLE `finder_packages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequences`
--

DROP TABLE IF EXISTS `hibernate_sequences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hibernate_sequences` (
  `sequence_name` varchar(255) DEFAULT NULL,
  `sequence_next_hi_value` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequences`
--

LOCK TABLES `hibernate_sequences` WRITE;
/*!40000 ALTER TABLE `hibernate_sequences` DISABLE KEYS */;
INSERT INTO `hibernate_sequences` VALUES ('domain_entity',1);
/*!40000 ALTER TABLE `hibernate_sequences` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `info_file`
--

DROP TABLE IF EXISTS `info_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `info_file` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `file_type` varchar(255) DEFAULT NULL,
  `contract` int(11) DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_tg02ts0s5bkktjibdd5x85imc` (`contract`),
  CONSTRAINT `FK_tg02ts0s5bkktjibdd5x85imc` FOREIGN KEY (`contract`) REFERENCES `contract` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `info_file`
--

LOCK TABLES `info_file` WRITE;
/*!40000 ALTER TABLE `info_file` DISABLE KEYS */;
INSERT INTO `info_file` VALUES (73,0,'INFO',70,'Info file 1 very very very very long text','Info file 1 title'),(74,0,'INFO',70,'Info file 2 very very very very long text','Info file 2 title'),(75,0,'INFO',69,'Info file 3 very very very very long text','Info file 3 title');
/*!40000 ALTER TABLE `info_file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `manager`
--

DROP TABLE IF EXISTS `manager`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `manager` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `middle_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `user_account` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_7fkueol8lcnvrbgwmmmyvsytv` (`user_account`),
  CONSTRAINT `FK_7fkueol8lcnvrbgwmmmyvsytv` FOREIGN KEY (`user_account`) REFERENCES `user_account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `manager`
--

LOCK TABLES `manager` WRITE;
/*!40000 ALTER TABLE `manager` DISABLE KEYS */;
INSERT INTO `manager` VALUES (53,0,'C/ Atar , 3 (Málaga)','antor@antor.com',NULL,'Antonio','+34611687654','https://upload.wikimedia.org/wikipedia/commons/thumb/8/85/Antonio_de_la_Torre_en_los_Premios_Goya_2017_cr2.jpg/265px-Antonio_de_la_Torre_en_los_Premios_Goya_2017_cr2.jpg','Torre',49),(54,0,'C/ Atranca , 4 (Málaga)','anoro@anoro.com',NULL,'Antonio','+34611787654','https://www.lecturas.com/medio/2017/09/21/antonio-orozco_647862d6_800x490.jpg','Orozco',50);
/*!40000 ALTER TABLE `manager` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `package`
--

DROP TABLE IF EXISTS `package`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `package` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `final_mode` bit(1) DEFAULT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `ticker` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `manager` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_5t0w8wdmjh387er5vyygqgwkb` (`ticker`),
  KEY `FK_em4ljitn9wpf4m0ig8mcj9ne7` (`manager`),
  CONSTRAINT `FK_em4ljitn9wpf4m0ig8mcj9ne7` FOREIGN KEY (`manager`) REFERENCES `manager` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `package`
--

LOCK TABLES `package` WRITE;
/*!40000 ALTER TABLE `package` DISABLE KEYS */;
INSERT INTO `package` VALUES (57,0,'A billboard 3x4 m','2020-08-10 10:00:00','','https://tinyurl.com/acme-billboards',600,'2019-08-10 10:00:00','YF54TF-89654','This is my best billboard',53),(58,0,'A billboard 3x4 m','2019-08-12 10:00:00','','https://tinyurl.com/acme-billboards',650,'2019-08-11 10:00:00','YF54TF-79654','This is my second best billboard',53),(59,0,'A billboard 3x4 m','2020-08-10 10:00:00','\0','https://tinyurl.com/acme-billboards',660,'2020-08-01 10:00:00','YF54TF-69654','This is my  worst billboard',53),(60,0,'A billboard 3x4 m','2020-08-10 10:00:00','','https://tinyurl.com/acme-billboards',670,'2020-08-08 10:00:00','YF54TF-59654','This is my best billboard',54),(61,0,'A billboard 3x4 m','2020-08-10 10:00:00','\0','https://tinyurl.com/acme-billboards',680,'2019-08-10 10:00:00','YF54TF-49654','This is my second best billboard',54),(62,0,'A billboard 3x4 m','2020-08-10 10:00:00','\0','https://tinyurl.com/acme-billboards',500,'2019-08-10 10:00:00','YF99TF-49654','This is my worst billboard',53);
/*!40000 ALTER TABLE `package` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `radio_file`
--

DROP TABLE IF EXISTS `radio_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `radio_file` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `file_type` varchar(255) DEFAULT NULL,
  `contract` int(11) DEFAULT NULL,
  `broadcaster_name` varchar(255) DEFAULT NULL,
  `schedule` varchar(255) DEFAULT NULL,
  `sound` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_6wos80xng18yr60j3rbhplbi5` (`contract`),
  CONSTRAINT `FK_6wos80xng18yr60j3rbhplbi5` FOREIGN KEY (`contract`) REFERENCES `contract` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `radio_file`
--

LOCK TABLES `radio_file` WRITE;
/*!40000 ALTER TABLE `radio_file` DISABLE KEYS */;
INSERT INTO `radio_file` VALUES (79,0,'RADIO',70,'Ni lo sé, ni me importa','De lunes a viernes a las 8:00 am','https://www.fakeurlsound.com'),(80,0,'RADIO',70,'Ni siquiera sé el nombre de la emisora','Todos los días a las 20:30','https://www.fakeurlsound.com'),(81,0,'RADIO',69,'Pato Donalds','Todos los días a las 1:00 am','https://www.fakeurlsound.com'),(82,0,'RADIO',69,'COPE','Todos los días a las 1:00 am','https://www.fakeurlsound.com');
/*!40000 ALTER TABLE `radio_file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `request`
--

DROP TABLE IF EXISTS `request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `request` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `comments_customer` varchar(255) DEFAULT NULL,
  `comments_manager` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `customer` int(11) DEFAULT NULL,
  `pack` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_6nifky7xv85rirbs5vmlr4l4w` (`customer`),
  KEY `FK_ffm6aom5su11gsavn4tv9r815` (`pack`),
  CONSTRAINT `FK_ffm6aom5su11gsavn4tv9r815` FOREIGN KEY (`pack`) REFERENCES `package` (`id`),
  CONSTRAINT `FK_6nifky7xv85rirbs5vmlr4l4w` FOREIGN KEY (`customer`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `request`
--

LOCK TABLES `request` WRITE;
/*!40000 ALTER TABLE `request` DISABLE KEYS */;
INSERT INTO `request` VALUES (63,0,'I want put a new imagen',NULL,'REJECTED',55,58),(64,0,'I want put a new image','Ok','APPROVED',55,57),(65,0,'I want put a old image','Ok','APPROVED',56,58),(66,0,'I want put some images',NULL,'REJECTED',56,57),(67,0,'I want put some images',NULL,'PENDING',55,60),(68,0,'I want put a world premier',NULL,'PENDING',56,60);
/*!40000 ALTER TABLE `request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `social_network_file`
--

DROP TABLE IF EXISTS `social_network_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `social_network_file` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `file_type` varchar(255) DEFAULT NULL,
  `contract` int(11) DEFAULT NULL,
  `banner` varchar(255) DEFAULT NULL,
  `target` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_nd4y8eo6dc910qrriycuveuf6` (`contract`),
  CONSTRAINT `FK_nd4y8eo6dc910qrriycuveuf6` FOREIGN KEY (`contract`) REFERENCES `contract` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `social_network_file`
--

LOCK TABLES `social_network_file` WRITE;
/*!40000 ALTER TABLE `social_network_file` DISABLE KEYS */;
INSERT INTO `social_network_file` VALUES (76,0,'SOCIALNETWORK',70,'https://i-cdn.phonearena.com/images/article/104992-two_lead/Twitter-details-its-new-API-breaks-certain-features-of-third-party-Twitter-apps.jpg','https://www.twitter.com'),(77,0,'SOCIALNETWORK',70,'https://www.trecebits.com/wp-content/uploads/2013/06/facebook.jpg','https://www.facebook.com'),(78,0,'SOCIALNETWORK',69,'https://parque-corredor.com/web/wp-content/uploads/2015/10/logo-cinesa.jpg','https://www.cinesa.com');
/*!40000 ALTER TABLE `social_network_file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_configuration`
--

DROP TABLE IF EXISTS `system_configuration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `system_configuration` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `banner` varchar(255) DEFAULT NULL,
  `country_code` varchar(255) DEFAULT NULL,
  `makers` varchar(255) DEFAULT NULL,
  `max_results` int(11) DEFAULT NULL,
  `system_name` varchar(255) DEFAULT NULL,
  `time_results_cached` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_configuration`
--

LOCK TABLES `system_configuration` WRITE;
/*!40000 ALTER TABLE `system_configuration` DISABLE KEYS */;
INSERT INTO `system_configuration` VALUES (88,0,'https://i.ibb.co/ZxXQz58/Untitled.png','+034','VISA,MASTER,DINNERS,AMEX',10,'Acme BillBoards',1);
/*!40000 ALTER TABLE `system_configuration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_configuration_breach_notification`
--

DROP TABLE IF EXISTS `system_configuration_breach_notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `system_configuration_breach_notification` (
  `system_configuration` int(11) NOT NULL,
  `breach_notification` varchar(255) DEFAULT NULL,
  `breach_notification_key` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`system_configuration`,`breach_notification_key`),
  CONSTRAINT `FK_ayrxtmu4hqnpdg16dr66ftfun` FOREIGN KEY (`system_configuration`) REFERENCES `system_configuration` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_configuration_breach_notification`
--

LOCK TABLES `system_configuration_breach_notification` WRITE;
/*!40000 ALTER TABLE `system_configuration_breach_notification` DISABLE KEYS */;
/*!40000 ALTER TABLE `system_configuration_breach_notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_configuration_welcome_message`
--

DROP TABLE IF EXISTS `system_configuration_welcome_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `system_configuration_welcome_message` (
  `system_configuration` int(11) NOT NULL,
  `welcome_message` varchar(255) DEFAULT NULL,
  `welcome_message_key` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`system_configuration`,`welcome_message_key`),
  CONSTRAINT `FK_afo2sg9l0wrwhjs6s7qjxwgjg` FOREIGN KEY (`system_configuration`) REFERENCES `system_configuration` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_configuration_welcome_message`
--

LOCK TABLES `system_configuration_welcome_message` WRITE;
/*!40000 ALTER TABLE `system_configuration_welcome_message` DISABLE KEYS */;
INSERT INTO `system_configuration_welcome_message` VALUES (88,'Welcome to Acme Adverts! It\'s time to reach beyond the billboards!','EN'),(88,'“¡Bienvenidos a Acme Adverts! Es hora de llegar más allá de los carteles','SP');
/*!40000 ALTER TABLE `system_configuration_welcome_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tvfile`
--

DROP TABLE IF EXISTS `tvfile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tvfile` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `file_type` varchar(255) DEFAULT NULL,
  `contract` int(11) DEFAULT NULL,
  `broadcaster_name` varchar(255) DEFAULT NULL,
  `schedule` varchar(255) DEFAULT NULL,
  `video` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_finfndrc1cq0lh0kfa46v7xc3` (`contract`),
  CONSTRAINT `FK_finfndrc1cq0lh0kfa46v7xc3` FOREIGN KEY (`contract`) REFERENCES `contract` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tvfile`
--

LOCK TABLES `tvfile` WRITE;
/*!40000 ALTER TABLE `tvfile` DISABLE KEYS */;
INSERT INTO `tvfile` VALUES (83,0,'TV',70,'Y a quién le importa?','S y D 10:10','https://www.fakeurlvideo.com'),(84,0,'TV',70,'Ni siquiera sé el nombre de la cadena','L-V a las 14:25','https://www.fakeurlvideo.com'),(85,0,'TV',69,'DMAX','L-D todo el día','https://www.fakeurlvideo.com');
/*!40000 ALTER TABLE `tvfile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_account`
--

DROP TABLE IF EXISTS `user_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_account` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_castjbvpeeus0r8lbpehiu0e4` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_account`
--

LOCK TABLES `user_account` WRITE;
/*!40000 ALTER TABLE `user_account` DISABLE KEYS */;
INSERT INTO `user_account` VALUES (45,0,'21232f297a57a5a743894a0e4a801fc3','admin'),(46,0,'e00cf25ad42683b3df678c61f42c6bda','admin1'),(47,0,'ffbc4675f864e0e9aab8bdf7a0437010','customer1'),(48,0,'5ce4d191fd14ac85a1469fb8c29b7a7b','customer2'),(49,0,'c240642ddef994358c96da82c0361a58','manager1'),(50,0,'8df5127cd164b5bc2d2b78410a7eea0c','manager2');
/*!40000 ALTER TABLE `user_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_account_authorities`
--

DROP TABLE IF EXISTS `user_account_authorities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_account_authorities` (
  `user_account` int(11) NOT NULL,
  `authority` varchar(255) DEFAULT NULL,
  KEY `FK_pao8cwh93fpccb0bx6ilq6gsl` (`user_account`),
  CONSTRAINT `FK_pao8cwh93fpccb0bx6ilq6gsl` FOREIGN KEY (`user_account`) REFERENCES `user_account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_account_authorities`
--

LOCK TABLES `user_account_authorities` WRITE;
/*!40000 ALTER TABLE `user_account_authorities` DISABLE KEYS */;
INSERT INTO `user_account_authorities` VALUES (45,'ADMIN'),(46,'ADMIN'),(47,'CUSTOMER'),(48,'CUSTOMER'),(49,'MANAGER'),(50,'MANAGER');
/*!40000 ALTER TABLE `user_account_authorities` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-09-10 12:51:07


commit;
