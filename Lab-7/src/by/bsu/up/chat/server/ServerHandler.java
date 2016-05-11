package by.bsu.up.chat.server;

import by.bsu.up.chat.Constants;
import by.bsu.up.chat.InvalidTokenException;
import by.bsu.up.chat.common.models.Message;
import by.bsu.up.chat.logging.Logger;
import by.bsu.up.chat.logging.impl.Log;
import by.bsu.up.chat.storage.InMemoryMessageStorage;
import by.bsu.up.chat.storage.Portion;
import by.bsu.up.chat.utils.MessageHelper;
import by.bsu.up.chat.utils.StringUtils;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServerHandler implements HttpHandler {

    private static final Logger logger = Log.create(ServerHandler.class);

    private InMemoryMessageStorage messageStorage = new InMemoryMessageStorage();

    {
        messageStorage.loadMessages();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Response response;

        try {
            response = dispatch(httpExchange);
        } catch (Throwable e) {

            logger.error("An error occurred when dispatching request.", e);
            response = new Response(Constants.RESPONSE_CODE_INTERNAL_SERVER_ERROR, "Error while dispatching message");
        }
        sendResponse(httpExchange, response);

    }

    private Response dispatch(HttpExchange httpExchange) throws ParseException {
        if (Constants.REQUEST_METHOD_GET.equals(httpExchange.getRequestMethod())) {
            return doGet(httpExchange);
        } else if (Constants.REQUEST_METHOD_POST.equals(httpExchange.getRequestMethod())) {
            return doPost(httpExchange);
        } else if (Constants.REQUEST_METHOD_PUT.equals(httpExchange.getRequestMethod())) {
            return doPut(httpExchange);
        } else if (Constants.REQUEST_METHOD_DELETE.equals(httpExchange.getRequestMethod())) {
            return doDelete(httpExchange);
        } else if (Constants.REQUEST_METHOD_OPTIONS.equals(httpExchange.getRequestMethod())) {
            return doOptions(httpExchange);
        } else {
            return new Response(Constants.RESPONSE_CODE_METHOD_NOT_ALLOWED,
                    String.format("Unsupported http method %s", httpExchange.getRequestMethod()));
        }
    }

    private Response doGet(HttpExchange httpExchange) {

        String query = httpExchange.getRequestURI().getQuery();
        if (query == null) {
            return Response.badRequest("Absent query in request");
        }
        Map<String, String> map = queryToMap(query);
        String token = map.get(Constants.REQUEST_PARAM_TOKEN);
        if (StringUtils.isEmpty(token)) {
            return Response.badRequest("Token query parameter is required");
        }
        try {
            int index = MessageHelper.parseToken(token);
            if (index > messageStorage.size()) {
                return Response.badRequest(
                        String.format("Incorrect token in request: %s. Server does not have so many messages", token));
            }

            Portion portion = new Portion(index);
            List<Message> messages = messageStorage.getPortion(portion);

            String responseBody = MessageHelper.buildServerResponseBody(messages, messageStorage.size());
            return Response.ok(responseBody);
        } catch (InvalidTokenException e) {
            return Response.badRequest(e.getMessage());
        }
    }

    private Response doPost(HttpExchange httpExchange) {
        try {
            Message message = MessageHelper.getClientMessage(httpExchange.getRequestBody());
            logger.info(String.format("Received new message from user: %s", message));
            if(messageStorage.addMessage(message)){
                return Response.ok();
            }
            return new Response(Constants.RESPONSE_CODE_BAD_REQUEST, "error add message");
        } catch (ParseException e) {
            logger.error("Could not parse message.", e);
            return new Response(Constants.RESPONSE_CODE_BAD_REQUEST, "Incorrect request body");
        }
    }

    private Response doPut(HttpExchange httpExchange) {

        try {

            String message = MessageHelper.inputStreamToString(httpExchange.getRequestBody());

            JSONObject jsonObject = MessageHelper.stringToJsonObject(message);

            Message messageObj = new Message();

            messageObj.setId((String) jsonObject.get(Constants.Message.FIELD_ID));
            messageObj.setText((String) jsonObject.get(Constants.Message.FIELD_TEXT));
            messageObj.setAuthor("");
            messageObj.setTimestamp(0);

            if(messageStorage.updateMessage(messageObj)){
                return Response.ok();
            }else{
                return Response.withCode(Constants.RESPONSE_CODE_NOT_IMPLEMENTED);
            }

        } catch (ParseException e) {
            logger.error("Could not parse message.", e);
        } catch (FileNotFoundException e) {
            logger.error("error edit from file.", e);
        }

        return Response.withCode(Constants.RESPONSE_CODE_NOT_IMPLEMENTED);
    }

    private Response doDelete(HttpExchange httpExchange) throws ParseException {

        String requestString = MessageHelper.inputStreamToString(httpExchange.getRequestBody());

        JSONObject jsonObject = MessageHelper.stringToJsonObject(requestString);

        String messageId = (String) jsonObject.get("id");

        if(messageId != null && !messageId.equals("")){
            try {
                messageStorage.removeMessage(messageId);
            } catch (FileNotFoundException e) {
                logger.error("Error remove from File", e);
            }
            return Response.ok();
        }
        return new Response(Constants.RESPONSE_CODE_BAD_REQUEST, "Incorrect request body");
    }

    private Response doOptions(HttpExchange httpExchange) {
        httpExchange.getResponseHeaders().add(Constants.REQUEST_HEADER_ACCESS_CONTROL_METHODS,Constants.HEADER_VALUE_ALL_METHODS);
        return Response.ok();
    }

    private void sendResponse(HttpExchange httpExchange, Response response) {
        try (OutputStream os = httpExchange.getResponseBody()) {
            byte[] bytes = response.getBody().getBytes();

            Headers headers = httpExchange.getResponseHeaders();
            headers.add(Constants.REQUEST_HEADER_ACCESS_CONTROL_ORIGIN,"*");
            httpExchange.sendResponseHeaders(response.getStatusCode(), bytes.length);

            os.write( bytes);

        } catch (IOException e) {
            logger.error("Could not send response", e);
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

    /**
     * This method does absolutely the same as
     * {@link ServerHandler#queryToMap(String)} one, but uses
     * Java's 8 Stream API and lambda expressions
     * <p>
     *     It's just as an example. Bu you can use it
     * @param query the query to be parsed
     * @return the map, containing parsed key-value pairs from request
     */
    private Map<String, String> queryToMap2(String query) {
        return Stream.of(query.split(Constants.REQUEST_PARAMS_DELIMITER))
                .collect(Collectors.toMap(
                        keyValuePair -> keyValuePair.split("=")[0],
                        keyValuePair -> keyValuePair.split("=")[1]
                ));
    }
}
