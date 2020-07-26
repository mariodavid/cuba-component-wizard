package de.diedavids.cuba.wizard.web.screens;

import com.haulmont.cuba.gui.components.AbstractWindow;
import de.diedavids.cuba.wizard.gui.components.AbstractWizardStep;

public class AbstractWizard extends AbstractWindow {

    public AbstractWizardStep createStep(String screenId) {
        return (AbstractWizardStep) openFrame(null, screenId);
    }
}
