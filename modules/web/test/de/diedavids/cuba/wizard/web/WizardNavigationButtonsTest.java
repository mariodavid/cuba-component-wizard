package de.diedavids.cuba.wizard.web;

import static org.assertj.core.api.Assertions.assertThat;

import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.web.app.main.MainScreen;
import de.diedavids.cuba.wizard.DdcwWebTestContainer;
import de.diedavids.cuba.wizard.gui.components.simple.SimpleWizard;
import de.diedavids.cuba.wizard.web.screens.sample.cuba7.WizardTestScreen;
import de.diedavids.sneferu.environment.SneferuTestUiEnvironment;
import de.diedavids.sneferu.screen.StandardScreenTestAPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

class WizardNavigationButtonsTest {

    private SimpleWizard wizard;

    @RegisterExtension
    SneferuTestUiEnvironment environment =
        new SneferuTestUiEnvironment(DdcwWebTestContainer.Common.INSTANCE)
            .withScreenPackages(
                "com.haulmont.cuba.web.app.main",
                "de.diedavids.cuba.wizard.web.screens"
            )
            .withUserLogin("admin")
            .withMainScreen(MainScreen.class);

    @BeforeEach
    void setUp() {
        final StandardScreenTestAPI<WizardTestScreen> wizardScreen = environment
            .getUiTestAPI().openStandardScreen(WizardTestScreen.class);

        wizard = (SimpleWizard) wizardScreen.screen().getWindow().getComponent("wizard");
    }

    @Test
    void when_screenIsLoaded_then_wizardButtonPanelsAreAvailable() {

        // when:
        assertThat(wizard)
            .isNotNull();

        // then:
        assertThat(button(wizard, "next").isEnabled())
            .isTrue();
        assertThat(button(wizard, "prev").isEnabled())
            .isFalse();
        assertThat(button(wizard, "finish").isEnabled())
            .isFalse();
    }


    @Test
    void when_nextStepIsPerformed_then_wizardButtonStateChangesAccordingly() {

        // when:
        wizard.nextStep();

        // then:
        assertThat(button(wizard, "next").isEnabled())
            .isFalse();
        assertThat(button(wizard, "prev").isEnabled())
            .isTrue();
        assertThat(button(wizard, "finish").isEnabled())
            .isTrue();
    }


    @Test
    void when_nextStepIsPerformed_then_secondStepIsActive() {

        // when:
        wizard.nextStep();

        // then:
        assertThat(wizard.getSelectedTab())
            .isEqualTo(wizard.getTab("step2Tab"));

        assertThat(wizard.getTab("step1Tab").isEnabled())
            .isFalse();
    }

    @Test
    void when_previousStepIsPerformed_then_wizardButtonStateChangesAccordingly() {

        // given:
        wizard.nextStep();

        // when:
        wizard.previousStep();

        // then:
        assertThat(button(wizard, "next").isEnabled())
            .isTrue();
        assertThat(button(wizard, "prev").isEnabled())
            .isFalse();
        assertThat(button(wizard, "finish").isEnabled())
            .isFalse();
    }

    @Test
    void when_previousStepIsPerformed_then_firstStepIsActive() {

        // given:
        wizard.nextStep();

        // when:
        wizard.previousStep();

        // then:
        assertThat(wizard.getSelectedTab())
            .isEqualTo(wizard.getTab("step1Tab"));

        assertThat(wizard.getTab("step2Tab").isEnabled())
            .isFalse();
    }

    private Button button(SimpleWizard wizard, String buttonId) {
        return (Button) wizard.getComponent(buttonId);
    }
}