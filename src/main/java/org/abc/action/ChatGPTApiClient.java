package org.abc.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ChatGPTApiClient {
    private static final Log LOG = LogFactory.getLog(ChatGPTApiClient.class);

    private String actionName;

    private String message;

    public String execute(String actionName, String message) throws Exception {

        String prompt = "as a java developer, i want to write the unit test for below function, could you please provide the unit test of below code snippet: " + message;
        this.actionName = prompt;
        this.message = message;

        this.LOG.info("promp is : " + prompt);


        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(Constant.ChatGPT_api_URL);

        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("Authorization", "Bearer " + Constant.ChatGPT_API_KEY);

        String requestBody = "{\"prompt\": prompt, \"max_tokens\": 50, \"temperature\": 0.7, \"model\":\"text-davinci-002\"}";

        StringEntity entity = new StringEntity(requestBody);
        httpPost.setEntity(entity);

        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity responseEntity = response.getEntity();
        String responseString = "";

        if (responseEntity != null) {
            responseString = EntityUtils.toString(responseEntity);
//            System.out.println(responseString);
            this.LOG.info("the response is: " + responseString);
            return responseString;
        }

        return responseString;
    }

}
