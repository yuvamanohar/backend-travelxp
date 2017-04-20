# --- !Ups
create table `socialProfiles`(
  `socialProfileId` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `userId` BIGINT REFERENCES users(userId),
  `socialNetwork` varchar(255) NOT NULL,
  `socialNetworkId` varchar(255) NOT NULL,
  `firstName` varchar(255) DEFAULT NULL,
  `middleName` varchar(255) DEFAULT NULL,
  `lastName` varchar(255) DEFAULT NULL,
  `completeName` varchar(255) DEFAULT NULL,
  `profilePic` TEXT DEFAULT NULL,
  `createdAt` DATETIME NOT NULL,
  `updatedAt` DATETIME NOT NULL,
  `softDeleted` tinyint(1) default 0,
  INDEX `idx_social_network_and_id` (`socialNetwork`, `socialNetworkId`),
  INDEX `idx_social_profile_id_soft_deleted` (`socialProfileId`, `softDeleted`),
  INDEX `idx_social_first_name` (`firstName`),
  INDEX `idx_social_last_name` (`lastName`),
  INDEX `idx_social_name`(`completeName`)
) ENGINE=InnoDB;

create table `users`(
  `userId` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `mobile` BIGINT DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `platform` varchar(255) NOT NULL,
  `deviceId` varchar(255) DEFAULT NULL,
  `createdAt` DATETIME NOT NULL,
  `updatedAt` DATETIME NOT NULL,
  `softDeleted` tinyint(1) default 0,
  INDEX `idx_user_id_soft_deleted` (`userId`, `softDeleted`)
) ENGINE=InnoDB;


# --- !Downs
drop table `socialProfiles` ;
drop table `users` ;