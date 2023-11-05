package org.abc.action;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.awt.*;

public class MyCustomToolWindow {


    private final JTextArea textArea = new JTextArea(10,80);
    
    
    
    
    public SimpleToolWindowPanel createToolWindowContent(final Project project) {
        SimpleToolWindowPanel panel = new SimpleToolWindowPanel(true, true);
        panel.setBackground(Color.YELLOW);
        panel.setLayout(new BorderLayout(200, 200));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));

        textArea.append("setIconLabel(currentDate, CALENDAR_ICON_PATH);\n" +
                "            setIconLabel(timeZone, TIME_ZONE_ICON_PATH);\n" +
                "            setIconLabel(currentTime, TIME_ICON_PATH);\n" +
                "            calendarPanel.add(currentDate);\n" +
                "            calendarPanel.add(timeZone);\n" +
                "            calendarPanel.add(currentTime);");
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setBackground(Color.BLUE);


        JBScrollPane scrollPane = new JBScrollPane(textArea);
        scrollPane.createHorizontalScrollBar();
//            scrollPane.createVerticalScrollBar();
        scrollPane.setAutoscrolls(true);


        panel.setContent(scrollPane);

        return panel;

    }

    public void displayMessage(String message) {
        textArea.setText(message);
    }


}
