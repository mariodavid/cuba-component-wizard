package de.diedavids.cuba.wizard.web.gui.components;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.TabSheet;
import com.haulmont.cuba.web.gui.components.WebVBoxLayout;
import de.diedavids.cuba.wizard.gui.components.WizardStep;
import de.diedavids.cuba.wizard.gui.components.WizardStepAware;

public class WebWizardStep extends WebVBoxLayout implements WizardStep {
    private String name;
    private WizardStepAware stepComponent;
    private TabSheet.Tab tabComponent;

    public WebWizardStep() {

    }

    @Override
    public void setIcon(String icon) {
        if (tabComponent != null) {
            tabComponent.setIcon(icon);
        }

    }

    @Override
    public void setCaption(String caption) {
        if (tabComponent != null) {
            tabComponent.setCaption(caption);
        }
    }

    public WebWizardStep(String name, WizardStepAware stepComponent) {
        this.name = name;
        this.stepComponent = stepComponent;
        this.add((Component) stepComponent);
    }


    @Override
    public void onActivate() {
        WizardStepAware wizardStepAware = getWizardStepAware();
        if (wizardStepAware != null) {
            wizardStepAware.onActivate();
        }
    }


    @Override
    public boolean preClose() {
        WizardStepAware wizardStepAware = getWizardStepAware();
        if (wizardStepAware != null) {
            return wizardStepAware.preClose();
        }
        else {
            return false;
        }
    }

    @Override
    public void setWrapperComponent(TabSheet.Tab tabComponent) {
        this.tabComponent = tabComponent;
    }


    private WizardStepAware getWizardStepAware() {
        if (ownComponents.size() > 0) {
            return (WizardStepAware) ownComponents.get(0);
        }
        else {
            return stepComponent;
        }
    }
}