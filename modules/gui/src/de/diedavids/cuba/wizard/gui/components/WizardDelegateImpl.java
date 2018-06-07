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
    protected Accordion accordionLayout;


    protected Map<String, WizardStep> steps = new LinkedHashMap<>();


    protected List<TabSheet.Tab> tabList = new LinkedList<>();

    protected WizardStep currentStep;
    protected TabSheet.Tab currentTab;
    private BaseAction nextAction;
    private BaseAction prevAction;
    private BaseAction finishAction;

    @PostConstruct
    public void init() {
        createLayout();
    }

    protected void createLayout() {
        if (tabSheetLayout == null) {
            layoutWrapper = componentsFactory.createComponent(GroupBoxLayout.class);
            layoutWrapper.setWidth("100%");
            createTabSheetLayout();
            layoutWrapper.add(tabSheetLayout);
        } else {
            Collection<Component> components = tabSheetLayout.getComponents();
            for (Component component : components) {
                tabSheetLayout.remove(component);
            }
        }

        ButtonsPanel buttonsPanel = createWizardButtonPanel();

        tabSheetLayout.setStyleName("centered-tabs equal-width-tabs icons-on-top");

        tabSheetLayout.addSelectedTabChangeListener(event -> {
            currentTab = event.getSelectedTab();
            refreshEnabledTabs();
            refreshWizardButtonPanel();
        });


        layoutWrapper.add(buttonsPanel);

    }

    private void refreshWizardButtonPanel() {
        nextAction.refreshState();
        prevAction.refreshState();
        finishAction.refreshState();
    }

    private void refreshEnabledTabs() {
        findNextTabs().forEach(this::disableTab);
    }

    private void disableTab(TabSheet.Tab tab) {
        tab.setEnabled(false);
    }

    private List<TabSheet.Tab> findNextTabs() {

        int tabCount = tabList.size();

        if (tabCount > 0) {
            return tabList.subList(getCurrentTabIndex() + 1, tabCount);
        }
        else {
            return Collections.emptyList();
        }
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
        return createWizardControlBtn("cancel");
    }

    private Button createPrevBtn() {
        Button prevBtn = createWizardControlBtn("prev");

        prevAction = new BaseAction(prevBtn.getId()) {
            @Override
            public void actionPerform(Component component) {
                TabSheet.Tab prevTab = findPrevTab();
                if (prevTab != null) {
                    tabSheetLayout.setSelectedTab(prevTab);
                }
            }
        };
        prevAction.addEnabledRule(this::currentTabIsNotFirstTab);
        prevBtn.setAction(prevAction);
        return prevBtn;
    }

    private boolean currentTabIsNotFirstTab() {
        return !currentTabIsFirstTab();
    }

    private Button createNextBtn() {
        Button nextBtn = createWizardControlBtn("next");

        nextAction = new BaseAction(nextBtn.getId()) {
            @Override
            public void actionPerform(Component component) {
                TabSheet.Tab nextTab = findNextTab();
                if (nextTab != null) {
                    nextTab.setEnabled(true);
                    tabSheetLayout.setSelectedTab(nextTab);
                }
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
        }
        else {
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
        }
        else {
            return null;
        }
    }

    private TabSheet.Tab findPrevTab() {
        return findTab(getCurrentTabIndex() - 1);
    }

    private int getCurrentTabIndex() {
        int possibleCurrentTabIndex = 0;
        int result = 0;

        for (TabSheet.Tab tab : tabList) {
            if (tab == currentTab) {
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

    private void createTabSheetLayout() {
        tabSheetLayout = componentsFactory.createComponent(TabSheet.class);
        tabSheetLayout.setWidth("100%");
        layoutWrapper.removeAll();
        layoutWrapper.add(tabSheetLayout);
    }

    @Override
    public void addStep(int index, WizardStep wizardStep) {
        steps.put(wizardStep.getId(), wizardStep);

        TabSheet.Tab tab = tabSheetLayout.addTab(wizardStep.getId(), wizardStep);

        if (tabList.size() == 0) {
            tab.setEnabled(true);
        }
        else {
            disableTab(tab);
        }

        wizardStep.setMargin(true, false, true, false);
        tabList.add(index, tab);
        tab.setCaption(wizardStep.getCaption());
    }

    @Override
    public Component.Container getTabSheetLayout() {
        return layoutWrapper;
    }

}