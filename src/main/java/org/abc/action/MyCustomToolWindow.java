package org.abc.action;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.components.JBScrollPane;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MyCustomToolWindow {


    private final JTextArea textArea = new JTextArea(20,80);
    private final JTextField textField = new JTextField();
    
    
    
    
    public SimpleToolWindowPanel createToolWindowContent(final Project project) {
        SimpleToolWindowPanel panel = new SimpleToolWindowPanel(true, true);
//        panel.setBackground(Color.YELLOW);
//        panel.setLayout(new BorderLayout(200, 200));
//        panel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));

        textArea.append("This is default text;\n" +
                "            This is default text;\n" +
                "            This is default text;\n" +
                "            This is default text;\n" +
                "            This is default text;\n" +
                "            This is default text;");
        textArea.setEditable(false);
        textArea.setLineWrap(true);
//        textArea.setBackground(Color.BLUE);
        textArea.setFont(new Font("Calibri", Font.PLAIN, 15));


        JBScrollPane scrollPane = new JBScrollPane(textArea);
        scrollPane.createHorizontalScrollBar();
//            scrollPane.createVerticalScrollBar();
        scrollPane.setAutoscrolls(true);


//        panel.setContent(scrollPane);
        panel.add(scrollPane,BorderLayout.PAGE_START);
        panel.add(createControlsPanel(), BorderLayout.CENTER);


        return panel;

    }

    public void displayMessage(String message) {
        textArea.setText(message);
    }

    private JPanel createControlsPanel() {
        JPanel controlsPanel = new JPanel();
//        controlsPanel.setBackground(Color.RED);
        JTextPane cityNamePane = new JTextPane();
        cityNamePane.setText("City: ");
        controlsPanel.add(cityNamePane);

        textField.setColumns(10);
        textField.setName("City:");
        controlsPanel.add(textField);


        JButton actionButton = new JButton("Ask ChatGPT");
        actionButton.addActionListener(e -> {
            try {
                cityWeather();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        controlsPanel.add(actionButton);
//        JButton hideToolWindowButton = new JButton("Hide");
//        hideToolWindowButton.addActionListener(e -> toolWindow.hide(null));
//        controlsPanel.add(hideToolWindowButton);
        return controlsPanel;
    }

    private void cityWeather() throws IOException {

        HttpClient httpClient = HttpClients.createDefault();
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + textField.getText() + "&appid=6e70a2c441e22fce5a882fe7eb170e9c";
        HttpGet httpGet = new HttpGet(url);

        HttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        HttpEntity responseEntity = response.getEntity();
        String responseString = EntityUtils.toString(responseEntity);

        textArea.setText(responseString);


    }


}
