package com.example.lab4.Repository.DataBase;

import com.example.lab4.Domain.FriendshipForDB;
import com.example.lab4.Domain.User;
import com.example.lab4.Domain.Validators.Validator;
import com.example.lab4.Repository.Repository;

import java.sql.*;
import java.util.*;

public class UserDataBaseRepository implements Repository<Long, User> {

    private String url;

    private String username;

    private String password;

    private Validator<User> validator;


    public UserDataBaseRepository(String url, String username, String password, Validator<User> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String lastName = resultSet.getString("last_name");
                String firstName = resultSet.getString("first_name");
                String userName = resultSet.getString("user_name");
                String pass = resultSet.getString("password");
                User user = new User(lastName, firstName,userName, pass);
                user.setId(id);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public Optional<User> findOne(Long aLong) {

        if(aLong == null)
            throw new IllegalArgumentException("ID must not be null");

        String sql = "select * from users where id = ?";

        try(Connection connection = DriverManager.getConnection(url, username, password);
        PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, Math.toIntExact(aLong));
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
                String lastName = resultSet.getString("last_name");
                String firstName = resultSet.getString("first_name");
                String userName = resultSet.getString("user_name");
                String pass = resultSet.getString("password");

                User user = new User(lastName, firstName, userName, pass);
                user.setId(aLong);
                return Optional.of(user);
            }
        }   catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> save(User entity) {
        String sql = "insert into users (id, last_name, first_name, user_name, password) values (?,?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1,entity.getId());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getFirstName());
            ps.setString(4, entity.getUserName());
            ps.setString(5, entity.getPassword());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> delete(Long aLong) {
        if(aLong == null)
            throw new IllegalArgumentException("Id is null!");
        String s = "delete from users where id = ?";
        String s2 = "delete from friendship where first_id = ? or second_id = ?";
        Optional<User> user = null;

        try (Connection connection = DriverManager.getConnection(url, username, password)){
            PreparedStatement ps = connection.prepareStatement(s);
            PreparedStatement ps2 = connection.prepareStatement(s2);
            user = this.findOne(aLong);
            if(user.isEmpty())
                return Optional.empty();

            ps2.setLong(1, aLong);
            ps2.setLong(2, aLong);
            ps2.executeUpdate();

            ps.setLong(1,aLong);
            ps.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
        return user;

    }

    @Override
    public Optional<User> update(User entity) {
        if(entity == null)
            throw new IllegalArgumentException("Entity must not be null!");
        validator.validate(entity);

        String sql = "update users set last_name = ?, first_name = ?, user_name = ?, password = ? where id = ?";
        int rowsNo = 0;

        try(Connection connection = DriverManager.getConnection(url, username, password)){
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, entity.getLastName());
            ps.setString(2, entity.getFirstName());
            ps.setString(3, entity.getUserName());
            ps.setString(4, entity.getPassword());
            ps.setLong(5, entity.getId());

            rowsNo = ps.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
        if(rowsNo > 0)
            return Optional.empty();
        return Optional.of(entity);
    }

    public User findByUsernameAndPassword(String userName, String userPassword){
        User user;
        try(Connection connection = DriverManager.getConnection(url, username, password);
        PreparedStatement ps = connection.prepareStatement("select * from users where user_name = ? and password = ?")){
            ps.setString(1, userName);
            ps.setString(2, userPassword);
            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()){
                Long id = resultSet.getLong("id");
                String lastName = resultSet.getString("last_name");
                String firstName = resultSet.getString("first_name");
                String userN = resultSet.getString("user_name");
                String pass = resultSet.getString("password");

                user = new User(lastName, firstName, userN, pass);

                user.setId(id);

                return user;
            }
        }   catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User findByUsers(Long ID1, Long ID2) {
        return null;
    }

    @Override
    public List<User> getAllAsList() {
        Iterable<User> iterable = findAll();
        List<User> list = new ArrayList<>();
        iterable.forEach(list::add);
        return list;
    }
}
