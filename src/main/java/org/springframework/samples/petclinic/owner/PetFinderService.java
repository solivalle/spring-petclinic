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

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

/**
 * Service for finding pets within an owner's collection. Part of the Strangler Fig
 * refactoring to extract search logic from domain models. Uses functional programming
 * with Streams for cleaner, more maintainable code.
 *
 * @author Tech Debt Refactoring Team
 */
@Service
public class PetFinderService {

	/**
	 * Finds a pet by ID within the owner's pets collection.
	 * @param pets the list of pets to search
	 * @param id the ID to search for
	 * @return Optional containing the pet if found
	 */
	public Optional<Pet> findPetById(List<Pet> pets, Integer id) {
		if (id == null) {
			return Optional.empty();
		}

		return pets.stream().filter(pet -> !pet.isNew()).filter(pet -> Objects.equals(pet.getId(), id)).findFirst();
	}

	/**
	 * Finds a pet by name within the owner's pets collection.
	 * @param pets the list of pets to search
	 * @param name the name to search for (case-insensitive)
	 * @param ignoreNew whether to exclude new (unsaved) pets from the search
	 * @return Optional containing the pet if found
	 */
	public Optional<Pet> findPetByName(List<Pet> pets, String name, boolean ignoreNew) {
		if (name == null) {
			return Optional.empty();
		}

		return pets.stream()
			.filter(pet -> name.equalsIgnoreCase(pet.getName()))
			.filter(pet -> !ignoreNew || !pet.isNew())
			.findFirst();
	}

}
