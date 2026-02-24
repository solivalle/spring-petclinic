package org.springframework.samples.petclinic;

import org.junit.jupiter.api.Test;
import org.springframework.aot.hint.RuntimeHints;

import static org.assertj.core.api.Assertions.assertThatNoException;

public class PetClinicRuntimeHintsTests {

	@Test
	void registerHintsShouldExecuteWithoutErrors() {
		RuntimeHints hints = new RuntimeHints();
		PetClinicRuntimeHints registrar = new PetClinicRuntimeHints();

		assertThatNoException().isThrownBy(() -> registrar.registerHints(hints, getClass().getClassLoader()));
	}

}
