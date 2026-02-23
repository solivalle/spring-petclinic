/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.owner;

import java.time.LocalDate;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

/**
 * Service for validating Pet business rules. Part of the Strangler Fig refactoring to
 * extract validation logic from controllers.
 *
 * @author Tech Debt Refactoring Team
 */
@Service
public class PetValidationService {

	/**
	 * Validates that a pet name is unique for the given owner.
	 * @param pet the pet being validated
	 * @param owner the owner of the pet
	 * @param errors the errors object to register validation failures
	 */
	public void validateUniquePetName(Pet pet, Owner owner, Errors errors) {
		if (!StringUtils.hasText(pet.getName())) {
			return;
		}

		if (isNewPetWithDuplicateName(pet, owner)) {
			errors.rejectValue("name", "duplicate", "already exists");
		}
		else if (isExistingPetWithNameConflict(pet, owner)) {
			errors.rejectValue("name", "duplicate", "already exists");
		}
	}

	/**
	 * Validates that the pet's birth date is not in the future.
	 * @param pet the pet being validated
	 * @param errors the errors object to register validation failures
	 */
	public void validateBirthDate(Pet pet, Errors errors) {
		if (pet.getBirthDate() == null) {
			return;
		}

		LocalDate currentDate = LocalDate.now();
		if (pet.getBirthDate().isAfter(currentDate)) {
			errors.rejectValue("birthDate", "typeMismatch.birthDate");
		}
	}

	/**
	 * Performs all standard validations for a pet.
	 * @param pet the pet being validated
	 * @param owner the owner of the pet
	 * @param errors the errors object to register validation failures
	 */
	public void validatePet(Pet pet, Owner owner, Errors errors) {
		validateUniquePetName(pet, owner, errors);
		validateBirthDate(pet, errors);
	}

	private boolean isNewPetWithDuplicateName(Pet pet, Owner owner) {
		return pet.isNew() && owner.getPet(pet.getName(), true) != null;
	}

	private boolean isExistingPetWithNameConflict(Pet pet, Owner owner) {
		Pet existingPet = owner.getPet(pet.getName(), false);
		return existingPet != null && !Objects.equals(existingPet.getId(), pet.getId());
	}

}
