package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.dao.MealDAO;
import ru.javawebinar.topjava.dao.MealDAOInMemoryImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import static ru.javawebinar.topjava.util.MealsUtil.filteredByStreams;

public class MealServlet extends HttpServlet {
    public static final String MEAL_FORM_JSP = "meal_form.jsp";
    public static final String MEALS_LIST = "meals.jsp";
    private final MealDAO mealDAO = MealDAOInMemoryImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String forward="";
        String action = req.getParameter("action");

        if (Objects.isNull(action)) {
            List<Meal> meals = mealDAO.findAll();
            List<MealTo> mealsTo = filteredByStreams(meals, LocalTime.of(0, 0), LocalTime.of(23, 59), 2000);

            req.setAttribute("meals", mealsTo);
            forward = MEALS_LIST;
        } else if (action.equalsIgnoreCase("delete")){
            int userId = Integer.parseInt(req.getParameter("mealId"));
            mealDAO.deleteById(userId);
            forward = MEALS_LIST;
            List<Meal> meals = mealDAO.findAll();
            List<MealTo> mealsTo = filteredByStreams(meals, LocalTime.of(0, 0), LocalTime.of(23, 59), 2000);
            req.setAttribute("meals", mealsTo);
        } else if (action.equalsIgnoreCase("edit")){
            forward = MEAL_FORM_JSP;
            int mealId = Integer.parseInt(req.getParameter("mealId"));
            Meal meal = mealDAO.findById(mealId).orElseThrow(NoSuchElementException::new);
            req.setAttribute("meal", meal);
        } else if (action.equalsIgnoreCase("insert")){
            forward = MEAL_FORM_JSP;

        }
        req.getRequestDispatcher(forward).forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
