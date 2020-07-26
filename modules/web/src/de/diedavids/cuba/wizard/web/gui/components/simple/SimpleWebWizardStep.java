package de.diedavids.cuba.wizard.web.gui.components.simple;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.TabSheet;
import com.haulmont.cuba.web.gui.components.WebFragment;
import com.haulmont.cuba.web.gui.components.WebVBoxLayout;
import de.diedavids.cuba.wizard.gui.components.AbstractWizardStep;
import de.diedavids.cuba.wizard.gui.components.WizardStep;
import de.diedavids.cuba.wizard.gui.components.simple.SimpleWizardStep;

public class SimpleWebWizardStep extends WebVBoxLayout implements SimpleWizardStep {
//    private String name;
//    private AbstractWizardStep stepComponent;
//    private TabSheet.Tab tabComponent;
//
//    public SimpleWebWizardStep() {
//
//    }
//
//    @Override
//    public void setIcon(String icon) {
//        if (tabComponent != null) {
//            tabComponent.setIcon(icon);
//        }
//
//    }
//
//    @Override
//    public void setCaption(String caption) {
//        if (tabComponent != null) {
//            tabComponent.setCaption(caption);
//        }
//    }
//
//    public SimpleWebWizardStep(String name, AbstractWizardStep stepComponent) {
//        this.id = name;
//        this.name = name;
//        this.stepComponent = stepComponent;
//        this.add((Component) stepComponent);
//    }
//
//
//    @Override
//    public void onActivate() {
//        AbstractWizardStep wizardStep = getWizardStep();
//        if (wizardStep != null) {
//            wizardStep.onActivate();
//        }
//    }
//
//
//    @Override
//    public boolean preClose() {
//        AbstractWizardStep wizardStep = getWizardStep();
//        if (wizardStep != null) {
//            return wizardStep.preClose();
//        } else {
//            return false;
//        }
//    }
//
//    @Override
//    public void setWrapperComponent(TabSheet.Tab tabComponent) {
//        this.tabComponent = tabComponent;
//    }
//
//
//    private AbstractWizardStep getWizardStep() {
//        if (isInitialized()) {
//            if (wasProgrammagicallyAdded()) {
//                return getWizardStepInstanceForProgrammaticAddedFrames();
//            } else {
//                return getWizardStepInstanceLoadedViaXml();
//            }
//
//        } else {
//            return stepComponent;
//        }
//    }
//
//    private boolean isInitialized() {
//        return ownComponents.size() > 0;
//    }
//
//    private boolean wasProgrammagicallyAdded() {
//        return ownComponents.get(0) instanceof AbstractWizardStep;
//    }
//
//    private AbstractWizardStep getWizardStepInstanceLoadedViaXml() {
//        return (AbstractWizardStep) ((WebFragment) ownComponents.get(0)).getFrameOwner();
//    }
//
//    private AbstractWizardStep getWizardStepInstanceForProgrammaticAddedFrames() {
//        return (AbstractWizardStep) ownComponents.get(0);
//    }
}