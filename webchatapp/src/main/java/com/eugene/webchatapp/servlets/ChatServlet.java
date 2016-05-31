package com.eugene.webchatapp.servlets;

import com.eugene.webchatapp.Constants;
import com.eugene.webchatapp.models.Message;
import com.eugene.webchatapp.service.MessageDAOService;
import com.eugene.webchatapp.service.UserDAOService;
import com.eugene.webchatapp.utils.MessageHelper;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by eugene on 27.04.16.
 */

@WebServlet(value = "/chat", asyncSupported = true)
public class ChatServlet extends HttpServlet{

    private MessageDAOService messages;
    private UserDAOService users;

    @Override
    public void init() throws ServletException {
        super.init();
        messages = new MessageDAOService();
        users = new UserDAOService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String query = req.getQueryString();

        if(query == null || query.equals("")){
            resp.sendRedirect("/homepage.jsp");
            return;
        }

        if(query.contains("users")){
            String responseBodyUsers = MessageHelper.buildServerResponseBodyUsers(users.getUsersName());
            resp.getOutputStream().write(responseBodyUsers.getBytes());
            return;
        }

        if(query.contains("search")){

            String textSearch = query.substring(7, query.length());

            String responseBodySearch = MessageHelper.buildServerResponseBodySearch(messages.search(textSearch));
            resp.getOutputStream().write(responseBodySearch.getBytes());

            return;
        }

        String responseBody = MessageHelper.buildServerResponseBody(messages.getMessages(), messages.getMessages().size());

        resp.getOutputStream().write(responseBody.getBytes());

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Message message = MessageHelper.getClientMessage(req.getInputStream());

            messages.addMessage(message);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String requestBody = MessageHelper.inputStreamToString(req.getInputStream());

        try {
            JSONObject jsonObject = MessageHelper.stringToJsonObject(requestBody);

            String messageId = (String) jsonObject.get("id");

            if(messageId != null && !messageId.equals("")){
                messages.removeMessage(messageId);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String query = req.getQueryString();

        if(query == null || !query.equals("editname")){
            try {
                String message = MessageHelper.inputStreamToString(req.getInputStream());

                JSONObject jsonObject = MessageHelper.stringToJsonObject(message);

                Message messageObj = new Message();

                messageObj.setId((String) jsonObject.get(Constants.Message.FIELD_ID));
                messageObj.setText((String) jsonObject.get(Constants.Message.FIELD_TEXT));
                messageObj.setAuthor("");
                messageObj.setTimestamp(0);

                messages.updateMessage(messageObj);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{

            try {

                String requestBody = MessageHelper.inputStreamToString(req.getInputStream());

                JSONObject jsonObject = MessageHelper.stringToJsonObject(requestBody);

                String newName = (String) jsonObject.get("newName");
                String oldName = (String) jsonObject.get("oldName");

                if(!users.isUser(newName)){
                    users.editName(newName, oldName);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }


        }


    }

    private Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();

        for (String queryParam : query.split(Constants.REQUEST_PARAMS_DELIMITER)) {
            String paramKeyValuePair[] = queryParam.split("=");
            if (paramKeyValuePair.length > 1) {
                result.put(paramKeyValuePair[0], paramKeyValuePair[1]);
            } else {
                result.put(paramKeyValuePair[0], "");
            }
        }
        return result;
    }
}
