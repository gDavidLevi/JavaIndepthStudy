package ru.davidlevi.jis.server.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.type.StringType;
import ru.davidlevi.jis.server.database.entities.UsersEntity;
import ru.davidlevi.jis.server.database.interfaces.InterfaceDatabase;

import java.util.List;
import java.util.UUID;

public class Database implements InterfaceDatabase {
    private Logger logger = LogManager.getLogger(Database.class);

    private Session session;

    @Override
    public void connect() {
        session = HibernateFactory.getSession().openSession();
    }

    @Override
    public boolean createUser(String newLogin, String newPassword, String newNickname, String email) {
        try {
            Transaction transaction = session.beginTransaction();
            UsersEntity entity = new UsersEntity();
            entity.setLogin(newLogin);
            entity.setPassword(newPassword);
            entity.setNickname(newNickname);
            entity.setEmail(email);
            entity.setUuid(UUID.randomUUID().toString());
            session.save(entity);
            transaction.commit();
        } catch (NonUniqueObjectException exception) {
            logger.error(exception);
        }
        //
        String nickDB = "";
        try {
            nickDB = getNickname(newLogin, newPassword);
        } catch (IllegalStateException exception) {
            //logger.error(exception);
            exception.printStackTrace();
        }
        return nickDB.equalsIgnoreCase(newNickname); // true - пользователь есть в таблице
    }

    /* Именнованный запрос из файла UsersEntity.hbm.xml */
    @Override
    public String getNickname(String login, String password) {
        Transaction transaction = session.beginTransaction();
        Query query = session.getNamedQuery("getNickname").
                setParameter("login", login, StringType.INSTANCE).
                setParameter("password", password, StringType.INSTANCE);
        List list = query.list();
        transaction.commit();
        if (list.size() == 0) return null; // null - не найден в таблице Users
        return ((UsersEntity) list.get(0)).getNickname();
    }

    /* Именнованный запрос из файла UsersEntity.hbm.xml */
    @Override
    public String getUuid(String nickname) {
        Transaction transaction = session.beginTransaction();
        Query query = session.getNamedQuery("getUuid").
                setParameter("nickname", nickname, StringType.INSTANCE);
        List list = query.list();
        transaction.commit();
        if (list.size() == 0) return null; // null - не найден в таблице Users
        return ((UsersEntity) list.get(0)).getUuid();
    }

    /* Именнованный запрос из файла UsersEntity.hbm.xml */
    @Override
    public boolean isExistLogin(String login) {
        Transaction transaction = session.beginTransaction();
        Query query = session.getNamedQuery("findLogin").
                setParameter("login", login, StringType.INSTANCE);
        List list = query.list();
        transaction.commit();
        return list.size() != 0; // true - если список не пустой
    }

    @Override
    public void disconnect() {
        session.close();
    }
}
