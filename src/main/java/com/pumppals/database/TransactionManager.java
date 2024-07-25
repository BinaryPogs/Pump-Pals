package com.pumppals.database;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.function.Supplier;

public class TransactionManager
{
    public static <T> T executeInTransaction(Supplier<T> action) {
        try (Session session = DatabaseManager.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                T result = action.get(); // This is where action is executed
                tx.commit();
                return result;
            } catch (Exception ex){
                tx.rollback();
                throw ex;
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
