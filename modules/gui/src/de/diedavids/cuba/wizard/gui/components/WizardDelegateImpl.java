package de.diedavids.cuba.wizard.gui.components;

import com.haulmont.cuba.gui.components.*;
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


    protected GroupBoxLayout layoutWrapper;
    protected TabSheet layout;


    protected Map<String, WizardStep> steps = new LinkedHashMap<>();

    @PostConstruct
    public void init() {
        createLayout();
    }

    protected void createLayout() {
        if (layout == null) {
            layoutWrapper = componentsFactory.createComponent(GroupBoxLayout.class);
            layoutWrapper.setWidth("100%");
            layoutWrapper.setCaption("Hello");
            layout = componentsFactory.createComponent(TabSheet.class);
            layout.setWidth("100%");
            layoutWrapper.add(layout);
        } else {
            Collection<Component> components = layout.getComponents();
            for (Component component : components) {
                layout.remove(component);
            }
        }
        //layout.setSpacing(true);

        createControlsLayoutForGeneric();

    }

    protected void createControlsLayoutForGeneric() {
    }

    @Override
    public void addStep(WizardStep wizardStep) {
        steps.put(wizardStep.getId(), wizardStep);
        TabSheet.Tab tab = layout.addTab(wizardStep.getId(), wizardStep);
        tab.setCaption(wizardStep.getCaption());
    }

    @Override
    public Component.Container getLayout() {
        return layoutWrapper;
    }
}