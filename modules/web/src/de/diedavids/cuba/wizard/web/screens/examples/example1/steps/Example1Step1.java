package de.diedavids.cuba.wizard.web.screens.examples.example1.steps;

import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import de.diedavids.cuba.wizard.gui.components.AbstractWizardStep;

import javax.inject.Inject;

@UiController("ExampleStep1")
@UiDescriptor("example-1-step-1.xml")
public class Example1Step1 extends AbstractWizardStep {

    @Inject
    private Notifications notifications;

    boolean preCloseTrue = true;

    @Override
    public boolean preClose() {
        preCloseTrue = !preCloseTrue;
        return preCloseTrue;
    }

    public void buttonClick() {
        notifications.create().withCaption("hello").show();
    }



    @Subscribe
    protected void onAfterShow(InitEvent event) {
        notifications.create().withCaption("Just opened").show();
    }


    /*
    @Override
    public void onActivate() {
        notifications
                .create()
                .withCaption("on activate step 1")
                .show();
    }
    */

}