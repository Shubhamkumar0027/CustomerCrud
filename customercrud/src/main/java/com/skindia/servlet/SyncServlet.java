package com.skindia.servlet;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skindia.DAO.CustomerDAO;
import com.skindia.entity.Customer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/sync")
public class SyncServlet extends HttpServlet {

    private final String AUTH_URL = "https://qa.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp";
    private final String CUSTOMER_LIST_URL = "https://qa.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=get_customer_list";
    private final String LOGIN_ID = "test@sunbasedata.com";
    private final String PASSWORD = "Test@123";
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = authenticate();
        if (token != null) {
            List<Customer> remoteCustomers = fetchRemoteCustomers(token);
            if (remoteCustomers != null) {
                CustomerDAO customerDAO = new CustomerDAO();
                for (Customer customer : remoteCustomers) {
                    if (customerDAO.exists(customer.getEmail())) {
                        try {
							customerDAO.updateCustomer(customer);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    } else {
                        try {
							customerDAO.addCustomer(customer);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    }
                }
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to fetch customers");
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Failed to authenticate");
        }
    }

    private String authenticate() throws IOException {
        URL url = new URL(AUTH_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String body = gson.toJson(new AuthRequest(LOGIN_ID, PASSWORD));
        conn.getOutputStream().write(body.getBytes());

        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.readLine();
            AuthResponse authResponse = gson.fromJson(response, AuthResponse.class);
            return authResponse.getToken();
        }
        return null;
    }

    private List<Customer> fetchRemoteCustomers(String token) throws IOException {
        URL url = new URL(CUSTOMER_LIST_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + token);

        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.readLine();
            Type listType = new TypeToken<List<Customer>>(){}.getType();
            return gson.fromJson(response, listType);
        }
        return null;
    }

    // Helper classes for JSON parsing
    private static class AuthRequest {
        private String login_id;
        private String password;

        public AuthRequest(String login_id, String password) {
            this.login_id = login_id;
            this.password = password;
        }
    }

    private static class AuthResponse {
        private String token;

        public String getToken() {
            return token;
        }
    }
}
