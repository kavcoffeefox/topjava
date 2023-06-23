package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoInMemoryImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import static ru.javawebinar.topjava.util.MealsUtil.filteredByStreams;

public class MealServlet extends HttpServlet {
    public static final String MEAL_FORM_JSP = "meal_form.jsp";
    public static final String MEALS_LIST = "meals.jsp";
    private final MealDao mealDAO = MealDaoInMemoryImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

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
        resp.setContentType("text/html");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        req.setCharacterEncoding("UTF-8");

        LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("datetime"));
        int calories = Integer.parseInt(req.getParameter("calories"));
        String description = req.getParameter("description");
        Meal meal = new Meal(
                dateTime,
                description,
                calories
        );
        Optional<String> id = Optional.ofNullable(req.getParameter("id"));
        id.filter(i -> !i.equals(""))
                .map(Integer::parseInt)
                .ifPresent(meal::setId);
        mealDAO.save(meal);

        List<Meal> meals = mealDAO.findAll();
        List<MealTo> mealsTo = filteredByStreams(meals, LocalTime.of(0, 0), LocalTime.of(23, 59), 2000);
        req.setAttribute("meals", mealsTo);
        req.getRequestDispatcher(MEALS_LIST).forward(req,resp);
    }
}
