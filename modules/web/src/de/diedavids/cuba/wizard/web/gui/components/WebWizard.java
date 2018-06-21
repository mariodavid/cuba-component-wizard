package de.diedavids.cuba.wizard.web.gui.components;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.toolkit.ui.CubaCssActionsLayout;
import de.diedavids.cuba.wizard.gui.components.Wizard;
import com.haulmont.cuba.web.gui.components.WebCssLayout;
import de.diedavids.cuba.wizard.gui.components.WizardStep;

import java.util.*;

public class WebWizard extends WebCssLayout implements Wizard {


    public WebWizard() {
        componentsFactory = AppBeans.get(ComponentsFactory.NAME);
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
        return steps.get(stepId);
    }

    @Override
    public void addWizardStepChangeListener(WizardStepChangeListener listener) {
        getEventRouter().addListener(WizardStepChangeListener.class, listener);
    }

    @Override
    public void removeWizardStepChangeListener(WizardStepChangeListener listener) {
        getEventRouter().removeListener(WizardStepChangeListener.class, listener);
    }


    @Override
    public void addWizardFinishClickListener(WizardFinishClickListener listener) {
        getEventRouter().addListener(WizardFinishClickListener.class, listener);
    }

    @Override
    public void removeWizardFinishClickListener(WizardFinishClickListener listener) {
        getEventRouter().removeListener(WizardFinishClickListener.class, listener);
    }


    protected ComponentsFactory componentsFactory;

    protected Messages messages;

    protected GroupBoxLayout layoutWrapper;
    protected TabSheet tabSheetLayout;


    protected Map<TabSheet.Tab, WizardStep> steps = new LinkedHashMap<>();


    protected List<TabSheet.Tab> tabList = new LinkedList<>();

    protected Wizard wizard;
    protected WizardStep currentStep;
    protected TabSheet.Tab currentTab;
    private BaseAction cancelAction;
    private BaseAction nextAction;
    private BaseAction prevAction;
    private BaseAction finishAction;


    protected void createLayout() {
        if (tabSheetLayout == null) {
            layoutWrapper = componentsFactory.createComponent(GroupBoxLayout.class);

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
        layoutWrapper.addShortcutAction(new Component.ShortcutAction("CTRL-ALT-ARROW_RIGHT", shortcutTriggeredEvent -> nextAction.actionPerform(shortcutTriggeredEvent.getSource())));
        layoutWrapper.addShortcutAction(new Component.ShortcutAction("CTRL-ALT-ARROW_LEFT", shortcutTriggeredEvent -> prevAction.actionPerform(shortcutTriggeredEvent.getSource())));
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

        ButtonsPanel wizardButtonsPanel = componentsFactory.createComponent(ButtonsPanel.class);
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

            }
        };

        cancelBtn.setAction(nextAction);
        return cancelBtn;
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
            tabSheetLayout.setSelectedTab(destination);
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
        getEventRouter().fireEvent(WizardFinishClickListener.class,
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
        Button btn = componentsFactory.createComponent(Button.class);
        btn.setId(id);
        btn.setCaption(messages.getMessage(this.getClass(), id + "BtnCaption"));
        btn.setIcon(messages.getMessage(this.getClass(), id + "BtnIcon"));
        return btn;
    }

    private TabSheet createTabSheetLayout() {
        TabSheet tabSheetLayout = componentsFactory.createComponent(TabSheet.class);
        tabSheetLayout.setWidth("100%");
        return tabSheetLayout;
    }

    @Override
    public void addStep(int index, WizardStep wizardStep) {

        TabSheet.Tab tab = tabSheetLayout.addTab(wizardStep.getId(), wizardStep);
        steps.put(tab, wizardStep);

        wizardStep.setMargin(true, false, true, false);
        tabList.add(index, tab);
        tab.setCaption(wizardStep.getCaption());


        if (tabListHasOnlyThisTab(tab)) {
            enableTab(tab);
            activateStep(wizardStep);
        } else {
            disableTab(tab);
        }
    }

    private boolean tabListHasOnlyThisTab(TabSheet.Tab tab) {
        return tabList.size() == 1 && tabList.get(0).equals(tab);
    }


}