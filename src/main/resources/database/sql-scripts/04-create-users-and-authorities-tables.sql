USE `my_golf_club`;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS `users_roles`;
DROP TABLE IF EXISTS `role`;
DROP TABLE IF EXISTS `user`;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE `user` (
  `username` varchar(50) NOT NULL UNIQUE,
  `password` char(60) NOT NULL,
  `enabled` tinyint NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `user` VALUES
	('employee','$2a$12$mjbSTPLi/cLOxxdrFrwcKO5kwpkiRAiB85Hs39Pbj4bA9nfD/ZtFy',1),
	('moderator','$2a$12$wd.0xHxzTtkZAEKFLM3/2eoXzkNcbcTZXEONdyy.udVvKyxXy.0La',1),
	('admin','$2a$12$jGXj8Ve3VwaVrnfwYeB7HOHzPZa9dMPT.WT7cPWT.04o/x0HAplk2',1);

CREATE TABLE `role` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `name` varchar(50) NOT NULL,
    PRIMARY KEY(`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

INSERT INTO `role` (`name`) VALUES
    ('ROLE_EMPLOYEE'),
    ('ROLE_MODERATOR'),
    ('ROLE_ADMIN');

SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE `users_roles` (
  `username` varchar(50) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`username`, `role_id`),
  KEY `FK_ROLE_idx` (`role_id`),

  CONSTRAINT `FK_USER` FOREIGN KEY (`username`)
  REFERENCES `user` (`username`)
  ON DELETE NO ACTION ON UPDATE NO ACTION,

  CONSTRAINT `FK_ROLE` FOREIGN KEY (`role_id`)
  REFERENCES `role` (`id`)
  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO `users_roles` VALUES
	('employee', 1),
	('moderator', 1),
	('moderator', 2),
	('admin', 1),
	('admin', 2),
	('admin', 3);

