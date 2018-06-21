package de.diedavids.cuba.wizard.gui.components;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Frame;

import java.util.EventObject;

public interface Wizard extends Component.OrderedContainer,
                Component.HasIcon, Component.HasCaption {
    String NAME = "wizard";

    enum WizardMode {
        HORIZONTAL,
        VERTICAL
    }

    void addStep(int index, WizardStep wizardStep);

    WizardStep getStep(String stepId);

    Frame getFrame();

    interface WizardStepChangeNotifier {
        void addWizardStepChangeListener(WizardStepChangeListener listener);
        void removeWizardStepChangeListener(WizardStepChangeListener listener);
    }

    class WizardStepChangeEvent extends EventObject {
        public WizardStepChangeEvent(Wizard source) {
            super(source);
        }

        @Override
        public Wizard getSource() {
            return (Wizard) super.getSource();
        }
    }

    @FunctionalInterface
    interface WizardStepChangeListener {
        void lookupValueChanged(Wizard.WizardStepChangeEvent event);
    }
}