package de.diedavids.cuba.wizard.web.screens.sample.cuba7;

import com.haulmont.cuba.core.global.Events;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.TabSheet.SelectedTabChangeEvent;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.StandardOutcome;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import de.diedavids.cuba.wizard.gui.components.simple.SimpleWizard;
import de.diedavids.cuba.wizard.gui.components.simple.SimpleWizard.WizardCancelClickEvent;
import de.diedavids.cuba.wizard.gui.components.simple.SimpleWizard.WizardFinishClickEvent;
import groovy.sql.GroovyRowResult;
import java.util.EventObject;
import java.util.HashMap;
import javax.inject.Inject;
import org.apache.groovy.util.concurrentlinkedhashmap.ConcurrentLinkedHashMap;

@UiController("ddcw_WizardNew")
@UiDescriptor("wizard-test-screen.xml")
public class WizardTestScreen extends Screen {

    @Inject
    protected Notifications notifications;

    @Inject
    protected SimpleWizard wizard;

    private HashMap<Class, ? super EventObject> events = new HashMap<>();

    @Subscribe("wizard")
    protected void onTabSheetSelectedTabChange(SelectedTabChangeEvent event) {
        events.put(SelectedTabChangeEvent.class, event);
    }

    @Subscribe("wizard")
    protected void onCancelWizardClick(WizardCancelClickEvent event) {
        events.put(WizardCancelClickEvent.class, event);
    }


    @Subscribe("wizard")
    protected void onFinishWizardClick(WizardFinishClickEvent event) {
        events.put(WizardFinishClickEvent.class, event);
    }

    public <T extends EventObject> T receivedEvent(Class<T> clazz) {
        return (T) events.get(clazz);
    }



}