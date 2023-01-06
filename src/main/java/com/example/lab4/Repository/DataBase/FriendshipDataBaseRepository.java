package com.example.lab4.Repository.DataBase;

import com.example.lab4.Domain.Friendship;
import com.example.lab4.Domain.FriendshipForDB;
import com.example.lab4.Domain.User;
import com.example.lab4.Domain.Validators.ValidationException;
import com.example.lab4.Domain.Validators.Validator;
import com.example.lab4.Repository.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.sql.*;

public class FriendshipDataBaseRepository implements Repository<Long, FriendshipForDB> {
    private String url;

    private String username;

    private String password;

    private Validator<FriendshipForDB> validator;

    public FriendshipDataBaseRepository(String url, String username, String password, Validator<FriendshipForDB> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Iterable<FriendshipForDB> findAll() {
        Set<FriendshipForDB> friends = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendship");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long firstID = resultSet.getLong("first_id");
                Long secondID = resultSet.getLong("second_id");
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
                boolean accpeted = resultSet.getBoolean("status");

                FriendshipForDB friendship = new FriendshipForDB(firstID, secondID, date);
                friendship.setId(id);
                friends.add(friendship);
                friendship.setAccepted(accpeted);
            }
            return friends;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friends;
    }

    private FriendshipForDB extractFriendship(ResultSet resultSet) throws SQLException {
        FriendshipForDB friendship;
        if (resultSet.next()) {
            Long id = resultSet.getLong("id");
            Long id1 = resultSet.getLong("first_id");
            Long id2 = resultSet.getLong("second_id");
            LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
            boolean acceptat = resultSet.getBoolean("status");

            friendship = new FriendshipForDB(id1, id2, date);
            friendship.setId(id);
            friendship.setAccepted(acceptat);

            return friendship;
        }
        return null;
    }

    @Override
    public Optional<FriendshipForDB> findOne(Long aLong) {
        if(aLong == null)
            throw new IllegalArgumentException("ID must not be null");
        FriendshipForDB friendship;
        String sql = "select * from friendship where id = ?";

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setLong(1, aLong);
            ResultSet resultSet = ps.executeQuery();

            friendship = extractFriendship(resultSet);
            if(friendship != null)
                return Optional.of(friendship);
        }   catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();

    }


    @Override
    public Optional<FriendshipForDB> save(FriendshipForDB entity) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date1 = entity.getDate().format(formatter);

        if(findOne(entity.getId()).isPresent()){
            throw new ValidationException("Duplicate ID!");
        }
        else{
            validator.validate(entity);
            String sql = "insert into friendship(id, first_id, second_id, date, status) values(?, ?, ?, ?, ?)";

            try(Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement ps = connection.prepareStatement(sql)){
                ps.setLong(1, entity.getId());
                ps.setLong(2, entity.getID1());
                ps.setLong(3, entity.getID2());
                ps.setTimestamp(4, Timestamp.valueOf(entity.getDate()));
                ps.setBoolean(5, entity.isAccepted());
                ps.executeUpdate();
            } catch(SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return Optional.empty();
        }

    @Override
    public Optional<FriendshipForDB> delete(Long aLong) {
        if(aLong == null)
            throw new IllegalArgumentException("ID must not be null");
        Optional<FriendshipForDB> friendship = this.findOne(aLong);
        if(friendship.isPresent()){
            String sql = "delete from friendship where first_id = ? and second_id =?";
            try(Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement ps = connection.prepareStatement(sql)){
                ps.setLong(1, friendship.get().getID1());
                ps.setLong(2, friendship.get().getID2());
                ps.executeUpdate();
            } catch(SQLException throwables) {
                throwables.printStackTrace();
        }

    }return friendship;
        }

    @Override
    public Optional<FriendshipForDB> update(FriendshipForDB entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity must not be null!");
        validator.validate(entity);
        String sql = "update friendship set status = ? where first_id = ? and second_id = ?";
        int rowsNo = 0;

        try(Connection connection = DriverManager.getConnection(url, username, password)){
            PreparedStatement ps = connection.prepareStatement(sql);
            boolean val = entity.isAccepted();
            ps.setBoolean(1, entity.isAccepted());
            ps.setLong(2, entity.getID1());
            ps.setLong(3, entity.getID2());

            rowsNo = ps.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
        if(rowsNo > 0)
            return Optional.empty();
        return Optional.of(entity);
    }

    @Override
    public FriendshipForDB findByUsernameAndPassword(String userName, String password) {
        return null;
    }

    @Override
    public FriendshipForDB findByUsers(Long ID1, Long ID2) {
        FriendshipForDB friendship;
        String sql = "select * from friendship where first_id = ? and second_id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, ID1);
            ps.setLong(2, ID2);
            ResultSet resultSet = ps.executeQuery();

            friendship = extractFriendship(resultSet);
            if (friendship != null)
                return friendship;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<FriendshipForDB> getAllAsList(){
        Iterable<FriendshipForDB> iterable = findAll();
        List<FriendshipForDB> list = new ArrayList<>();
        iterable.forEach(list::add);
        return list;
    }
}
