DROP USER if exists 'golfadmin'@'%';

CREATE USER 'golfadmin'@'%' IDENTIFIED BY 'golfadmin';

GRANT ALL PRIVILEGES ON * . * TO 'golfadmin'@'%';