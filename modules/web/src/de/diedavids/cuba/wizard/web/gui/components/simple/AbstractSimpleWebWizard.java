package de.diedavids.cuba.wizard.web.gui.components.simple;

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.app.security.role.edit.UiPermissionDescriptor;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.GroupBoxLayout;
import com.haulmont.cuba.gui.components.SupportsChildrenSelection;
import com.haulmont.cuba.gui.components.TabSheet;
import com.haulmont.cuba.gui.components.UiPermissionAware;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.gui.components.WebCssLayout;
import com.haulmont.cuba.web.gui.components.WebTabSheet;
import com.haulmont.cuba.web.widgets.CubaCssActionsLayout;
import de.diedavids.cuba.wizard.gui.components.simple.SimpleWizard;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.dom4j.Element;

public abstract class AbstractSimpleWebWizard extends WebCssLayout
    implements SimpleWizard, UiPermissionAware, SupportsChildrenSelection {


    protected final UiComponents uiComponents;
    protected final Messages messages;
    protected GroupBoxLayout layoutWrapper;

    protected TabSheet tabSheetLayout;


    public AbstractSimpleWebWizard() {
        uiComponents = AppBeans.get(UiComponents.NAME);
        messages = AppBeans.get(Messages.NAME);

        component = new CubaCssActionsLayout();

        layoutWrapper = createLayout();
        com.vaadin.ui.Component unwrap = WebComponentsHelper.getComposition(layoutWrapper);
        component.addComponent(unwrap);
    }


    @Override
    public Tab addTab(String name, Component component) {
        return tabSheetLayout.addTab(name, component);
    }

    @Override
    public Tab addLazyTab(String name, Element descriptor,
        ComponentLoader loader) {
        return tabSheetLayout.addLazyTab(name, descriptor, loader);
    }

    @Override
    public void removeTab(String name) {
        tabSheetLayout.removeTab(name);
    }

    @Override
    public void removeAllTabs() {
        tabSheetLayout.removeAllTabs();
    }

    @Override
    public Tab getSelectedTab() {
        return tabSheetLayout.getSelectedTab();
    }

    @Override
    public void setSelectedTab(Tab tab) {
        tabSheetLayout.setSelectedTab(tab);
    }

    @Override
    public void setSelectedTab(String name) {
        tabSheetLayout.setSelectedTab(name);
    }

    @Override
    public Tab getTab(String name) {
        return tabSheetLayout.getTab(name);
    }

    @Override
    public Component getTabComponent(String name) {
        return tabSheetLayout.getTabComponent(name);
    }

    @Override
    public Collection<Tab> getTabs() {
        return tabSheetLayout.getTabs();
    }

    @Override
    public boolean isTabCaptionsAsHtml() {
        return tabSheetLayout.isTabCaptionsAsHtml();
    }

    @Override
    public void setTabCaptionsAsHtml(boolean tabCaptionsAsHtml) {
        tabSheetLayout.setTabCaptionsAsHtml(tabCaptionsAsHtml);
    }

    @Override
    public boolean isTabsVisible() {
        return tabSheetLayout.isTabsVisible();
    }

    @Override
    public void setTabsVisible(boolean tabsVisible) {
        tabSheetLayout.setTabsVisible(tabsVisible);
    }

    @Override
    public Subscription addSelectedTabChangeListener(
        Consumer<SelectedTabChangeEvent> listener) {
        return tabSheetLayout.addSelectedTabChangeListener(listener);
    }

    @Override
    @Deprecated
    public void removeSelectedTabChangeListener(
        Consumer<SelectedTabChangeEvent> listener) {
        tabSheetLayout.removeSelectedTabChangeListener(listener);
    }

    abstract protected GroupBoxLayout createLayout();


    @Override
    public void focus() {
        tabSheetLayout.focus();
    }

    @Override
    public int getTabIndex() {
        return tabSheetLayout.getTabIndex();
    }

    @Override
    public void setTabIndex(int tabIndex) {
        tabSheetLayout.setTabIndex(tabIndex);
    }

    @Override
    public void setChildSelected(Component childComponent) {
        ((WebTabSheet) tabSheetLayout).setChildSelected(childComponent);
    }

    @Override
    public boolean isChildSelected(Component component) {
        return ((WebTabSheet) tabSheetLayout).isChildSelected(component);
    }

    @Override
    public void applyPermission(UiPermissionDescriptor permissionDescriptor) {
        ((WebTabSheet) tabSheetLayout).applyPermission(permissionDescriptor);
    }


    @Override
    public Component getOwnComponent(String id) {
        return layoutWrapper.getOwnComponent(id);
    }

    @Nullable
    @Override
    public Component getComponent(String id) {
        return layoutWrapper.getComponent(id);
    }

    @Override
    public Collection<Component> getOwnComponents() {
        return layoutWrapper.getOwnComponents();
    }

    @Override
    public Stream<Component> getOwnComponentsStream() {
        return layoutWrapper.getOwnComponentsStream();
    }

    @Override
    public Collection<Component> getComponents() {
        return layoutWrapper.getComponents();
    }


    @Override
    public Subscription addWizardStepChangeListener(Consumer<WizardStepChangeEvent> listener) {
        return getEventHub().subscribe(WizardStepChangeEvent.class, listener);
    }

    @Override
    public void removeWizardStepChangeListener(Consumer<WizardStepChangeEvent> listener) {
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