package com.skindia.servlet;

import com.google.gson.Gson;
import com.skindia.DAO.CustomerDAO;
import com.skindia.entity.Customer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/customer/*")
public class CustomerServlet extends HttpServlet {
    private CustomerDAO customerDAO;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        customerDAO = new CustomerDAO();
        gson = new Gson();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String json = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
            Customer customer = gson.fromJson(json, Customer.class);
            customerDAO.addCustomer(customer);
            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String json = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
            Customer customer = gson.fromJson(json, Customer.class);
            customerDAO.updateCustomer(customer);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            // Get all customers
            String search = request.getParameter("search");
            String sort = request.getParameter("sort");
            int page = Integer.parseInt(request.getParameter("page"));
            int pageSize = Integer.parseInt(request.getParameter("pageSize"));
            try {
                List<Customer> customers = customerDAO.getAllCustomers(page, pageSize, search, sort);
                String json = gson.toJson(customers);
                response.setContentType("application/json");
                response.getWriter().write(json);
            } catch (SQLException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            }
        } else {
            // Get a customer by ID
            String idStr = pathInfo.substring(1);
            int id = Integer.parseInt(idStr);
            try {
                Customer customer = customerDAO.getCustomerById(id);
                if (customer != null) {
                    String json = gson.toJson(customer);
                    response.setContentType("application/json");
                    response.getWriter().write(json);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            } catch (SQLException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && !pathInfo.equals("/")) {
            String idStr = pathInfo.substring(1);
            int id = Integer.parseInt(idStr);
            try {
                customerDAO.deleteCustomer(id);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } catch (SQLException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
