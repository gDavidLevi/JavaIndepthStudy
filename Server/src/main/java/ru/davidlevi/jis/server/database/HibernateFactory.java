package ru.davidlevi.jis.server.database;import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;import org.hibernate.SessionFactory;import org.hibernate.boot.MetadataSources;import org.hibernate.boot.registry.StandardServiceRegistry;import org.hibernate.boot.registry.StandardServiceRegistryBuilder;public class HibernateFactory {    private static Logger logger = LogManager.getLogger(HibernateFactory.class);    public static SessionFactory getSession() {        return session;    }    private static SessionFactory session = buildSession();    private static SessionFactory buildSession() {        logger.info("Hibernate is building...");        final StandardServiceRegistry standardServiceRegistry = new StandardServiceRegistryBuilder()                .configure() // hibernate.cfg.xml                .build();        try {            session = new MetadataSources(standardServiceRegistry).                    buildMetadata().                    buildSessionFactory();        } catch (Exception exception) {            StandardServiceRegistryBuilder.destroy(standardServiceRegistry);            logger.error(exception);            throw new ExceptionInInitializerError(exception);        }        logger.info("Hibernate created.");        return session;    }    public static void shutdown() {        logger.info("Hibernate stoped.");        getSession().close();    }}