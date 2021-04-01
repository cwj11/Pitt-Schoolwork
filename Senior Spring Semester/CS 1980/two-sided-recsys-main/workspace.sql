-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Feb 25, 2021 at 07:06 PM
-- Server version: 10.4.17-MariaDB
-- PHP Version: 8.0.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `workspace`
--

-- --------------------------------------------------------

--
-- Table structure for table `businesses`
--

CREATE TABLE `businesses` (
  `business_id` int(11) NOT NULL,
  `business_name` varchar(50) NOT NULL,
  `preferred_age_min` int(3) NOT NULL COMMENT 'Range 0 to 999. Must be less than/equal to max age. ',
  `preferred_age_max` int(3) NOT NULL COMMENT 'Range 0 to 999. Must be greater than/equal to min.',
  `preferred_gender` varchar(50) NOT NULL COMMENT '"Male", "Female", "Other"',
  `preferred_occupation` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `business_movie_item`
--

CREATE TABLE `business_movie_item` (
  `movie_id` int(11) NOT NULL,
  `business_id` int(11) NOT NULL,
  `movie_name` int(50) NOT NULL,
  `movie_genre` varchar(50) NOT NULL,
  `movie_length` int(50) NOT NULL COMMENT 'In minutes',
  `movie_rating` int(4) NOT NULL COMMENT 'Scale 0-5',
  `release_year` int(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `coupon_list`
--

CREATE TABLE `coupon_list` (
  `coupon_id` int(11) NOT NULL,
  `business_id` int(11) NOT NULL,
  `coupon_desc` mediumtext DEFAULT NULL,
  `exp_date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `customers`
--

CREATE TABLE `customers` (
  `customer_id` int(11) NOT NULL,
  `age` int(3) NOT NULL COMMENT 'Range 0 to 999',
  `gender` varchar(50) NOT NULL COMMENT '"Male", "Female", "Other"',
  `occupation` varchar(50) NOT NULL,
  `first_name` int(50) NOT NULL,
  `last_name` int(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `customer_movie_preferences`
--

CREATE TABLE `customer_movie_preferences` (
  `user_id` int(11) NOT NULL,
  `movie_genre` varchar(50) NOT NULL,
  `movie_length` int(50) NOT NULL COMMENT 'In minutes',
  `release_year` int(4) NOT NULL,
  `movie_rating` decimal(4,0) NOT NULL COMMENT 'Scored from 0 to 5. '
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `experts`
--

CREATE TABLE `experts` (
  `expert_id` int(11) NOT NULL,
  `expert_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `recommendation_list`
--

CREATE TABLE `recommendation_list` (
  `recommendation_id` int(11) NOT NULL,
  `business_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `coupon_id` int(11) NOT NULL,
  `total_score` decimal(4,0) DEFAULT NULL COMMENT 'Score 0-5 ',
  `customer_score` decimal(4,0) DEFAULT NULL COMMENT 'Score 0-5',
  `business_score` decimal(4,0) DEFAULT NULL COMMENT 'Score 0-5',
  `expert_score` decimal(10,0) DEFAULT NULL COMMENT 'Score 0-5'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(100) NOT NULL,
  `user_type` varchar(50) NOT NULL COMMENT '"customer", "business", "expert"'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `businesses`
--
ALTER TABLE `businesses`
  ADD PRIMARY KEY (`business_id`),
  ADD KEY `business_id` (`business_id`);

--
-- Indexes for table `business_movie_item`
--
ALTER TABLE `business_movie_item`
  ADD PRIMARY KEY (`movie_id`,`business_id`),
  ADD KEY `business_id` (`business_id`);

--
-- Indexes for table `coupon_list`
--
ALTER TABLE `coupon_list`
  ADD PRIMARY KEY (`coupon_id`),
  ADD KEY `business_id` (`business_id`);

--
-- Indexes for table `customers`
--
ALTER TABLE `customers`
  ADD PRIMARY KEY (`customer_id`),
  ADD KEY `customer_id` (`customer_id`);

--
-- Indexes for table `customer_movie_preferences`
--
ALTER TABLE `customer_movie_preferences`
  ADD PRIMARY KEY (`user_id`);

--
-- Indexes for table `experts`
--
ALTER TABLE `experts`
  ADD PRIMARY KEY (`expert_id`),
  ADD KEY `expert_id` (`expert_id`);

--
-- Indexes for table `recommendation_list`
--
ALTER TABLE `recommendation_list`
  ADD PRIMARY KEY (`recommendation_id`),
  ADD KEY `business_id` (`business_id`,`customer_id`),
  ADD KEY `coupon_id` (`coupon_id`),
  ADD KEY `customer_id` (`customer_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `businesses`
--
ALTER TABLE `businesses`
  ADD CONSTRAINT `fk_business_id` FOREIGN KEY (`business_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `business_movie_item`
--
ALTER TABLE `business_movie_item`
  ADD CONSTRAINT `fk_business_id_movie` FOREIGN KEY (`business_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `coupon_list`
--
ALTER TABLE `coupon_list`
  ADD CONSTRAINT `fk_business_id_coupon` FOREIGN KEY (`business_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `customers`
--
ALTER TABLE `customers`
  ADD CONSTRAINT `fk_customer_id` FOREIGN KEY (`customer_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `customer_movie_preferences`
--
ALTER TABLE `customer_movie_preferences`
  ADD CONSTRAINT `fk_customer_id_movie` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `experts`
--
ALTER TABLE `experts`
  ADD CONSTRAINT `fk_expert_id` FOREIGN KEY (`expert_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `recommendation_list`
--
ALTER TABLE `recommendation_list`
  ADD CONSTRAINT `fk_business_id_recommendation` FOREIGN KEY (`business_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `fk_coupon_id_recommendation` FOREIGN KEY (`coupon_id`) REFERENCES `coupon_list` (`coupon_id`),
  ADD CONSTRAINT `fk_customer_id_recommendation` FOREIGN KEY (`customer_id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
