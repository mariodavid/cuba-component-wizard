package de.diedavids.cuba.wizard.web.gui.components.simple;

import com.haulmont.cuba.gui.components.Action.ActionPerformedEvent;
import com.haulmont.cuba.gui.components.ButtonsPanel;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.GroupBoxLayout;
import com.haulmont.cuba.gui.components.ShortcutAction;
import com.haulmont.cuba.gui.components.TabSheet;
import de.diedavids.cuba.wizard.gui.components.Wizard;
import de.diedavids.cuba.wizard.web.gui.components.simple.WizardButtonsPanel.WizardButtonType;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SimpleWebWizard extends AbstractSimpleWebWizard {


    protected Map<Tab, TabSheet.Tab> steps = new LinkedHashMap<>();

    protected List<Tab> tabList = new LinkedList<>();


    protected Wizard wizard;
    protected TabSheet.Tab currentStep;
    protected TabSheet.Tab currentTab;
//    private BaseAction cancelAction;
//    private BaseAction nextAction;
//    private BaseAction prevAction;
//    private BaseAction finishAction;
    private WizardButtonsPanel buttonsPanel;

    @Override
    protected GroupBoxLayout createLayout() {

        buttonsPanel = new WizardButtonsPanel(
            uiComponents,
            messages
        );

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
        layoutWrapper.addShortcutAction(new ShortcutAction("CTRL-ALT-ARROW_RIGHT",
            shortcutTriggeredEvent -> nextStep()));
        layoutWrapper.addShortcutAction(new ShortcutAction("CTRL-ALT-ARROW_LEFT",
            shortcutTriggeredEvent -> previousStep()));
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

    private void setCurrentStep(TabSheet.SelectedTabChangeEvent event) {
        currentTab = event.getSelectedTab();
        currentStep = steps.get(currentTab);
    }

    private void refreshWizardButtonPanel() {
        buttonsPanel.refresh();
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

       return buttonsPanel.createWizardButtonPanel(
            Arrays.asList(
                WizardButtonsPanel.button(
                    WizardButtonType.CANCEL,
                    this::handleCancelClick,
                    () -> true
                ),
                WizardButtonsPanel.button(
                    WizardButtonType.PREVIOUS,
                    e -> previousStep(),
                    () -> !currentTabIsFirstTab()
                ),
                WizardButtonsPanel.button(
                    WizardButtonType.NEXT,
                    e -> nextStep(),
                    () -> !currentTabIsLastTab()
                ),
                WizardButtonsPanel.button(
                    WizardButtonType.FINISH,
                    this::handleFinishClick,
                    this::currentTabIsLastTab
                )
            )
        );
    }

    private void handleCancelClick(ActionPerformedEvent actionPerformedEvent) {
        getEventHub().publish(WizardCancelClickEvent.class, new WizardCancelClickEvent(this));
    }

    private void switchToTab(
        Tab destination,
        Direction direction
    ) {

        WizardStepPreChangeEvent stepPreChangeEvent = new WizardStepPreChangeEvent(this, currentTab,
            destination, direction);
        getEventHub().publish(WizardStepPreChangeEvent.class, stepPreChangeEvent);

        if (!stepPreChangeEvent.isStepChangePrevented()) {

            enableTab(destination);

            tabSheetLayout.setSelectedTab(destination);
            WizardStepChangeEvent wizardStepChangeEvent = new WizardStepChangeEvent(this,
                currentTab, destination, direction);
            getEventHub().publish(WizardStepChangeEvent.class, wizardStepChangeEvent);

        }
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

    @Override
    public void nextStep() {
        switchToTab(findNextTab(), Direction.NEXT);
    }

    @Override
    public void previousStep() {
        switchToTab(findPrevTab(), Direction.PREVIOUS);
    }
}