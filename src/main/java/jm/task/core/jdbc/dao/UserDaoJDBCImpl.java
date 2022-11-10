package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UserDaoJDBCImpl implements UserDao {

    public UserDaoJDBCImpl() {
    }
    public void createUsersTable() throws SQLException {
        Connection connection = Util.getConnection();
        Statement statement = null;
        String query="CREATE TABLE IF NOT EXISTS ITEM(id int NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                "name varchar(50), " +
                "lastName varchar(50), age int)";

        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("Database has been created!");
        } catch (SQLException e) {
            System.err.println("Таблица не создалась ! ");
            e.printStackTrace();
        } finally {
            try{connection.close();
            } catch (SQLException e){
            }
        }
    }

    public void dropUsersTable() {
        Connection connection = Util.getConnection();
        Statement statement = null;
        String query = "DROP TABLE IF EXISTS ITEM";
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("Database has been dropped!");
        } catch (SQLException e) {
            System.err.println("Таблица не удалилась ! ");
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
            }
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        Connection connection = Util.getConnection();
        PreparedStatement statement = null;
        String query = "INSERT INTO ITEM(name, lastName, age) VALUES (?, ?, ?)";
        try {
            statement = connection.prepareStatement(query);
            connection.setAutoCommit(false);
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setInt(3,age);

            statement.executeUpdate();
            connection.commit();
            System.out.println("User с именем – " + name +" добавлен в базу данных" );
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            System.err.println("Таблица не удалилась ! ");
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
            }
        }

    }

    public void removeUserById(long id) {
        Connection connection = Util.getConnection();
        PreparedStatement statement = null;
        String query = "DELETE FROM ITEM WHERE id = ?";
        try {
            statement = connection.prepareStatement(query);
            connection.setAutoCommit(false);
            statement.setLong(1, id);


            statement.executeUpdate();
            connection.commit();
            System.out.println("Data has been removed");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            System.err.println("Таблица не удалилась !");
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
            }
        }
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        Connection connection = Util.getConnection();
        Statement statement = null;
        String query = "SELECT id, name, lastName, age from ITEM";
        try {
            statement = connection.createStatement();
            connection.setAutoCommit(false);
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                User us = new User();
                us.setId(resultSet.getLong("id"));
                us.setName(resultSet.getString("name"));
                us.setLastName(resultSet.getString("lastName"));
                us.setAge(resultSet.getByte("age"));
                userList.add(us);
                connection.commit();
                for(User element : userList){
                    System.out.println(element.toString());
                }
            }

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            System.err.println("Таблица не удалилась ! ");
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
            }
        }
        return userList;
    }

    public void cleanUsersTable() {
        Connection connection = Util.getConnection();
        Statement statement = null;
        String query = "DELETE FROM ITEM";
        try {
            statement = connection.createStatement();
            connection.setAutoCommit(false);
            statement.executeUpdate(query);
            connection.commit();
            System.out.println("Database has been Cleared!");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            System.err.println("Таблица не удалилась !");
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
            }
        }
    }
}
