package org.abc.action;

import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiFile;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.IOException;

/**
 * Action class to demonstrate how to interact with the IntelliJ Platform.
 * The only action this class performs is to provide the user with a popup dialog as feedback.
 * Typically this class is instantiated by the IntelliJ Platform framework based on declarations
 * in the plugin.xml file. But when added at runtime this class is instantiated by an action group.
 */
public class PopupDialogAction extends AnAction {

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }

    /**
     * This default constructor is used by the IntelliJ Platform framework to instantiate this class based on plugin.xml
     * declarations. Only needed in {@link PopupDialogAction} class because a second constructor is overridden.
     *
     * @see AnAction#AnAction()
     */
    public PopupDialogAction() {
        super();
    }

    /**
     * This constructor is used to support dynamically added menu actions.
     * It sets the text, description to be displayed for the menu item.
     * Otherwise, the default AnAction constructor is used by the IntelliJ Platform.
     *
     * @param text        The text to be displayed as a menu item.
     * @param description The description of the menu item.
     * @param icon        The icon to be used with the menu item.
     */
    public PopupDialogAction(@Nullable String text, @Nullable String description, @Nullable Icon icon) {
        super(text, description, icon);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        // Using the event, create and show a dialog
//        Project currentProject = event.getProject();
//        StringBuilder message =
//                new StringBuilder(event.getPresentation().getText() + " Selected!");
//        // If an element is selected in the editor, add info about it.
//        Navigatable selectedElement = event.getData(CommonDataKeys.NAVIGATABLE);
//        if (selectedElement != null) {
//            message.append("\nSelected Element: ").append(selectedElement);
//        }
//        String title = event.getPresentation().getDescription();
//        Messages.showMessageDialog(
//                currentProject,
//                message.toString(),
//                title,
//                Messages.getInformationIcon());


        HttpClient httpClient = HttpClients.createDefault();
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
            Project project = event.getData(PlatformDataKeys.PROJECT);
            MyCustomToolWindowFactory.ProjectService projectService = ServiceManager.getService(project, MyCustomToolWindowFactory.ProjectService.class);
            MyCustomToolWindow toolWindow = projectService.getMyCustomToolWindow();
            toolWindow.displayMessage(responseString);


        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }


    }

    @Override
    public void update(AnActionEvent e) {
        // Set the availability based on whether a project is open
        Project project = e.getProject();
        e.getPresentation().setEnabledAndVisible(project != null);
    }

}