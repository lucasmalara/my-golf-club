DROP TABLE IF EXISTS `golf_club_member`;

CREATE TABLE `golf_club_member` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `active_member` boolean DEFAULT FALSE NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

INSERT INTO `golf_club_member` VALUES 
	(1,'Emma','Green','emmagreen@mail.com',true),
	(2,'Anna','Barsky','annab@mail.de',false),
	(3,'Richard','Dunkins','rdunkins@mail.com',true),
	(4,'Thomas','Terra','thomas@terra.me',true),
	(5,'Edwin','Vega','edwin.vega@maily.com',false);