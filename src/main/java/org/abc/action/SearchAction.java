package org.abc.action;

import com.intellij.ide.BrowserUtil;
import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class SearchAction extends AnAction
{
    /**
     * Convert selected text to a URL friendly string.
     * @param e
     */
    private static final Log LOG = LogFactory.getLog(ChatGPTApiClient.class);

    public ChatGPTApiClient client  = new ChatGPTApiClient();



    @Override
    public void actionPerformed(AnActionEvent e)
    {
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        CaretModel caretModel = editor.getCaretModel();

        this.LOG.info("i'm here====");

        // For searches from the editor, we should also get file type information
        // to help add scope to the search using the Stack overflow search syntax.
        //
        // https://stackoverflow.com/help/searching

        String languageTag = "";
        PsiFile file = e.getData(CommonDataKeys.PSI_FILE);
        if(file != null)
        {
            Language lang = e.getData(CommonDataKeys.PSI_FILE).getLanguage();
            languageTag = "+[" + lang.getDisplayName().toLowerCase() + "]";
        }

        // The update method below is only called periodically so need
        // to be careful to check for selected text
        if(caretModel.getCurrentCaret().hasSelection())
        {
            String query = caretModel.getCurrentCaret().getSelectedText().replace(' ', '+') + languageTag;

            HttpClient httpClient = HttpClients.createDefault();
//            HttpPost httpPost = new HttpPost(Constant.ChatGPT_api_URL);
//
//            httpPost.addHeader("Content-Type", "application/json");
//            httpPost.addHeader("Authorization", "Bearer " + Constant.ChatGPT_API_KEY);
//
//            String requestBody = "{\"prompt\": prompt, \"max_tokens\": 50, \"temperature\": 0.7}";
//
//            StringEntity entity = null;
//            try {
//                entity = new StringEntity(requestBody);
//            } catch (UnsupportedEncodingException ex) {
//                throw new RuntimeException(ex);
//            }
//            httpPost.setEntity(entity);
//
//            HttpResponse response = null;
//            try {
//                response = httpClient.execute(httpPost);
//            } catch (IOException ex) {
//                throw new RuntimeException(ex);
//            }

            HttpGet httpGet = new HttpGet("http://api.openweathermap.org/data/2.5/weather?q=Xi'an&appid=6e70a2c441e22fce5a882fe7eb170e9c");

            HttpResponse response = null;
            try {
                response = httpClient.execute(httpGet);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }


            HttpEntity responseEntity = response.getEntity();
            try {
                String responseString = EntityUtils.toString(responseEntity);
//                BrowserUtil.browse("https://stackoverflow.com/search?q=" + query);
//                CalendarToolWindowFactory toolWindowFactory = ApplicationManager.getApplication().getComponent(CalendarToolWindowFactory.class);
//                toolWindowFactory.displayMessage(responseString);

                Project project = e.getData(PlatformDataKeys.PROJECT);
                MyCustomToolWindowFactory.ProjectService projectService = ServiceManager.getService(project, MyCustomToolWindowFactory.ProjectService.class);
                MyCustomToolWindow toolWindow = projectService.getMyCustomToolWindow();
//                toolWindow.displayMessage(responseString);
                toolWindow.displayMessage(query);


            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }




        }
    }

    public String execute(String actionName, String message) throws Exception {

        String prompt = "as a java developer, i want to write the unit test for below function, could you please provide the unit test of below code snippet: " + message;

//        this.LOG.info("promp is : " + prompt);


        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(Constant.ChatGPT_api_URL);

        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("Authorization", "Bearer " + Constant.ChatGPT_API_KEY);

        String requestBody = "{\"prompt\": prompt, \"max_tokens\": 50, \"temperature\": 0.7}";

        StringEntity entity = new StringEntity(requestBody);
        httpPost.setEntity(entity);

        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity responseEntity = response.getEntity();
        String responseString = "";

        if (responseEntity != null) {
            responseString = EntityUtils.toString(responseEntity);
//            System.out.println(responseString);
//            this.LOG.info("the response is: " + responseString);
            return responseString;
        }

        return responseString;
    }



    /**
     * Only make this action visible when text is selected.
     * @param e
     */
    @Override
    public void update(AnActionEvent e)
    {
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        CaretModel caretModel = editor.getCaretModel();
        e.getPresentation().setEnabledAndVisible(caretModel.getCurrentCaret().hasSelection());
    }
}