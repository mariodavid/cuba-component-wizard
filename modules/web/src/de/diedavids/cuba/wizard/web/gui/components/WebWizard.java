package de.diedavids.cuba.wizard.web.gui.components;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.filter.FilterDelegate;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.toolkit.ui.CubaAccordion;
import com.haulmont.cuba.web.toolkit.ui.CubaCssActionsLayout;
import com.vaadin.ui.TabSheet;
import de.diedavids.cuba.wizard.gui.components.Wizard;
import com.haulmont.cuba.web.gui.components.WebCssLayout;
import de.diedavids.cuba.wizard.gui.components.WizardDelegate;
import de.diedavids.cuba.wizard.gui.components.WizardStep;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class WebWizard extends WebCssLayout implements Wizard {

    WizardDelegate delegate;

    public WebWizard() {

        delegate = AppBeans.get(WizardDelegate.class);
        component = new CubaCssActionsLayout();
        Container layout = delegate.getLayout();
        com.vaadin.ui.Component unwrap = WebComponentsHelper.getComposition(layout);
        component.addComponent(unwrap);
        component.setWidth("100%");

    }


    protected Map<String, WizardStep> steps = new LinkedHashMap<>();


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
    public void addStep(WizardStep wizardStep) {
        delegate.addStep(wizardStep);
    }

    @Override
    public WizardStep getStep(String stepId) {
        return steps.get(stepId);
    }
}