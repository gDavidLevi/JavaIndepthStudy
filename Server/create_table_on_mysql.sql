CREATE TABLE jis.Users
(
  id int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  login varchar(20) NOT NULL,
  password varchar(20) NOT NULL,
  nickname varchar(20) NOT NULL,
  email varchar(320) NOT NULL,
  uuid varchar(37) NOT NULL
);
CREATE UNIQUE INDEX Users_id_uindex ON jis.Users (id);
CREATE UNIQUE INDEX Users_login_uindex ON jis.Users (login);
CREATE UNIQUE INDEX Users_nickname_uindex ON jis.Users (nickname);
CREATE UNIQUE INDEX Users_email_uindex ON jis.Users (email);
CREATE UNIQUE INDEX Users_uuid_uindex ON jis.Users (uuid);
INSERT INTO jis.Users (id, login, password, nickname, email, uuid) VALUES (1, 'admin', 'admin', 'Admin', 'gDavidLevy@gmail.com', '1000');