package de.diedavids.cuba.wizard.gui.components;

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.*;
import java.util.function.Consumer;

@org.springframework.stereotype.Component(WizardDelegate.NAME)
@Scope("prototype")
public class WizardDelegateImpl implements WizardDelegate {


    protected static final Logger log = LoggerFactory.getLogger(WizardDelegateImpl.class);

    @Inject
    protected ComponentsFactory componentsFactory;

    @Inject
    protected Messages messages;

    protected GroupBoxLayout layoutWrapper;
    protected TabSheet tabSheetLayout;


    protected Map<TabSheet.Tab, WizardStep> steps = new LinkedHashMap<>();


    protected List<TabSheet.Tab> tabList = new LinkedList<>();

    protected Wizard wizard;
    protected WizardStep currentStep;
    protected TabSheet.Tab currentTab;
    private BaseAction nextAction;
    private BaseAction prevAction;
    private BaseAction finishAction;

    @PostConstruct
    public void init() {
        createLayout();
    }


    @Override
    public void setWizard(Wizard wizard) {
        this.wizard = wizard;
    }

    protected void createLayout() {
        if (tabSheetLayout == null) {
            layoutWrapper = componentsFactory.createComponent(GroupBoxLayout.class);
            layoutWrapper.setWidth("100%");

            addWizardShortcutActions();
            tabSheetLayout = createTabSheetLayout();
            layoutWrapper.add(tabSheetLayout);
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
        } else {
            wizard.getFrame().showNotification(messages.getMessage(this.getClass(), "switchStepNotAllowed"), Frame.NotificationType.WARNING);
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
                super.actionPerform(component);
            }
        };

        finishAction.addEnabledRule(this::currentTabIsLastTab);
        finishBtn.setAction(finishAction);
        return finishBtn;
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

    @Override
    public Component.Container getTabSheetLayout() {
        return layoutWrapper;
    }

}