# Geek University Android 
## 2 семестр
## Java. Углубленное изучение
14 ноября 2017 MSK (UTC+3)

Курсовой проект:
- Модуль "Server" - серверная часть 
- Модуль "Client" - клиентская часть
- Модуль "Common" - общие классы между клинтом и сервером

Другое:
- Модуль "Lessons" - конспекты уроков и домашние работы (лекции не все законспектированы)


### Настрока серверной части:

1. Создать на MySQL-сервере БД:
```sql
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
```

2. Настройка подключения к БД находятся в модуле {Server} в файле "resources/hibernate.cfg.xml":
```xml
<?xml version='1.0' encoding='utf-8'?>
<!DOCTYPE hibernate-configuration PUBLIC
        "-//Hibernate/Hibernate Configuration DTD//EN"
        "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
    <session-factory>
        <!-- datasource config -->
        <property name="hibernate.connection.driver_class">com.mysql.jdbc.Driver</property>
        <!--<property name="hibernate.connection.url">jdbc:mysql://localhost:3306/flask?useSSL=false</property>-->
        <!-- <property name="hibernate.connection.username">root</property>-->
        <!-- <property name="hibernate.connection.password">password</property>-->
        <property name="hibernate.connection.url">jdbc:mysql://localhost:3306/flask?useSSL=false</property>
        <property name="hibernate.connection.username">root</property>
        <property name="hibernate.connection.password">password</property>
        <property name="hibernate.dialect">org.hibernate.dialect.MySQLDialect</property>

        <!-- entities -->
        <mapping resource="hibernate-mapping/UsersEntity.hbm.xml"/>
        <mapping class="ru.davidlevi.jis.server.database.entities.UsersEntity"/>
    </session-factory>
</hibernate-configuration>
```

3. Файл настроек "server_settings.json":
```json
{
  "server": {
    "hostname": "capricorn",
    "backlog": 100,
    "port": 8189,
    "storage_folder": "/store/david"  
  }
}
```
- "storage_folder" - каталог хранения файлов пользователя
- "hostname" - имя хоста сервера доступное в сети Интернет
- "port" - порт
- "backlog" - максимальное количество подключений к соккет-серверу 
Если файл "server_settings.json" отсутствует, то он создается автоматически с настройками по умолчанию.

### Настрока клиентской части:

1. Файл настроек "сlient_settings.json":
```json
{
  "client": {
    "hostname": "capricorn",
    "port": 8189
  }
}
```
- "hostname" - имя хоста сервера доступное в сети Интернет
- "port" - порт
Если файл "сlient_settings.json" отсутствует, то он создается автоматически с настройками по умолчанию.


