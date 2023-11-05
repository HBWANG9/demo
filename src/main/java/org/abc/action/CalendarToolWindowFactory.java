package org.abc.action;

import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBPanelWithEmptyText;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

final class CalendarToolWindowFactory implements ToolWindowFactory, DumbAware {

    private static CalendarToolWindowContent toolWindowContent;


    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        toolWindowContent = new CalendarToolWindowContent(toolWindow);
        Content content = ContentFactory.getInstance().createContent(toolWindowContent.getContentPanel(), "", false);
        toolWindow.getContentManager().addContent(content);

    }

    public void displayMessage(String message) {
        toolWindowContent.textArea.setText(message);
    }





    public static class CalendarToolWindowContent {

        private static final String CALENDAR_ICON_PATH = "/toolWindow/Calendar-icon.png";
        private static final String TIME_ZONE_ICON_PATH = "/toolWindow/Time-zone-icon.png";
        private static final String TIME_ICON_PATH = "/toolWindow/Time-icon.png";

        private final JPanel contentPanel = new JPanel();
        private final JLabel currentDate = new JLabel();
        private final JLabel timeZone = new JLabel();
        private final JLabel currentTime = new JLabel();

        private final JTextArea textArea = new JTextArea(10,80);

        public CalendarToolWindowContent(ToolWindow toolWindow) {
            contentPanel.setLayout(new BorderLayout(200, 200));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
            contentPanel.add(createCalendarPanel(), BorderLayout.PAGE_START);
            contentPanel.add(createControlsPanel(toolWindow), BorderLayout.CENTER);
//            updateCurrentDateTime();
        }





        @NotNull
        private JPanel createCalendarPanel() {
            JPanel calendarPanel = new JPanel();
//            calendarPanel.setBackground(Color.RED);
//            setIconLabel(currentDate, CALENDAR_ICON_PATH);
//            setIconLabel(timeZone, TIME_ZONE_ICON_PATH);
//            setIconLabel(currentTime, TIME_ICON_PATH);
//            calendarPanel.add(currentDate);
//            calendarPanel.add(timeZone);
//            calendarPanel.add(currentTime);

            textArea.append("setIconLabel(currentDate, CALENDAR_ICON_PATH);\n" +
                    "            setIconLabel(timeZone, TIME_ZONE_ICON_PATH);\n" +
                    "            setIconLabel(currentTime, TIME_ICON_PATH);\n" +
                    "            calendarPanel.add(currentDate);\n" +
                    "            calendarPanel.add(timeZone);\n" +
                    "            calendarPanel.add(currentTime);");
            textArea.setEditable(false);
            textArea.setLineWrap(true);
//            textArea.setBackground(Color.BLUE);


            JBScrollPane scrollPane = new JBScrollPane(textArea);
            scrollPane.createHorizontalScrollBar();
//            scrollPane.createVerticalScrollBar();
            scrollPane.setAutoscrolls(true);
//            scrollPane.setBackground(Color.YELLOW);

            calendarPanel.add(scrollPane);

            return calendarPanel;
        }

        private void setIconLabel(JLabel label, String imagePath) {
            label.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource(imagePath))));
        }

        @NotNull
        private JPanel createControlsPanel(ToolWindow toolWindow) {
            JPanel controlsPanel = new JPanel();
            JButton refreshDateAndTimeButton = new JButton("Ask ChatGPT");
            refreshDateAndTimeButton.addActionListener(e -> {
                try {
                    updateCurrentDateTime();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            controlsPanel.add(refreshDateAndTimeButton);
            JButton hideToolWindowButton = new JButton("Hide");
            hideToolWindowButton.addActionListener(e -> toolWindow.hide(null));
            controlsPanel.add(hideToolWindowButton);
            return controlsPanel;
        }

        private void updateCurrentDateTime() throws IOException {
//            Calendar calendar = Calendar.getInstance();
//            currentDate.setText(getCurrentDate(calendar));
//            timeZone.setText(getTimeZone(calendar));
//            currentTime.setText(getCurrentTime(calendar));
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet("http://api.openweathermap.org/data/2.5/weather?q=Xi'an&appid=6e70a2c441e22fce5a882fe7eb170e9c");

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

        private String getCurrentDate(Calendar calendar) {
            return calendar.get(Calendar.DAY_OF_MONTH) + "/"
                    + (calendar.get(Calendar.MONTH) + 1) + "/"
                    + calendar.get(Calendar.YEAR);
        }

        private String getTimeZone(Calendar calendar) {
            long gmtOffset = calendar.get(Calendar.ZONE_OFFSET); // offset from GMT in milliseconds
            String gmtOffsetString = String.valueOf(gmtOffset / 3600000);
            return (gmtOffset > 0) ? "GMT + " + gmtOffsetString : "GMT - " + gmtOffsetString;
        }

        private String getCurrentTime(Calendar calendar) {
            return getFormattedValue(calendar, Calendar.HOUR_OF_DAY) + ":" + getFormattedValue(calendar, Calendar.MINUTE);
        }

        private String getFormattedValue(Calendar calendar, int calendarField) {
            int value = calendar.get(calendarField);
            return StringUtils.leftPad(Integer.toString(value), 2, "0");
        }

        public JPanel getContentPanel() {
            return contentPanel;
        }

    }

}