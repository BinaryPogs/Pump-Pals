package com.pumppals.database;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.function.Function;
import java.util.function.Supplier;

public class TransactionManager
{
    public static <T> T executeInTransaction(Function<Session, T> action) {
        Session session = DatabaseManager.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            T result = action.apply(session);
            tx.commit();
            return result;
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
    public static void executeInTransaction(Runnable action) {
        try (Session session = DatabaseManager.getSessionFactory().openSession()){
            Transaction tx = session.beginTransaction();
            try {
                action.run();
                tx.commit();
            } catch (Exception ex) {
                tx.rollback();
                throw ex;
            }
        }
    }
}
