package dao;


import java.util.List;

public interface Dao<T> {
    /**
     * SELECT * from ...
     */
    List<T> getAll();


    /**
     * INSERT INTO ...
     */
    void insert(T t);


    /**
     * DELETE FROM ...
     */

    void delete(T t);
}