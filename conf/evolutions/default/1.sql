# --- !Ups
create table `users`(
  `userId` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `aboutMe` TEXT DEFAULT NULL,
  `mobile` BIGINT DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `platform` varchar(255) NOT NULL,
  `deviceId` varchar(255) DEFAULT NULL,
  `createdAt` DATETIME NOT NULL,
  `updatedAt` DATETIME NOT NULL,
  `softDeleted` tinyint(1) default 0,
  INDEX `idx_user_id_soft_deleted` (`userId`, `softDeleted`)
) ENGINE=InnoDB;

create table `socialProfiles`(
  `socialProfileId` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `userId` BIGINT NOT NULL,
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
  CONSTRAINT `idx_social_network_and_id` UNIQUE (`socialNetwork`, `socialNetworkId`),
  INDEX `idx_social_profile_id_soft_deleted` (`socialProfileId`, `softDeleted`),
  INDEX `idx_social_first_name` (`firstName`),
  INDEX `idx_social_last_name` (`lastName`),
  INDEX `idx_social_name`(`completeName`),
  CONSTRAINT fk_sp_user FOREIGN KEY (`userId`) REFERENCES users(`userId`)
) ENGINE=InnoDB;

create table `albums`(
  `albumId` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` varchar(255) NOT NULL,
  `description` TEXT DEFAULT NULL,
  `userId` BIGINT NOT NULL,
  `createdAt` DATETIME NOT NULL,
  `updatedAt` DATETIME NOT NULL,
  `softDeleted` tinyint(1) default 0,
  INDEX `idx_post_soft_deleted` (`albumId`, `softDeleted`),
  CONSTRAINT fk_album_user FOREIGN KEY (`userId`) REFERENCES users(`userId`)
) ENGINE=InnoDB;

create table `posts`(
  `postId` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `userId` BIGINT NOT NULL,
  `albumId` BIGINT,
  `scribble` TEXT DEFAULT NULL,
  `latitude` DOUBLE DEFAULT NULL,
  `longitude` DOUBLE DEFAULT NULL,
  `location` TEXT DEFAULT NULL,
  `likes` INT DEFAULT NULL,
  `comments` INT DEFAULT NULL,
  `shares` INT DEFAULT NULL,
  `createdAt` DATETIME NOT NULL,
  `updatedAt` DATETIME NOT NULL,
  `softDeleted` tinyint(1) default 0,
  INDEX `idx_post_soft_deleted` (`postId`, `softDeleted`),
  INDEX `idx_post_lat` (`latitude`),
  INDEX `idx_post_long` (`longitude`),
  INDEX `idx_post_updatedAt` (`updatedAt`),
  CONSTRAINT fk_post_user FOREIGN KEY (`userId`) REFERENCES users(`userId`),
  CONSTRAINT fk_post_album FOREIGN KEY (`albumId`) REFERENCES albums(`albumId`)
) ENGINE=InnoDB;

create table `postDetails`(
  `postDetailId` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `postId` BIGINT NOT NULL,
  `media` TEXT NOT NULL,
  `mediaType` ENUM('GIF', 'PHOTO', 'VIDEO') NOT NULL,
  `createdAt` DATETIME NOT NULL,
  `updatedAt` DATETIME NOT NULL,
  `softDeleted` tinyint(1) default 0,
  INDEX `idx_post_soft_deleted` (`postDetailId`, `softDeleted`),
  CONSTRAINT fk_post_detail_post FOREIGN KEY (`postId`) REFERENCES posts(`postId`)
) ENGINE=InnoDB;


# --- !Downs
drop table `postDetails` ;
drop table `posts` ;
drop table `albums` ;
drop table `socialProfiles` ;
drop table `users` ;