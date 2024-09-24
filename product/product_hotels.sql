-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: product
-- ------------------------------------------------------
-- Server version	8.0.37

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `hotels`
--

DROP TABLE IF EXISTS `hotels`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hotels` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `location` varchar(255) NOT NULL,
  `max_price` decimal(7,2) DEFAULT NULL,
  `min_price` decimal(7,2) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `number_of_rooms` int NOT NULL,
  `rating` float DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `hotels_chk_1` CHECK ((`max_price` >= 0)),
  CONSTRAINT `hotels_chk_2` CHECK ((`min_price` >= 1)),
  CONSTRAINT `hotels_chk_3` CHECK ((`number_of_rooms` >= 1))
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hotels`
--

LOCK TABLES `hotels` WRITE;
/*!40000 ALTER TABLE `hotels` DISABLE KEYS */;
INSERT INTO `hotels` VALUES (1,'2024-09-24 14:32:25.790519','2024-09-24 14:32:25.790519','One Hotel Dr, Boston, MA 02128, United States',650.00,135.00,'Hilton Boston Logan Airport',604,4.1,'ACTIVE'),(2,'2024-09-24 14:40:40.581123','2024-09-24 14:40:40.581123','320 Washington St, Newton, MA 02458, United States',318.00,148.00,'Four Points by Sheraton Boston Newton',270,3.6,'ACTIVE'),(3,'2024-09-24 14:43:03.098678','2024-09-24 14:43:03.098678','19 Stuart St, Boston, MA 02116, United States',149.00,48.00,'HI Boston Hostel',270,4.4,'ACTIVE'),(4,'2024-09-24 14:44:47.229171','2024-09-24 14:44:47.229171','200 Boylston St, Boston, MA 02116, United States',1263.00,896.00,'Four Seasons Hotel Boston',273,4.7,'ACTIVE');
/*!40000 ALTER TABLE `hotels` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-09-24 14:45:54
