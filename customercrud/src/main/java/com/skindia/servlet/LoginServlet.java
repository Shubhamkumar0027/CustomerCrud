package com.skindia.servlet;

import com.google.gson.Gson;
import com.skindia.JWT.JwtUtil;

import io.jsonwebtoken.Claims;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/auth/login")
public class LoginServlet extends HttpServlet {
    private Gson gson;

    @Override
    public void init() throws ServletException {
        gson = new Gson();
        System.out.println("LoginServlet initialized");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String json = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
        Map<String, String> credentials = gson.fromJson(json, HashMap.class);
        String username = credentials.get("username");
        String password = credentials.get("password");

        if ("admin".equals(username) && "password".equals(password)) {
            String token = JwtUtil.generateToken(username);
            request.getSession().setAttribute("token", token);

            // Forward to customer list page
            request.getRequestDispatcher("/customer-list.html").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
