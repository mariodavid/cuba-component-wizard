package de.diedavids.cuba.wizard.web.screens.sample.cuba7;

import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.Notifications.NotificationType;
import com.haulmont.cuba.gui.components.TabSheet.SelectedTabChangeEvent;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import javax.inject.Inject;

@UiController("ddcw_WizardNew")
@UiDescriptor("wizard-new.xml")
public class WizardNew extends Screen {

    @Inject
    protected Notifications notifications;

    @Subscribe("wizard")
    protected void onTabSheetSelectedTabChange(SelectedTabChangeEvent event) {


        notifications.create(NotificationType.TRAY)
            .withCaption("hello")
            .show();
    }


}