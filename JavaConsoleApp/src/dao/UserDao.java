package dao;

import model.User;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserDao implements Dao<User> {    ;

    private static final String SELECT_ALL_USERS =
            "select * " +
                    "from users";

    private static final String INSERT_USER_TEMPLATE =
            "insert into users (login) " +
                    "values ('%s');";

    private static final String DELETE_USER_TEMPLATE =
            "delete from users " +
                    "where login = '%s'";

    private static final String GET_USER_BY_LOGIN_TEMPLATE =
            "select * from users where login = '%s'";

    private static final String COUNT_USER_TEMPLATE =
            "select count(*) from users where login = '%s'";

    @Override
    public List<User> getAll() {

        List<User> users = new ArrayList<>();
        try
        {
            Connection con = DbConnector.getConnection();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(SELECT_ALL_USERS);
            while (rs.next()) {
                users.add(mapToUser(rs));
            }
        } catch (SQLException e) {
            System.out.println(e);
            return Collections.emptyList();
        }
        return users;
    }

    @Override
    public void insert(User user) {
        try {
            Connection con = DbConnector.getConnection();
            Statement stm = con.createStatement();
            stm.execute(String.format(INSERT_USER_TEMPLATE, user.getLogin()));
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    @Override
    public void delete(User user) {
        try {
            Connection con = DbConnector.getConnection();
            Statement stm = con.createStatement();
            stm.execute(String.format(DELETE_USER_TEMPLATE, user.getLogin()));
        } catch (SQLException e) {
            System.out.println(e);
        }
    }


    public User getByLogin(String login)
    {
        User user = null;
        try {
            Connection con = DbConnector.getConnection();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(String.format(GET_USER_BY_LOGIN_TEMPLATE, login));
            rs.next();
            user = mapToUser(rs);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return  user;
    }

    public boolean Exists(User user)
    {
        boolean result = false;
        try {
            Connection con = DbConnector.getConnection();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(String.format(COUNT_USER_TEMPLATE, user.getLogin()));
            rs.next();
            if(!(rs.getInt(1) == 0))
                result = true;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return result;
    }


    private static User mapToUser(ResultSet rs) throws SQLException {
        return new User()
                .setId(rs.getInt("id"))
                .setLogin(rs.getString("login"));
    }
}
