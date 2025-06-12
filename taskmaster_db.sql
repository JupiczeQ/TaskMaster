/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19  Distrib 10.11.11-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: taskmaster
-- ------------------------------------------------------
-- Server version	10.11.11-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `categories_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES
(1,'Praca',2),
(2,'Dom',2),
(3,'Hobby',2),
(4,'Studia',3),
(5,'Zakupy',3),
(6,'Sport',3);
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tasks`
--

DROP TABLE IF EXISTS `tasks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `tasks` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL,
  `description` text DEFAULT NULL,
  `priority` enum('Wysoki','Średni','Niski') DEFAULT 'Średni',
  `status` enum('Do zrobienia','W trakcie','Ukończono') DEFAULT 'Do zrobienia',
  `category_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `category_id` (`category_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `tasks_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`),
  CONSTRAINT `tasks_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tasks`
--

LOCK TABLES `tasks` WRITE;
/*!40000 ALTER TABLE `tasks` DISABLE KEYS */;
INSERT INTO `tasks` VALUES
(1,'Napisać raport','Raport miesięczny dla kierownika','Wysoki','W trakcie',1,2,'2025-06-10 14:24:16'),
(2,'Przygotować prezentację','Prezentacja na spotkanie z klientem','Wysoki','Do zrobienia',1,2,'2025-06-10 14:24:16'),
(3,'Odpowiedzieć na emaile','Sprawdzić skrzynkę i odpowiedzieć na pilne','Średni','Ukończono',1,2,'2025-06-10 14:24:16'),
(4,'Posprzątać mieszkanie','Odkurzyć i przetrzeć kurze','Średni','Do zrobienia',2,2,'2025-06-10 14:24:16'),
(5,'Naprawić kran','Kran w łazience kapie','Niski','Do zrobienia',2,2,'2025-06-10 14:24:16'),
(6,'Przeczytać książkę','Clean Code - Robert Martin','Niski','W trakcie',3,2,'2025-06-10 14:24:16'),
(7,'Nauczyć się gitary','Przećwiczyć nowe akordy','Średni','Do zrobienia',3,2,'2025-06-10 14:24:16'),
(8,'Napisać pracę magisterską','Rozdział 3 - metodologia badań','Wysoki','W trakcie',4,3,'2025-06-10 14:24:24'),
(9,'Przygotować się do egzaminu','Powtórzyć materiał z programowania','Wysoki','Do zrobienia',4,3,'2025-06-10 14:24:24'),
(10,'Oddać książki do biblioteki','Termin mija w piątek','Średni','Do zrobienia',4,3,'2025-06-10 14:24:24'),
(11,'Kupić produkty','Mleko, chleb, masło, jajka','Średni','Do zrobienia',5,3,'2025-06-10 14:24:24'),
(12,'Zrobić zakupy online','Zamówić nowe buty','Niski','Ukończono',5,3,'2025-06-10 14:24:24'),
(13,'Iść na siłownię','Trening nóg i pleców','Średni','Do zrobienia',6,3,'2025-06-10 14:24:24'),
(14,'Pobiegać w parku','5km trasa dookoła jeziora','Niski','W trakcie',6,3,'2025-06-10 14:24:24'),
(15,'Zapisać się na basen','Znaleźć dobry basen w okolicy','Niski','Do zrobienia',6,3,'2025-06-10 14:24:24');
/*!40000 ALTER TABLE `tasks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `password_hash` varchar(255) DEFAULT NULL,
  `salt` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES
(1,'admin','2025-06-10 15:42:40','3ctAoMXCdNy4Glvl70mg4JlgZkabJ9/r6IMwQ5ZyFIc=','9u/MTI1vTk8mO6jJ8cj6hQ=='),
(2,'Test1','2025-06-10 15:44:39','/95deM2TrtheW54f6bFWBab2SoyIeI1HJ/Z6MVHv27g=','oac9VQuAvbpHoKkRLLjOXg=='),
(3,'Test2','2025-06-10 15:44:51','aLgaqtdo7fwXQ2DueUR21vmeT1XrjvoqYgNE8ejJigY=','Ko0ApSHmh0vt4caqamZQpA==');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-10 17:47:18
