package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDAOInMemoryImpl implements MealDAO {
    private static final AtomicInteger meal_id_generator = new AtomicInteger();
    private static final MealDAOInMemoryImpl INSTANCE = new MealDAOInMemoryImpl();

    private static final List<Meal> meals = new CopyOnWriteArrayList<>();

    static {
        meals.addAll(Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        ));
    }

    public static MealDAOInMemoryImpl getInstance(){
        return INSTANCE;
    }
    private MealDAOInMemoryImpl() {
    }

    @Override
    public Meal save(Meal meal) {
        if (Objects.isNull(meal.getId())){
            meal.setId(meal_id_generator.incrementAndGet());
        }
        deleteById(meal.getId());
        meals.add(meal);
        return meal;
    }

    @Override
    public List<Meal> findAll() {
        return meals;
    }

    @Override
    public Optional<Meal> findById(Integer id) {
        return meals.stream().filter(meal -> id.equals(meal.getId())).findFirst();
    }

    @Override
    public void deleteById(Integer id) {
        findById(id).ifPresent(meals::remove);
    }
}
