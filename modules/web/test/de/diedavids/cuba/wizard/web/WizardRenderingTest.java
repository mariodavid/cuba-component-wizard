package de.diedavids.cuba.wizard.web;

import static org.assertj.core.api.Assertions.assertThat;

import com.haulmont.cuba.gui.components.ComponentContainer;
import com.haulmont.cuba.web.app.main.MainScreen;
import de.diedavids.cuba.wizard.DdcwWebTestContainer;
import de.diedavids.cuba.wizard.gui.components.simple.SimpleWizard;
import de.diedavids.cuba.wizard.web.screens.sample.cuba7.WizardTestScreen;
import de.diedavids.sneferu.environment.SneferuTestUiEnvironment;
import de.diedavids.sneferu.screen.StandardScreenTestAPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

class WizardRenderingTest {

    @RegisterExtension
    SneferuTestUiEnvironment environment =
        new SneferuTestUiEnvironment(DdcwWebTestContainer.Common.INSTANCE)
            .withScreenPackages(
                "com.haulmont.cuba.web.app.main",
                "de.diedavids.cuba.wizard.web.screens"
            )
            .withUserLogin("admin")
            .withMainScreen(MainScreen.class);
    private StandardScreenTestAPI<WizardTestScreen> wizardScreen;
    private SimpleWizard wizard;

    @BeforeEach
    void setUp() {

        wizardScreen = environment
            .getUiTestAPI().openStandardScreen(WizardTestScreen.class);

        wizard = (SimpleWizard) wizardScreen.screen().getWindow().getComponent("wizard");

    }

    @Test
    void when_screenIsLoaded_then_wizardComponentIsAvailable() {

        assertThat(wizard)
            .isNotNull();

        final ComponentContainer step1Tab = (ComponentContainer) wizard.getTabComponent("step1Tab");

        assertThat(step1Tab.getComponent("checkBtn"))
            .isNotNull();
    }


    @Test
    void when_screenIsLoaded_then_firstTabIsActive() {

        assertThat(wizard.getSelectedTab())
            .isEqualTo(wizard.getTab("step1Tab"));
    }

    @Test
    void when_screenIsLoaded_then_onlyTheFirstTabIsEnabled() {

        assertThat(wizard.getTab("step1Tab").isEnabled())
            .isTrue();

        assertThat(wizard.getTab("step2Tab").isEnabled())
            .isFalse();
    }



}