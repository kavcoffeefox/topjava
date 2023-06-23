package ru.javawebinar.topjava.dao;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.ConnectionManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MealDaoDBImpl implements MealDao {

    public static final String FIND_ALL_SQL = "SELECT * FROM meals";
    public static final String SAVE_SQL = "INSERT INTO meals (datetime, description, calories) VALUES (?,?,?);";
    public static final String UPDATE_SQL = "UPDATE meals SET datetime=?, description=?, calories=? WHERE id=?;";
    public static final String DELETE_SQL = "DELETE FROM meals WHERE id=?";

    public static final String FIND_BY_ID_SQL = "SELECT * FROM meals WHERE id=?";
    public static final String CREATE_TABLE_MEAL_IF_NOT_EXIST_SQL = "CREATE TABLE IF NOT EXISTS meals (id serial primary key, datetime timestamp, description text, calories int);";
    private static final MealDaoDBImpl INSTANCE = new MealDaoDBImpl();


    public static MealDaoDBImpl getInstance() {
        return INSTANCE;
    }

    {
//        ONLY FOR TESTING
        createTable();
        initTable();
    }

    private void initTable() {
        List<Meal> list = Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );
        list.forEach(this::save);
    }

    private static void createTable() {
        try (Connection connection = ConnectionManager.get();
             Statement statement = connection.createStatement()) {
            statement.execute(CREATE_TABLE_MEAL_IF_NOT_EXIST_SQL);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.getId() != 0) {
            return update(meal);
        }
        return insert(meal);
    }

    private Meal update(Meal meal) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {

            mapMealToPreparedStatement(meal, preparedStatement);

            preparedStatement.setInt(4, meal.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return meal;
    }

    private Meal insert(Meal meal) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            mapMealToPreparedStatement(meal, preparedStatement);
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                meal.setId(generatedKeys.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return meal;
    }

    @Override
    public List<Meal> findAll() {
        List<Meal> result = new ArrayList<>();

        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(mapResultSetToMeal(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public Optional<Meal> findById(Integer id) {
        Optional<Meal> result = Optional.empty();

        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = Optional.of(mapResultSetToMeal(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public void deleteById(Integer id) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Meal mapResultSetToMeal(ResultSet resultSet) throws SQLException {
        return new Meal(
                resultSet.getObject("id", Integer.class),
                resultSet.getObject("datetime", Timestamp.class).toLocalDateTime(),
                resultSet.getObject("description", String.class),
                resultSet.getObject("calories", Integer.class)
        );
    }

    private static void mapMealToPreparedStatement(Meal meal, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setObject(1, Timestamp.valueOf(meal.getDateTime()));
        preparedStatement.setObject(2, meal.getDescription());
        preparedStatement.setObject(3, meal.getCalories());
    }
}