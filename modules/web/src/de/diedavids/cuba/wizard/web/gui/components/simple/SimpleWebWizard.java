package de.diedavids.cuba.wizard.web.gui.components.simple;

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.ButtonsPanel;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.GroupBoxLayout;
import com.haulmont.cuba.gui.components.TabSheet;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import de.diedavids.cuba.wizard.gui.components.Wizard;
import de.diedavids.cuba.wizard.gui.components.Wizard.WizardFinishClickEvent;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class SimpleWebWizard extends AbstractSimpleWebWizard {



    protected Map<Tab, TabSheet.Tab> steps = new LinkedHashMap<>();
    protected Map<String, TabSheet.Tab> stepsById = new LinkedHashMap<>();
    protected Map<String, Integer> tabIndexByName = new LinkedHashMap<>();


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

//            addWizardShortcutActions();
            tabSheetLayout = createTabSheetLayout();

            layoutWrapper.add(tabSheetLayout);
            layoutWrapper.expand(tabSheetLayout);
        } else {
            Collection<Component> components = tabSheetLayout.getComponents();
            for (Component component : components) {
                tabSheetLayout.remove(component);
            }
        }

        layoutWrapper.requestFocus();
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

    @Override
    public Tab addTab(String name, Component component) {
        final Tab tab = super.addTab(name, component);

        tabList.add(tab);
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
        activateStep(currentStep);
    }

    private void activateStep(Tab wizardStep) {
        if (wizardStep != null) {
            currentStep = wizardStep;
//            wizardStep.onActivate();
        }
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
        cancelAction = new BaseAction(cancelBtn.getId()) {
            @Override
            public void actionPerform(Component component) {
                handleCancelClick();
            }
        };

        cancelBtn.setAction(cancelAction);
        return cancelBtn;
    }

    private void handleCancelClick() {
        WizardCancelClickEvent event = new WizardCancelClickEvent(this);
        getEventHub().publish(WizardCancelClickEvent.class, event);
    }

    private Button createPrevBtn() {
        Button prevBtn = createWizardControlBtn("prev");

        prevAction = new BaseAction(prevBtn.getId()) {
            @Override
            public void actionPerform(Component component) {
                switchToTab(findPrevTab());
            }
        };
        prevAction.addEnabledRule(this::currentTabIsNotFirstTab);
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

    private boolean currentTabIsNotFirstTab() {
        return !currentTabIsFirstTab();
    }

    private Button createNextBtn() {
        Button nextBtn = createWizardControlBtn("next");

        nextAction = new BaseAction(nextBtn.getId())
            .withHandler(actionPerformedEvent -> nextStep());

        nextAction.addEnabledRule(() -> !currentTabIsLastTab());
        nextBtn.setAction(nextAction);
        return nextBtn;
    }

    private Button createFinishBtn() {
        Button finishBtn = createWizardControlBtn("finish");
        finishAction = new BaseAction(finishBtn.getId()) {
            @Override
            public void actionPerform(Component component) {
                handleFinishClick();
            }
        };

        finishAction.addEnabledRule(this::currentTabIsLastTab);
        finishBtn.setAction(finishAction);
        return finishBtn;
    }

    private void handleFinishClick() {
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



    @Override
    public void addWizardStepChangeListener(WizardStepChangeListener listener) {
        getEventHub().subscribe(WizardStepChangeEvent.class, listener);
    }

    @Override
    public void removeWizardStepChangeListener(WizardStepChangeListener listener) {
        getEventHub().unsubscribe(WizardStepChangeEvent.class, listener);
    }

    @Override
    public void removeWizardCancelClickListener(Consumer<WizardCancelClickEvent> listener) {
        getEventHub().unsubscribe(WizardCancelClickEvent.class, listener);
    }

    @Override
    public Subscription addWizardCancelClickListener(Consumer<WizardCancelClickEvent> listener) {
        return getEventHub().subscribe(WizardCancelClickEvent.class, listener);
    }

    @Override
    public Subscription addWizardFinishClickListener(Consumer<WizardFinishClickEvent>  listener) {
        return getEventHub().subscribe(WizardFinishClickEvent.class, listener);
    }

    @Override
    public void removeWizardFinishClickListener(Consumer<WizardFinishClickEvent> listener) {
        getEventHub().unsubscribe(WizardFinishClickEvent.class, listener);
    }

}