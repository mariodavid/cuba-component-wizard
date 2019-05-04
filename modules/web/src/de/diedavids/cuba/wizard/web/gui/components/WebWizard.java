package de.diedavids.cuba.wizard.web.gui.components;

import com.haulmont.bali.events.EventRouter;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.gui.components.WebCssLayout;
import com.haulmont.cuba.web.widgets.CubaCssActionsLayout;
import de.diedavids.cuba.wizard.gui.components.Wizard;
import de.diedavids.cuba.wizard.gui.components.WizardStep;
import de.diedavids.cuba.wizard.gui.components.WizardStepAware;

import java.util.*;

public class WebWizard extends WebCssLayout implements Wizard {


    protected UiComponents uiComponents;

    protected Messages messages;

    protected GroupBoxLayout layoutWrapper;
    protected TabSheet tabSheetLayout;


    protected Map<TabSheet.Tab, WizardStep> steps = new LinkedHashMap<>();
    protected Map<String, WizardStep> stepsById = new LinkedHashMap<>();
    protected Map<String, Integer> tabIndexByName = new LinkedHashMap<>();


    protected List<TabSheet.Tab> tabList = new LinkedList<>();

    protected Wizard wizard;
    protected WizardStep currentStep;
    protected TabSheet.Tab currentTab;
    private BaseAction cancelAction;
    private BaseAction nextAction;
    private BaseAction prevAction;
    private BaseAction finishAction;
    private EventRouter eventRouter = new EventRouter();


    public WebWizard() {
        uiComponents = AppBeans.get(UiComponents.NAME);
        messages = AppBeans.get(Messages.NAME);

        component = new CubaCssActionsLayout();


        createLayout();
        com.vaadin.ui.Component unwrap = WebComponentsHelper.getComposition(layoutWrapper);
        component.addComponent(unwrap);
    }


    @Override
    public void add(Component component) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(Component component) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeAll() {
        throw new UnsupportedOperationException();
    }


    @Override
    public WizardStep getStep(String stepId) {
        return stepsById.get(stepId);
    }

    @Override
    public void addWizardStepChangeListener(WizardStepChangeListener listener) {
        eventRouter.addListener(WizardStepChangeListener.class, listener);
    }

    @Override
    public void removeWizardStepChangeListener(WizardStepChangeListener listener) {
        eventRouter.removeListener(WizardStepChangeListener.class, listener);
    }


    @Override
    public void addWizardCancelClickListener(WizardCancelClickListener listener) {
        eventRouter.addListener(WizardCancelClickListener.class, listener);
    }

    @Override
    public void removeWizardCancelClickListener(WizardCancelClickListener listener) {
        eventRouter.removeListener(WizardCancelClickListener.class, listener);
    }


    @Override
    public void addWizardFinishClickListener(WizardFinishClickListener listener) {
        eventRouter.addListener(WizardFinishClickListener.class, listener);
    }

    @Override
    public void removeWizardFinishClickListener(WizardFinishClickListener listener) {
        eventRouter.removeListener(WizardFinishClickListener.class, listener);
    }


    protected void createLayout() {
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

        layoutWrapper.requestFocus();
        ButtonsPanel buttonsPanel = createWizardButtonPanel();

        tabSheetLayout.setStyleName("centered-tabs equal-width-tabs icons-on-top");

        tabSheetLayout.addSelectedTabChangeListener(event -> {
            setCurrentStep(event);
            disableAllOtherTabs(event.getSelectedTab());
            refreshWizardButtonPanel();

        });

        layoutWrapper.add(buttonsPanel);
    }

    private void addWizardShortcutActions() {
        layoutWrapper.addShortcutAction(new ShortcutAction("CTRL-ALT-ARROW_RIGHT", shortcutTriggeredEvent -> nextAction.actionPerform(shortcutTriggeredEvent.getSource())));
        layoutWrapper.addShortcutAction(new ShortcutAction("CTRL-ALT-ARROW_LEFT", shortcutTriggeredEvent -> prevAction.actionPerform(shortcutTriggeredEvent.getSource())));
    }

    private boolean isStepChangedAllowed() {
        return currentStep == null || currentStep.preClose();
    }

    private void setCurrentStep(TabSheet.SelectedTabChangeEvent event) {
        currentTab = event.getSelectedTab();
        currentStep = steps.get(currentTab);
        activateStep(currentStep);
    }

    private void activateStep(WizardStep wizardStep) {
        if (wizardStep != null) {
            currentStep = wizardStep;
            wizardStep.onActivate();
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
        Wizard.WizardCancelClickEvent event = new Wizard.WizardCancelClickEvent(this);
        eventRouter.fireEvent(WizardCancelClickListener.class,
                Wizard.WizardCancelClickListener::cancelClicked, event);
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

            WizardStep prevStep = currentStep;
            WizardStep step = getStep(destination.getName());


            tabSheetLayout.setSelectedTab(destination);


            Wizard.WizardStepChangeEvent wizardStepChangeEvent = new Wizard.WizardStepChangeEvent(this, prevStep, step);
            eventRouter.fireEvent(WizardStepChangeListener.class,
                    Wizard.WizardStepChangeListener::stepChanged, wizardStepChangeEvent);

        }
    }

    private boolean currentTabIsNotFirstTab() {
        return !currentTabIsFirstTab();
    }

    private Button createNextBtn() {
        Button nextBtn = createWizardControlBtn("next");

        nextAction = new BaseAction(nextBtn.getId()) {
            @Override
            public void actionPerform(Component component) {
                switchToTab(findNextTab());
            }
        };

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
        Wizard.WizardFinishClickEvent finishClickEvent = new Wizard.WizardFinishClickEvent(this);
        eventRouter.fireEvent(WizardFinishClickListener.class,
                Wizard.WizardFinishClickListener::finishClicked, finishClickEvent);
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

    private TabSheet createTabSheetLayout() {
        TabSheet tabSheetLayout = uiComponents.create(TabSheet.class);
        tabSheetLayout.setWidth("100%");
        return tabSheetLayout;
    }


    @Override
    public WizardStep addStep(int index, String name, WizardStepAware wizardStepAware) {
        WebWizardStep wizardStep = new WebWizardStep(name, wizardStepAware);
        addStep(index, wizardStep);
        return wizardStep;
    }

    @Override
    public void addStep(int index, WizardStep wizardStep) {
        String name = wizardStep.getId();
        TabSheet.Tab tab = tabSheetLayout.addTab(name, wizardStep);
        steps.put(tab, wizardStep);
        stepsById.put(name, wizardStep);

        wizardStep.setWrapperComponent(tab);

        wizardStep.setMargin(true, false, true, false);

        tabList.add(index, tab);
        tabIndexByName.put(name, index);
        tab.setCaption(wizardStep.getCaption());


        if (tabListHasOnlyThisTab(tab)) {
            enableTab(tab);
            activateStep(wizardStep);
        } else {
            disableTab(tab);
        }
    }


    @Override
    public void removeStep(String name) {

        Integer index = tabIndexByName.get(name);

        if (index != null) {
            stepsById.remove(name);

            tabList.remove((int) index);

            tabSheetLayout.removeTab(name);
            tabIndexByName.remove(name);
        }

    }


    private boolean tabListHasOnlyThisTab(TabSheet.Tab tab) {
        return tabList.size() == 1 && tabList.get(0).equals(tab);
    }


}