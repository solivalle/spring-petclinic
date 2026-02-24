package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PetFinderServiceTests {

	private PetFinderService petFinderService;

	@BeforeEach
	void setUp() {
		petFinderService = new PetFinderService();
	}

	private Pet createPet(Integer id, String name) {
		Pet pet = new Pet();
		pet.setId(id);
		pet.setName(name);
		return pet;
	}

	@Test
	void shouldFindPetById() {
		Pet pet1 = createPet(1, "Max");
		Pet pet2 = createPet(2, "Bella");

		List<Pet> pets = List.of(pet1, pet2);

		Optional<Pet> result = petFinderService.findPetById(pets, 2);

		assertTrue(result.isPresent());
		assertEquals("Bella", result.get().getName());
	}

	@Test
	void shouldReturnEmptyWhenIdIsNull() {
		List<Pet> pets = List.of(createPet(1, "Max"));

		Optional<Pet> result = petFinderService.findPetById(pets, null);

		assertTrue(result.isEmpty());
	}

	@Test
	void shouldReturnEmptyWhenPetIdNotFound() {
		List<Pet> pets = List.of(createPet(1, "Max"));

		Optional<Pet> result = petFinderService.findPetById(pets, 99);

		assertTrue(result.isEmpty());
	}

	@Test
	void shouldIgnoreNewPetsWhenFindingById() {
		Pet newPet = new Pet(); // id null → isNew() = true
		newPet.setName("NewPet");

		List<Pet> pets = List.of(newPet);

		Optional<Pet> result = petFinderService.findPetById(pets, null);

		assertTrue(result.isEmpty());
	}

	@Test
	void shouldFindPetByNameIgnoringCase() {
		Pet pet1 = createPet(1, "Max");
		List<Pet> pets = List.of(pet1);

		Optional<Pet> result = petFinderService.findPetByName(pets, "max", false);

		assertTrue(result.isPresent());
		assertEquals("Max", result.get().getName());
	}

	@Test
	void shouldReturnEmptyWhenNameIsNull() {
		List<Pet> pets = List.of(createPet(1, "Max"));

		Optional<Pet> result = petFinderService.findPetByName(pets, null, false);

		assertTrue(result.isEmpty());
	}

	@Test
	void shouldIgnoreNewPetsWhenFlagIsTrue() {
		Pet newPet = new Pet(); // id null → isNew() = true
		newPet.setName("Max");

		List<Pet> pets = List.of(newPet);

		Optional<Pet> result = petFinderService.findPetByName(pets, "Max", true);

		assertTrue(result.isEmpty());
	}

	@Test
	void shouldIncludeNewPetsWhenFlagIsFalse() {
		Pet newPet = new Pet(); // id null → isNew() = true
		newPet.setName("Max");

		List<Pet> pets = new ArrayList<>();
		pets.add(newPet);

		Optional<Pet> result = petFinderService.findPetByName(pets, "Max", false);

		assertTrue(result.isPresent());
	}

}
