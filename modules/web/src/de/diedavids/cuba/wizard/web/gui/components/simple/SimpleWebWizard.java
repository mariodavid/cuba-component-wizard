package de.diedavids.cuba.wizard.web.gui.components.simple;

import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Action.ActionPerformedEvent;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.ButtonsPanel;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.GroupBoxLayout;
import com.haulmont.cuba.gui.components.ShortcutAction;
import com.haulmont.cuba.gui.components.TabSheet;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import de.diedavids.cuba.wizard.gui.components.Wizard;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class SimpleWebWizard extends AbstractSimpleWebWizard {



    protected Map<Tab, TabSheet.Tab> steps = new LinkedHashMap<>();


    protected List<Tab> tabList = new LinkedList<>();

    protected Wizard wizard;
    protected TabSheet.Tab currentStep;
    protected TabSheet.Tab currentTab;
    private BaseAction cancelAction;
    private BaseAction nextAction;
    private BaseAction prevAction;
    private BaseAction finishAction;

    @Override
    protected GroupBoxLayout createLayout() {
        if (tabSheetLayout == null) {
            layoutWrapper = uiComponents.create(GroupBoxLayout.class);

            layoutWrapper.setWidthFull();
            layoutWrapper.setHeightFull();

            addWizardShortcutActions();
            tabSheetLayout = createTabSheetLayout();

            layoutWrapper.add(tabSheetLayout);
            layoutWrapper.expand(tabSheetLayout);
        } else {
            Collection<Component> components = tabSheetLayout.getComponents();
            for (Component component : components) {
                tabSheetLayout.remove(component);
            }
        }

        layoutWrapper.focusFirstComponent();
        ButtonsPanel buttonsPanel = createWizardButtonPanel();

        tabSheetLayout.setStyleName("centered-tabs equal-width-tabs icons-on-top");

        tabSheetLayout.addSelectedTabChangeListener(event -> {
            setCurrentStep(event);
            disableAllOtherTabs(event.getSelectedTab());
            refreshWizardButtonPanel();
        });

        layoutWrapper.add(buttonsPanel);

        return layoutWrapper;
    }

    private void addWizardShortcutActions() {
        layoutWrapper.addShortcutAction(new ShortcutAction("CTRL-ALT-ARROW_RIGHT", shortcutTriggeredEvent -> nextAction.actionPerform(shortcutTriggeredEvent.getSource())));
        layoutWrapper.addShortcutAction(new ShortcutAction("CTRL-ALT-ARROW_LEFT", shortcutTriggeredEvent -> prevAction.actionPerform(shortcutTriggeredEvent.getSource())));
    }

    @Override
    public Tab addTab(String name, Component component) {
        final Tab tab = super.addTab(name, component);

        tabList.add(tab);
        disableAllOtherTabs(currentTab);

        return tab;
    }

    private TabSheet createTabSheetLayout() {
        TabSheet tabSheetLayout = uiComponents.create(TabSheet.class);
        tabSheetLayout.setWidth("100%");
        return tabSheetLayout;
    }

    private boolean isStepChangedAllowed() {
        return currentStep == null; // || currentStep.preClose();
    }

    private void setCurrentStep(TabSheet.SelectedTabChangeEvent event) {
        currentTab = event.getSelectedTab();
        currentStep = steps.get(currentTab);
    }

    private void refreshWizardButtonPanel() {
        nextAction.refreshState();
        prevAction.refreshState();
        finishAction.refreshState();
    }

    private void disableAllOtherTabs(TabSheet.Tab exludingTab) {

        for (TabSheet.Tab tab : tabList) {
            if (tab.equals(exludingTab)) {
                enableTab(tab);
            } else {
                disableTab(tab);
            }
        }
    }

    private void enableTab(TabSheet.Tab tab) {
        tab.setEnabled(true);
    }

    private void disableTab(TabSheet.Tab tab) {
        tab.setEnabled(false);
    }

    private ButtonsPanel createWizardButtonPanel() {

        ButtonsPanel wizardButtonsPanel = uiComponents.create(ButtonsPanel.class);
        wizardButtonsPanel.setAlignment(Component.Alignment.TOP_RIGHT);

        wizardButtonsPanel.add(createCancelBtn());
        wizardButtonsPanel.add(createPrevBtn());
        wizardButtonsPanel.add(createNextBtn());
        wizardButtonsPanel.add(createFinishBtn());

        return wizardButtonsPanel;
    }

    private Button createCancelBtn() {
        Button cancelBtn = createWizardControlBtn("cancel");
        cancelBtn.setTabIndex(-1);
        cancelAction = wizardAction(cancelBtn, this::handleCancelClick);
        cancelBtn.setAction(cancelAction);
        return cancelBtn;
    }

    private void handleCancelClick(ActionPerformedEvent actionPerformedEvent) {
        getEventHub().publish(WizardCancelClickEvent.class, new WizardCancelClickEvent(this));
    }

    private BaseAction wizardAction(Button button, Consumer<Action.ActionPerformedEvent> handler) {
        return new BaseAction(button.getId())
            .withHandler(handler);
    }

    private Button createPrevBtn() {
        Button prevBtn = createWizardControlBtn("prev");

        prevAction = wizardAction(prevBtn, e -> switchToTab(findPrevTab()));
        prevAction.addEnabledRule(() -> !currentTabIsFirstTab());

        prevBtn.setAction(prevAction);
        return prevBtn;
    }

    private void switchToTab(TabSheet.Tab destination) {
        if (destination != null && isStepChangedAllowed()) {
            enableTab(destination);

            TabSheet.Tab prevStep = currentStep;
//            WizardStep step = getStep(destination.getName());


            tabSheetLayout.setSelectedTab(destination);


//            WizardStepChangeEvent wizardStepChangeEvent = new WizardStepChangeEvent(this, prevStep, step);
//            getEventHub().publish(WizardStepChangeEvent.class, wizardStepChangeEvent);

        }
    }

    private Button createNextBtn() {
        Button nextBtn = createWizardControlBtn("next");
        nextAction = wizardAction(nextBtn, actionPerformedEvent -> nextStep());
        nextAction.addEnabledRule(() -> !currentTabIsLastTab());
        nextBtn.setAction(nextAction);
        return nextBtn;
    }

    private Button createFinishBtn() {
        Button finishBtn = createWizardControlBtn("finish");
        finishAction = wizardAction(finishBtn, this::handleFinishClick);
        finishAction.addEnabledRule(this::currentTabIsLastTab);
        finishBtn.setAction(finishAction);
        return finishBtn;
    }

    private void handleFinishClick(ActionPerformedEvent actionPerformedEvent) {
        WizardFinishClickEvent finishClickEvent = new WizardFinishClickEvent(this);
        publish(WizardFinishClickEvent.class, finishClickEvent);
    }

    private boolean currentTabIsLastTab() {
        if (tabList.size() > 0) {
            return getCurrentTabIndex() == tabList.size() - 1;
        } else {
            return false;
        }

    }

    private boolean currentTabIsFirstTab() {
        return getCurrentTabIndex() == 0;
    }

    private TabSheet.Tab findNextTab() {
        int resultTabIndex = getCurrentTabIndex() + 1;
        return findTab(resultTabIndex);
    }

    private TabSheet.Tab findTab(int tabIndex) {
        if (tabIndex >= 0 && tabIndex <= tabList.size() - 1) {
            return tabList.get(tabIndex);
        } else {
            return null;
        }
    }

    private TabSheet.Tab findPrevTab() {
        return findTab(getCurrentTabIndex() - 1);
    }

    private int getCurrentTabIndex() {
        return getTabIndex(currentTab);
    }

    private int getTabIndex(TabSheet.Tab tabToFind) {
        int possibleCurrentTabIndex = 0;
        int result = 0;

        for (TabSheet.Tab tab : tabList) {
            if (tab == tabToFind) {
                result = possibleCurrentTabIndex;
            }
            possibleCurrentTabIndex++;
        }
        return result;
    }


    private Button createWizardControlBtn(String id) {
        Button btn = uiComponents.create(Button.class);
        btn.setId(id);
        btn.setCaption(messages.getMessage(this.getClass(), id + "BtnCaption"));
        btn.setIcon(messages.getMessage(this.getClass(), id + "BtnIcon"));
        return btn;
    }

    @Override
    public void nextStep() {
        switchToTab(findNextTab());
    }
    @Override
    public void previousStep() {
        switchToTab(findPrevTab());
    }


}