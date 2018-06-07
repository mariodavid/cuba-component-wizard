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
           nextAction.refreshState();
           prevAction.refreshState();
           finishAction.refreshState();
        });


        layoutWrapper.add(buttonsPanel);

    }

    private void refreshEnabledTabs() {
        findNextTabs().forEach(tab -> tab.setEnabled(false));
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
        ButtonsPanel buttonsPanel = componentsFactory.createComponent(ButtonsPanel.class);
        buttonsPanel.setAlignment(Component.Alignment.TOP_RIGHT);

        Button cancelBtn = createWizardControlBtn("cancel");
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
        prevAction.addEnabledRule(() -> !currentTabIsFirstTab());
        prevBtn.setAction(prevAction);


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

        Button finishBtn = createWizardControlBtn("finish");
        finishAction = new BaseAction(finishBtn.getId()) {
            @Override
            public void actionPerform(Component component) {
                super.actionPerform(component);
            }
        };

        finishAction.addEnabledRule(() -> currentTabIsLastTab());
        finishBtn.setAction(finishAction);
        buttonsPanel.add(cancelBtn);
        buttonsPanel.add(prevBtn);
        buttonsPanel.add(nextBtn);
        buttonsPanel.add(finishBtn);

        return buttonsPanel;
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
        int i = 0;
        int currentTabIndex = 0;

        for (TabSheet.Tab tab : tabList) {
            if (tab == currentTab) {
                currentTabIndex = i;
            }

            i++;

        }
        return currentTabIndex;
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
            tab.setEnabled(false);
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