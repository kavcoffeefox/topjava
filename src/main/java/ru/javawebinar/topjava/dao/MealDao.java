package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;
import java.util.Optional;

public interface MealDao {
    Meal save(Meal meal);

    List<Meal> findAll();

    Optional<Meal> findById(Integer id);

    void deleteById(Integer id);

}
