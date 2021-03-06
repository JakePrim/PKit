CREATE DATABASE IF NOT EXISTS `shop` CHARACTER SET ='UTF8';
USE `shop`;
CREATE TABLE IF NOT EXISTS `user`(
	`id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`username` VARCHAR(20) NOT NULL,
	`password` VARCHAR(20) NOT NULL
)ENGINE=InnoDB CHARSET=utf8;

INSERT `user`(`username`,`password`) VALUES('admin','admin');

ALTER TABLE `user`
MODIFY username VARCHAR(20) NOT NULL UNIQUE;


CREATE TABLE IF NOT EXISTS `category`(
	id INT(11) AUTO_INCREMENT PRIMARY KEY,
	cname VARCHAR(20) NOT NULL UNIQUE,
	cdesc VARCHAR(100) NOT NULL
)ENGINE=InnoDB CHARSET=utf8;

 INSERT `category`(cname,cdesc) VALUES('人物画','描述人物的画'),
 ('风景画','风景画'),
 ('动物画','描述动物的画');

CREATE TABLE IF NOT EXISTS `product`(
	`id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`pname` VARCHAR(20) NOT NULL UNIQUE,
	`price` DOUBLE NOT NULL,
	`desc` LONTTEXT NOT NULL,
	`filename` VARCHAR(20) NOT NULL,
	`path` VARCHAR(200) NOT NULL,
	`cid` INT(11) NOT NULL,
	CONSTRAINT `cid_fk` FOREIGN KEY(`cid`) REFERENCES category(id)
)ENGINE=InnoDB CHARSET=utf8;