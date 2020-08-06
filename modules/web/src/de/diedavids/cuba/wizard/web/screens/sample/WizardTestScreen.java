package de.diedavids.cuba.wizard.web.screens.sample;

import com.haulmont.cuba.gui.components.TabSheet.SelectedTabChangeEvent;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import de.diedavids.cuba.wizard.gui.components.Wizard;
import de.diedavids.cuba.wizard.gui.components.Wizard.WizardCancelClickEvent;
import de.diedavids.cuba.wizard.gui.components.Wizard.WizardFinishClickEvent;
import de.diedavids.cuba.wizard.gui.components.Wizard.WizardStepChangeEvent;
import de.diedavids.cuba.wizard.gui.components.Wizard.WizardStepPreChangeEvent;
import java.util.EventObject;
import java.util.HashMap;
import javax.inject.Inject;

@UiController("ddcw_WizardNew")
@UiDescriptor("wizard-test-screen.xml")
public class WizardTestScreen extends Screen {

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
    protected void onWizardStepPreChangeEvent(WizardStepPreChangeEvent event) {
        events.put(WizardStepPreChangeEvent.class, event);
    }

    @Subscribe("wizard")
    protected void onWizardStepChangeEvent(WizardStepChangeEvent event) {
        events.put(WizardStepChangeEvent.class, event);
    }

    @Subscribe("wizard")
    protected void onFinishWizardClick(WizardFinishClickEvent event) {
        events.put(WizardFinishClickEvent.class, event);
    }

    public <T extends EventObject> T receivedEvent(Class<T> clazz) {
        return (T) events.get(clazz);
    }

}