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

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Service for managing Pet business logic. Part of the Strangler Fig refactoring to
 * extract business logic from controllers.
 *
 * @author Tech Debt Refactoring Team
 */
@Service
public class PetService {

	private final OwnerRepository ownerRepository;

	public PetService(OwnerRepository ownerRepository) {
		this.ownerRepository = ownerRepository;
	}

	/**
	 * Creates a new pet and associates it with the given owner.
	 * @param owner the owner to associate the pet with
	 * @param pet the pet to create
	 * @return the saved owner
	 */
	@Transactional
	public Owner createPet(Owner owner, Pet pet) {
		owner.addPet(pet);
		return ownerRepository.save(owner);
	}

	/**
	 * Updates an existing pet's details or adds it if it doesn't exist. This method
	 * implements the Single Responsibility Principle by separating the update logic from
	 * the controller.
	 * @param owner the owner of the pet
	 * @param pet the pet with updated details
	 * @return the saved owner
	 */
	@Transactional
	public Owner updatePet(Owner owner, Pet pet) {
		Integer petId = pet.getId();
		Assert.notNull(petId, "Pet ID must not be null for update operation");

		Pet existingPet = owner.getPet(petId);

		if (existingPet != null) {
			updateExistingPetDetails(existingPet, pet);
		}
		else {
			owner.addPet(pet);
		}

		return ownerRepository.save(owner);
	}

	/**
	 * Updates the properties of an existing pet.
	 * @param existingPet the pet to update
	 * @param updatedPet the pet with new values
	 */
	private void updateExistingPetDetails(Pet existingPet, Pet updatedPet) {
		existingPet.setName(updatedPet.getName());
		existingPet.setBirthDate(updatedPet.getBirthDate());
		existingPet.setType(updatedPet.getType());
	}

}
