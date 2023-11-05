package org.abc.action;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;

public class MyCustomToolWindowFactory implements ToolWindowFactory, DumbAware {


    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {

        MyCustomToolWindow myCustomToolWindow = new MyCustomToolWindow();

        ProjectService projectService = ServiceManager.getService(project, ProjectService.class);
        projectService.setMyCustomToolWindow(myCustomToolWindow);

        SimpleToolWindowPanel toolWindowPanel = myCustomToolWindow.createToolWindowContent(project);
        ContentManager contentManager = toolWindow.getContentManager();
        Content content = ContentFactory.getInstance().createContent(toolWindowPanel, "", false);
        contentManager.addContent(content);
    }



    public static class ProjectService {
        private MyCustomToolWindow myCustomToolWindow;

        public MyCustomToolWindow getMyCustomToolWindow() {
            return myCustomToolWindow;
        }

        void setMyCustomToolWindow(MyCustomToolWindow myCustomTool) {
            this.myCustomToolWindow  = myCustomTool;
        }



    }


}
