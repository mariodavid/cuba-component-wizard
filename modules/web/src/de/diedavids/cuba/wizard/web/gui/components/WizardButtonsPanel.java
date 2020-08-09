package de.diedavids.cuba.wizard.web.gui.components;

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.Action.ActionPerformedEvent;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.ButtonsPanel;
import com.haulmont.cuba.gui.components.Component.Alignment;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.actions.BaseAction.EnabledRule;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class WizardButtonsPanel {

    private final UiComponents uiComponents;
    private final Messages messages;

    private final Map<WizardButtonType, BaseAction> actions = new HashMap<>();

    public WizardButtonsPanel(
        UiComponents uiComponents,
        Messages messages
    ) {
        this.uiComponents = uiComponents;
        this.messages = messages;
    }

    public void refresh() {

        actions
            .forEach((key, value) ->
                value.refreshState()
            );
    }

    public enum WizardButtonType {
        CANCEL("cancel"),
        PREVIOUS("prev"),
        NEXT("next"),
        FINISH("finish");

        private final String id;

        WizardButtonType(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    static public WizardButtonDescriptor buttonDescriptor(
        WizardButtonType type,
        Consumer<ActionPerformedEvent> clickHandler,
        EnabledRule enabledRule
    ) {
        return new WizardButtonDescriptor(
            type,
            clickHandler,
            enabledRule
        );
    }

    public static class WizardButtonDescriptor {
        final WizardButtonType type;
        final Consumer<ActionPerformedEvent> clickHandler;
        final EnabledRule enabledRule;

        public WizardButtonDescriptor(
            WizardButtonType type,
            Consumer<ActionPerformedEvent> clickHandler,
            EnabledRule enabledRule
        ) {
            this.type = type;
            this.clickHandler = clickHandler;
            this.enabledRule = enabledRule;
        }
    }

    class WizardButtonDescriptors {
        final List<WizardButtonDescriptor> buttonDescriptors;

        WizardButtonDescriptors(
            List<WizardButtonDescriptor> buttonDescriptors
        ) {
            this.buttonDescriptors = buttonDescriptors;
        }

        WizardButtonDescriptor byType(WizardButtonType type) {
            return buttonDescriptors.stream()
                .filter(wizardButtonDescriptor -> wizardButtonDescriptor.type.equals(type))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Wizard Button Type not found"));
        }
    }

    public ButtonsPanel createWizardButtonPanel(
        List<WizardButtonDescriptor> buttonDescriptors
    ) {

        final WizardButtonDescriptors descriptors = new WizardButtonDescriptors(
            buttonDescriptors
        );

        ButtonsPanel wizardButtonsPanel = uiComponents.create(ButtonsPanel.class);
        wizardButtonsPanel.setAlignment(Alignment.TOP_RIGHT);

        wizardButtonsPanel.add(createCancelBtn(descriptors.byType(WizardButtonType.CANCEL)));
        wizardButtonsPanel.add(createBtn(descriptors.byType(WizardButtonType.PREVIOUS)));
        wizardButtonsPanel.add(createBtn(descriptors.byType(WizardButtonType.NEXT)));
        wizardButtonsPanel.add(createBtn(descriptors.byType(WizardButtonType.FINISH)));

        return wizardButtonsPanel;
    }

    private Button createCancelBtn(
        WizardButtonDescriptor descriptor
    ) {
        Button btn = createBtn(descriptor);
        btn.setTabIndex(-1);
        return btn;
    }

    private Button createBtn(
        WizardButtonDescriptor descriptor
    ) {
        String id = descriptor.type.getId();
        Button btn = uiComponents.create(Button.class);
        btn.setId(id);
        btn.setCaption(messages.getMessage(this.getClass(), id + "BtnCaption"));
        btn.setIcon(messages.getMessage(this.getClass(), id + "BtnIcon"));

        BaseAction action = wizardAction(btn, descriptor);
        btn.setAction(action);
        actions.put(descriptor.type, action);

        return btn;
    }


    private BaseAction wizardAction(
        Button button,
        WizardButtonDescriptor handler
    ) {
        final BaseAction action = new BaseAction(button.getId())
            .withHandler(handler.clickHandler);
        action.addEnabledRule(handler.enabledRule);
        return action;
    }

}