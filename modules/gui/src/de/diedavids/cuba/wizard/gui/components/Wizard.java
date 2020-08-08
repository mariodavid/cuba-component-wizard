package de.diedavids.cuba.wizard.gui.components;

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.gui.components.TabSheet;
import com.haulmont.cuba.gui.components.TabSheet.Tab;
import java.util.EventObject;
import java.util.function.Consumer;

/**
 * Wizard component interface.
 */
public interface Wizard extends TabSheet {

    String NAME = "wizard";

    void nextTab();

    void previousTab();

    enum Direction {
        NEXT,
        PREVIOUS
    }


    /**
     * Add a listener that will be notified when a tab change happened
     */
    Subscription addWizardTabChangeListener(Consumer<WizardTabChangeEvent> listener);
    void removeWizardTabChangeListener(Consumer<WizardTabChangeEvent> listener);

    /**
     * Add a listener that will be notified when a tab is going to be changed
     */
    Subscription addWizardTabPreChangeListener(Consumer<WizardTabPreChangeEvent> listener);
    void removeWizardTabPreChangeListener(Consumer<WizardTabPreChangeEvent> listener);

    class WizardTabChangeEvent extends EventObject {

        TabSheet.Tab oldTab;
        TabSheet.Tab newTab;
        Direction direction;

        public WizardTabChangeEvent(Wizard source, TabSheet.Tab oldTab, TabSheet.Tab newTab, Direction direction) {
            super(source);
            this.oldTab = oldTab;
            this.newTab = newTab;
            this.direction = direction;
        }

        public Direction getDirection() {
            return direction;
        }

        @Override
        public Wizard getSource() {
            return (Wizard) super.getSource();
        }

        public TabSheet.Tab getOldTab() {
            return oldTab;
        }

        public TabSheet.Tab getNewTab() {
            return newTab;
        }
    }

    class WizardTabPreChangeEvent extends EventObject {

        TabSheet.Tab oldTab;
        TabSheet.Tab newTab;

        Direction direction;

        private boolean tabChangePrevented = false;

        public WizardTabPreChangeEvent(Wizard source, TabSheet.Tab oldTab, TabSheet.Tab newTab, Direction direction) {
            super(source);
            this.oldTab = oldTab;
            this.newTab = newTab;
            this.direction = direction;
        }

        public Direction getDirection() {
            return direction;
        }
        @Override
        public Wizard getSource() {
            return (Wizard) super.getSource();
        }

        public TabSheet.Tab getOldTab() {
            return oldTab;
        }

        public TabSheet.Tab getNewTab() {
            return newTab;
        }


        /**
         * Invoke this method if you want to abort the tab change.
         */
        public void preventTabChange() {
            tabChangePrevented = true;
        }

        /**
         * Returns true if {@link #preventTabChange()} method was called and tab change will be aborted.
         */
        public boolean isTabChangePrevented() {
            return tabChangePrevented;
        }
    }


    /**
     * Add a listener that will be notified when a wizards cancel operation is performed
     */
    Subscription addWizardCancelClickListener(Consumer<WizardCancelClickEvent> listener);
    void removeWizardCancelClickListener(Consumer<WizardCancelClickEvent> listener);

    class WizardCancelClickEvent extends EventObject {
        public WizardCancelClickEvent(Wizard source) {
            super(source);
        }

        @Override
        public Wizard getSource() {
            return (Wizard) super.getSource();
        }
    }


    /**
     * Add a listener that will be notified when a wizards finish operation is performed
     */
    Subscription addWizardFinishClickListener(Consumer<WizardFinishClickEvent> listener);
    void removeWizardFinishClickListener(Consumer<WizardFinishClickEvent> listener);

    class WizardFinishClickEvent extends EventObject {
        public WizardFinishClickEvent(Wizard source) {
            super(source);
        }

        @Override
        public Wizard getSource() {
            return (Wizard) super.getSource();
        }
    }
}