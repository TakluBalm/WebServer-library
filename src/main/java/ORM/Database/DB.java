package ORM.Database;

import java.sql.Connection;
import java.sql.SQLException;

public interface DB {
    <T> String insert(T obj) throws IllegalAccessException;
    <T> String  delete(T obj) throws Exception;
    <T> String update(T obj) throws Exception;
    <T> String doesExist(T obj) throws IllegalAccessException;
    <T> String getAll(Class<T>cl);
}
