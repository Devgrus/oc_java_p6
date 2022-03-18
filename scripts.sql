CREATE DATABASE IF NOT EXISTS paymybuddy;

CREATE TABLE IF NOT EXISTS `paymybuddy`.`member` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(255) NOT NULL,
    `nickname` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `role` VARCHAR(255) NOT NULL,
    `bank_account` VARCHAR(255),
    `amount` DECIMAL(20,2) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE (`username`));

    CREATE TABLE IF NOT EXISTS `paymybuddy`.`transaction_bank` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `member_id` BIGINT NOT NULL,
    `amount` DECIMAL(20,2) NOT NULL,
    `fee` DECIMAL(20,2) NOT NULL,
    `transaction_time` DATETIME NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `member_id`
    FOREIGN KEY (`member_id`)
    REFERENCES `paymybuddy`.`member` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE IF NOT EXISTS `paymybuddy`.`transaction_member` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `amount` DECIMAL(20,2) NOT NULL,
    `fee` DECIMAL(20,2) NOT NULL,
    `transaction_time` DATETIME NOT NULL,
    `description` VARCHAR(255) NULL,
    `remitter_id` BIGINT NOT NULL,
    `receiver_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `remitter`
    FOREIGN KEY (`remitter_id`)
    REFERENCES `paymybuddy`.`member` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `receiver`
    FOREIGN KEY (`receiver_id`)
    REFERENCES `paymybuddy`.`member` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE IF NOT EXISTS `paymybuddy`.`connections`(
    `member_id` BIGINT NOT NULL,
    `connection_id` BIGINT NOT NULL,
    CONSTRAINT `member`
    FOREIGN KEY (`member_id`)
    REFERENCES `paymybuddy`.`member` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `connection`
    FOREIGN KEY (`connection_id`)
    REFERENCES `paymybuddy`.`member` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);