package de.diedavids.cuba.wizard.web.gui.components;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.TabSheet;
import com.haulmont.cuba.web.gui.components.WebFragment;
import com.haulmont.cuba.web.gui.components.WebVBoxLayout;
import de.diedavids.cuba.wizard.gui.components.AbstractWizardStep;
import de.diedavids.cuba.wizard.gui.components.WizardStep;

public class WebWizardStep extends WebVBoxLayout implements WizardStep {
    private String name;
    private AbstractWizardStep stepComponent;
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

    public WebWizardStep(String name, AbstractWizardStep stepComponent) {
        this.id = name;
        this.name = name;
        this.stepComponent = stepComponent;
        this.add((Component) stepComponent);
    }


    @Override
    public void onActivate() {
        AbstractWizardStep wizardStep = getWizardStep();
        if (wizardStep != null) {
            wizardStep.onActivate();
        }
    }


    @Override
    public boolean preClose() {
        AbstractWizardStep wizardStep = getWizardStep();
        if (wizardStep != null) {
            return wizardStep.preClose();
        } else {
            return false;
        }
    }

    @Override
    public boolean preClosePreviousClicked() {
        AbstractWizardStep wizardStep = getWizardStep();
        if (wizardStep != null) {
            return wizardStep.preClosePreviousClicked();
        } else {
            return false;
        }
    }

    @Override
    public boolean preCloseNextClicked() {
        AbstractWizardStep wizardStep = getWizardStep();
        if (wizardStep != null) {
            return wizardStep.preCloseNextClicked();
        } else {
            return false;
        }
    }

    @Override
    public void setWrapperComponent(TabSheet.Tab tabComponent) {
        this.tabComponent = tabComponent;
    }


    private AbstractWizardStep getWizardStep() {
        if (isInitialized()) {
            if (wasProgrammagicallyAdded()) {
                return getWizardStepInstanceForProgrammaticAddedFrames();
            } else {
                return getWizardStepInstanceLoadedViaXml();
            }

        } else {
            return stepComponent;
        }
    }

    private boolean isInitialized() {
        return ownComponents.size() > 0;
    }

    private boolean wasProgrammagicallyAdded() {
        return ownComponents.get(0) instanceof AbstractWizardStep;
    }

    private AbstractWizardStep getWizardStepInstanceLoadedViaXml() {
        return (AbstractWizardStep) ((WebFragment) ownComponents.get(0)).getFrameOwner();
    }

    private AbstractWizardStep getWizardStepInstanceForProgrammaticAddedFrames() {
        return (AbstractWizardStep) ownComponents.get(0);
    }
}