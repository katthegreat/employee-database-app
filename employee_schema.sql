-- MySQL dump 10.13  Distrib 9.3.0, for macos15.2 (arm64)
--
-- Host: localhost    Database: employeeData
-- ------------------------------------------------------
-- Server version	9.2.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `address` (
  `empid` int NOT NULL,
  `street` varchar(100) DEFAULT NULL,
  `city_id` int NOT NULL,
  `state_id` int NOT NULL,
  `zip` varchar(15) DEFAULT NULL,
  `gender` varchar(15) DEFAULT NULL,
  `race` varchar(25) DEFAULT NULL,
  `DOB` date DEFAULT NULL,
  `phone_number` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`empid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
INSERT INTO `address` VALUES (1,'123 Doghouse Lane',1,1,'30301','Male','Beagle','1990-05-15','555-1111'),(2,'456 Peanuts Street',1,1,'30302','Male','Human','1985-08-22','555-2222'),(3,'789 Psychiatry Ave',1,1,'30303','Female','Human','1988-03-10','555-3333'),(4,'321 Baseball Blvd',1,1,'30304','Female','Human','1982-11-30','555-4444'),(5,'654 Blanket Way',1,1,'30305','Male','Human','1995-07-04','555-5555'),(6,'987 Dusty Road',1,1,'30306','Male','Human','1993-09-18','555-6666'),(7,'100 Mystery Lane',2,2,'10001','Male','Great Dane','1975-02-14','555-7777'),(8,'200 Sandwich Street',2,2,'10002','Male','Human','1978-06-25','555-8888'),(9,'300 Glasses Avenue',2,2,'10003','Female','Human','1976-12-05','555-9999'),(10,'400 Fashion Blvd',2,2,'10004','Female','Human','1980-04-20','555-0000'),(11,'500 Carrot Lane',2,2,'10005','Male','Rabbit','1965-01-01','555-1212'),(12,'600 Pond Street',2,2,'10006','Male','Duck','1968-07-12','555-1313'),(13,'700 Farm Road',2,2,'10007','Male','Pig','1970-09-08','555-1414'),(14,'800 Hunting Trail',2,2,'10008','Male','Human','1972-10-31','555-1515'),(15,'900 Mars Avenue',2,2,'10009','Male','Martian','1963-04-22','555-1616');
/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `city`
--

DROP TABLE IF EXISTS `city`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `city` (
  `city_id` int NOT NULL,
  `name_of_city` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`city_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `city`
--

LOCK TABLES `city` WRITE;
/*!40000 ALTER TABLE `city` DISABLE KEYS */;
/*!40000 ALTER TABLE `city` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `division`
--

DROP TABLE IF EXISTS `division`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `division` (
  `ID` int NOT NULL,
  `Name` varchar(100) DEFAULT NULL,
  `city` varchar(50) NOT NULL,
  `addressLine1` varchar(50) NOT NULL,
  `addressLine2` varchar(50) DEFAULT NULL,
  `state` varchar(50) DEFAULT NULL,
  `country` varchar(50) NOT NULL,
  `postalCode` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `division`
--

LOCK TABLES `division` WRITE;
/*!40000 ALTER TABLE `division` DISABLE KEYS */;
INSERT INTO `division` VALUES (1,'Technology Engineering','Atlanta','200 17th Street NW','','GA','USA','30363'),(2,'Marketing','Atlanta','200 17th Street NW','','GA','USA','30363'),(3,'Human Resources','New York','45 West 57th Street','','NY','USA','00034'),(4,'HQ','New York','45 West 57th Street','','NY','USA','00034');
/*!40000 ALTER TABLE `division` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_division`
--

DROP TABLE IF EXISTS `employee_division`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_division` (
  `empid` int NOT NULL,
  `div_ID` int NOT NULL,
  PRIMARY KEY (`empid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_division`
--

LOCK TABLES `employee_division` WRITE;
/*!40000 ALTER TABLE `employee_division` DISABLE KEYS */;
INSERT INTO `employee_division` VALUES (1,4),(2,4),(3,4),(4,4),(5,1),(6,2),(7,1),(8,1),(9,1),(10,1),(11,2),(12,2),(13,2),(14,3),(15,2);
/*!40000 ALTER TABLE `employee_division` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_job_titles`
--

DROP TABLE IF EXISTS `employee_job_titles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_job_titles` (
  `empid` int NOT NULL,
  `job_title_id` int NOT NULL,
  PRIMARY KEY (`empid`,`job_title_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_job_titles`
--

LOCK TABLES `employee_job_titles` WRITE;
/*!40000 ALTER TABLE `employee_job_titles` DISABLE KEYS */;
INSERT INTO `employee_job_titles` VALUES (1,901),(2,900),(3,102),(4,101),(5,201),(6,102),(7,100),(8,202),(9,202),(10,900),(11,901),(12,902),(13,103),(14,103),(15,201);
/*!40000 ALTER TABLE `employee_job_titles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employees`
--

DROP TABLE IF EXISTS `employees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employees` (
  `empid` int NOT NULL,
  `Fname` varchar(65) NOT NULL,
  `Lname` varchar(65) NOT NULL,
  `email` varchar(65) NOT NULL,
  `HireDate` date DEFAULT NULL,
  `Salary` decimal(10,2) NOT NULL,
  `SSN` varchar(12) DEFAULT NULL,
  PRIMARY KEY (`empid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employees`
--

LOCK TABLES `employees` WRITE;
/*!40000 ALTER TABLE `employees` DISABLE KEYS */;
INSERT INTO `employees` VALUES (1,'Snoopy','Beagle','Snoopy@example.com','2022-08-01',600000.00,'111-11-1111'),(2,'Charlie','Brown','Charlie@example.com','2022-07-01',48000.00,'111-22-1111'),(3,'Lucy','Doctor','Lucy@example.com','2022-07-03',55297.10,'111-33-1111'),(4,'Pepermint','Patti','Peppermint@example.com','2022-08-02',98529.39,'111-44-1111'),(5,'Linus','Blanket','Linus@example.com','2022-09-01',43232.29,'111-55-1111'),(6,'PigPin','Dusty','PigPin@example.com','2022-10-01',33178.26,'111-66-1111'),(7,'Scooby','Doo','Scooby@example.com','1973-07-03',78421.35,'111-77-1111'),(8,'Shaggy','Rodgers','Shaggy@example.com','1973-07-11',77415.95,'111-88-1111'),(9,'Velma','Dinkley','Velma@example.com','1973-07-21',82442.96,'111-99-1111'),(10,'Daphne','Blake','Daphne@example.com','1973-07-30',59318.71,'111-00-1111'),(11,'Bugs','Bunny','Bugs@example.com','1934-07-01',18097.23,'222-11-1111'),(12,'Daffy','Duck','Daffy@example.com','1935-04-01',16086.43,'333-11-1111'),(13,'Porky','Pig','Porky@example.com','1935-08-12',16639.41,'444-11-1111'),(14,'Elmer','Fudd','Elmer@example.com','1934-08-01',15583.74,'555-11-1111'),(15,'Marvin','Martian','Marvin@example.com','1937-05-01',28011.20,'777-11-1111');
/*!40000 ALTER TABLE `employees` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `job_titles`
--

DROP TABLE IF EXISTS `job_titles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `job_titles` (
  `job_title_id` int DEFAULT NULL,
  `job_title` varchar(125) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job_titles`
--

LOCK TABLES `job_titles` WRITE;
/*!40000 ALTER TABLE `job_titles` DISABLE KEYS */;
INSERT INTO `job_titles` VALUES (100,'software manager'),(101,'software architect'),(102,'software engineer'),(103,'software developer'),(200,'marketing manager'),(201,'marketing associate'),(202,'marketing assistant'),(900,'Chief Exec. Officer'),(901,'Chief Finn. Officer'),(902,'Chief Info. Officer');
/*!40000 ALTER TABLE `job_titles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payroll`
--

DROP TABLE IF EXISTS `payroll`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payroll` (
  `payID` int DEFAULT NULL,
  `pay_date` date DEFAULT NULL,
  `earnings` decimal(8,2) DEFAULT NULL,
  `fed_tax` decimal(7,2) DEFAULT NULL,
  `fed_med` decimal(7,2) DEFAULT NULL,
  `fed_SS` decimal(7,2) DEFAULT NULL,
  `state_tax` decimal(7,2) DEFAULT NULL,
  `retire_401k` decimal(7,2) DEFAULT NULL,
  `health_care` decimal(7,2) DEFAULT NULL,
  `empid` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payroll`
--

LOCK TABLES `payroll` WRITE;
/*!40000 ALTER TABLE `payroll` DISABLE KEYS */;
INSERT INTO `payroll` VALUES (1,'2025-01-31',865.38,276.92,12.55,53.65,103.85,3.46,26.83,1),(2,'2025-02-28',865.38,276.92,12.55,53.65,103.85,3.46,26.83,1),(16,'2025-01-31',1500.00,480.00,21.75,93.00,180.00,6.00,46.50,7);
/*!40000 ALTER TABLE `payroll` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `state`
--

DROP TABLE IF EXISTS `state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `state` (
  `state_id` int NOT NULL,
  `name_of_state` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`state_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `state`
--

LOCK TABLES `state` WRITE;
/*!40000 ALTER TABLE `state` DISABLE KEYS */;
/*!40000 ALTER TABLE `state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `role` enum('admin','employee') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin1','adminpass','admin'),(2,'emp1','emppass','employee');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'employeeData'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-23 18:42:35
