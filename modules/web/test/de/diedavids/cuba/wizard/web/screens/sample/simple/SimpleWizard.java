package de.diedavids.cuba.wizard.web.screens.sample.simple;

import com.haulmont.cuba.gui.components.TabSheet.SelectedTabChangeEvent;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import de.diedavids.cuba.wizard.gui.components.Wizard;
import de.diedavids.cuba.wizard.gui.components.Wizard.WizardCancelClickEvent;
import de.diedavids.cuba.wizard.gui.components.Wizard.WizardFinishClickEvent;
import de.diedavids.cuba.wizard.gui.components.Wizard.WizardTabChangeEvent;
import de.diedavids.cuba.wizard.gui.components.Wizard.WizardTabPreChangeEvent;
import java.util.EventObject;
import java.util.HashMap;
import javax.inject.Inject;

@UiController("ddcw_SimpleWizard")
@UiDescriptor("simple-wizard.xml")
public class SimpleWizard extends Screen {

    @Inject
    protected Wizard wizard;

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
    protected void onWizardStepPreChangeEvent(WizardTabPreChangeEvent event) {
        events.put(WizardTabPreChangeEvent.class, event);
    }

    @Subscribe("wizard")
    protected void onWizardStepChangeEvent(WizardTabChangeEvent event) {
        events.put(WizardTabChangeEvent.class, event);
    }

    @Subscribe("wizard")
    protected void onFinishWizardClick(WizardFinishClickEvent event) {
        events.put(WizardFinishClickEvent.class, event);
    }

    public <T extends EventObject> T receivedEvent(Class<T> clazz) {
        return (T) events.get(clazz);
    }

}