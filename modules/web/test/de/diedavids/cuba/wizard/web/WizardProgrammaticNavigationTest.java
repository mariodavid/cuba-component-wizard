package de.diedavids.cuba.wizard.web;

import static org.assertj.core.api.Assertions.assertThat;

import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.web.app.main.MainScreen;
import de.diedavids.cuba.wizard.DdcwWebTestContainer;
import de.diedavids.cuba.wizard.gui.components.Wizard;
import de.diedavids.cuba.wizard.gui.components.Wizard.Direction;
import de.diedavids.cuba.wizard.gui.components.Wizard.WizardTabChangeEvent;
import de.diedavids.cuba.wizard.web.screens.sample.four_steps.FourStepsWizard;
import de.diedavids.cuba.wizard.web.screens.sample.simple.SimpleWizard;
import de.diedavids.sneferu.environment.SneferuTestUiEnvironment;
import de.diedavids.sneferu.screen.StandardScreenTestAPI;
import java.util.EventObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

class WizardProgrammaticNavigationTest {

    private Wizard wizard;

    @RegisterExtension
    SneferuTestUiEnvironment environment =
        new SneferuTestUiEnvironment(DdcwWebTestContainer.Common.INSTANCE)
            .withScreenPackages(
                "com.haulmont.cuba.web.app.main",
                "de.diedavids.cuba.wizard.web.screens"
            )
            .withUserLogin("admin")
            .withMainScreen(MainScreen.class);
    private StandardScreenTestAPI<FourStepsWizard> wizardScreen;

    @BeforeEach
    void setUp() {
        wizardScreen = environment
            .getUiTestAPI().openStandardScreen(FourStepsWizard.class);

        wizard = (Wizard) wizardScreen.screen().getWindow().getComponent("wizard");
    }

    @Test
    void given_step1IsActive_when_nextTabIsPerformed_then_step2IsActive() {


        // given:
        ensureTabIsActive("step1Tab");

        // when:
        wizard.nextTab();

        // then:
        ensureTabIsActive("step2Tab");
    }

    @Test
    void given_step1IsActive_when_tab3IsOpened_then_step3IsActive() {

        // given:
        ensureTabIsActive("step1Tab");

        // when:
        wizard.setSelectedTab("step3Tab");

        // then:
        ensureTabIsActive("step3Tab");

        // and:
        assertThat(event(WizardTabChangeEvent.class).getDirection())
            .isEqualTo(Direction.NEXT);
    }

    @Test
    void given_tabChangeIsPrevented_when_tab3IsOpened_then_tabChangeHasNotBeenExecuted() {

        // given:
        ensureTabIsActive("step1Tab");

        // and:
        wizard.addWizardTabPreChangeListener(event -> event.preventTabChange());

        // when:
        wizard.setSelectedTab("step3Tab");

        // then:
        ensureTabIsActive("step1Tab");
    }

    private void ensureTabIsActive(String tabName) {
        assertThat(wizard.getSelectedTab())
            .isEqualTo(wizard.getTab(tabName));
    }

    @Test
    void given_tab3IsActive_when_tab1IsOpened_then_tab1IsActive() {

        // given:
        wizard.setSelectedTab("step3Tab");

        // when:
        wizard.setSelectedTab("step1Tab");

        // then:
        ensureTabIsActive("step1Tab");

        // and:
        assertThat(event(WizardTabChangeEvent.class).getDirection())
            .isEqualTo(Direction.PREVIOUS);
    }


    private <T extends EventObject> T event(Class<T> clazz) {
        return wizardScreen.screen().receivedEvent(clazz);
    }
    private Button button(Wizard wizard, String buttonId) {
        return (Button) wizard.getComponent(buttonId);
    }
}