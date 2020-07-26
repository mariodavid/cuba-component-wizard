package de.diedavids.cuba.wizard.web;

import static org.assertj.core.api.Assertions.assertThat;

import com.haulmont.cuba.gui.components.ComponentContainer;
import com.haulmont.cuba.web.app.main.MainScreen;
import de.diedavids.cuba.wizard.DdcwWebTestContainer;
import de.diedavids.cuba.wizard.gui.components.simple.SimpleWizard;
import de.diedavids.cuba.wizard.web.screens.sample.cuba7.WizardNew;
import de.diedavids.sneferu.environment.SneferuTestUiEnvironment;
import de.diedavids.sneferu.screen.StandardScreenTestAPI;
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

    @Test
    void when_screenIsLoaded_then_wizardComponentIsAvailable() {

        final StandardScreenTestAPI<WizardNew> wizardScreen = environment
            .getUiTestAPI().openStandardScreen(WizardNew.class);

        final SimpleWizard wizard = (SimpleWizard) wizardScreen.screen().getWindow().getComponent("wizard");

        assertThat(wizard)
            .isNotNull();

        final ComponentContainer helloTabContainer = (ComponentContainer) wizard.getTabComponent("step1Tab");

        assertThat(helloTabContainer.getComponent("checkBtn"))
            .isNotNull();
    }

}