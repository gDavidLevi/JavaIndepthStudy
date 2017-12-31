CREATE TABLE flask.Users
(
  id int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  login varchar(20) NOT NULL,
  password varchar(20) NOT NULL,
  nickname varchar(20) NOT NULL,
  email varchar(320) NOT NULL,
  uuid varchar(37) NOT NULL
);
CREATE UNIQUE INDEX Users_id_uindex ON flask.Users (id);
CREATE UNIQUE INDEX Users_login_uindex ON flask.Users (login);
CREATE UNIQUE INDEX Users_nickname_uindex ON flask.Users (nickname);
CREATE UNIQUE INDEX Users_email_uindex ON flask.Users (email);
CREATE UNIQUE INDEX Users_uuid_uindex ON flask.Users (uuid);
INSERT INTO flask.Users (id, login, password, nickname, email, uuid) VALUES (1, 'admin', 'admin', 'I''am admin', 'gDavidLevy@gmail.com', '1000');