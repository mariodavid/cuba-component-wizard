package de.diedavids.cuba.wizard.web.gui.components;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.toolkit.ui.CubaCssActionsLayout;
import de.diedavids.cuba.wizard.gui.components.Wizard;
import com.haulmont.cuba.web.gui.components.WebCssLayout;
import de.diedavids.cuba.wizard.gui.components.WizardDelegate;
import de.diedavids.cuba.wizard.gui.components.WizardStep;

import java.util.LinkedHashMap;
import java.util.Map;

public class WebWizard extends WebCssLayout implements Wizard {

    WizardDelegate delegate;

    public WebWizard() {

        delegate = AppBeans.get(WizardDelegate.class);
        component = new CubaCssActionsLayout();
        Container layout = delegate.getTabSheetLayout();
        com.vaadin.ui.Component unwrap = WebComponentsHelper.getComposition(layout);
        component.addComponent(unwrap);
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
    public void addStep(int index, WizardStep wizardStep) {
        delegate.addStep(index, wizardStep);
    }

    @Override
    public WizardStep getStep(String stepId) {
        return steps.get(stepId);
    }
}