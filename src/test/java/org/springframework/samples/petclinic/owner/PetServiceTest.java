package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

	@Mock
	private OwnerRepository ownerRepository;

	private PetService petService;

	@BeforeEach
	void setUp() {
		petService = new PetService(ownerRepository);
	}

	@Test
	void createPet_shouldAddPetToOwnerAndSave() {
		Owner owner = mock(Owner.class);
		Pet pet = new Pet();

		when(ownerRepository.save(owner)).thenReturn(owner);

		Owner result = petService.createPet(owner, pet);

		verify(owner).addPet(pet);
		verify(ownerRepository).save(owner);
		assertThat(result).isEqualTo(owner);
	}

	@Test
	void updatePet_shouldUpdateExistingPetWhenFound() {
		Owner owner = mock(Owner.class);

		Pet existingPet = new Pet();
		existingPet.setId(1);

		Pet updatedPet = new Pet();
		updatedPet.setId(1);
		updatedPet.setName("UpdatedName");
		updatedPet.setBirthDate(LocalDate.of(2020, 1, 1));

		when(owner.getPet(1)).thenReturn(existingPet);
		when(ownerRepository.save(owner)).thenReturn(owner);

		Owner result = petService.updatePet(owner, updatedPet);

		verify(owner).getPet(1);
		verify(ownerRepository).save(owner);

		assertThat(existingPet.getName()).isEqualTo("UpdatedName");
		assertThat(existingPet.getBirthDate()).isEqualTo(LocalDate.of(2020, 1, 1));
		assertThat(result).isEqualTo(owner);
	}

	@Test
	void updatePet_shouldAddPetWhenNotExisting() {
		Owner owner = mock(Owner.class);

		Pet newPet = new Pet();
		newPet.setId(2);

		when(owner.getPet(2)).thenReturn(null);
		when(ownerRepository.save(owner)).thenReturn(owner);

		Owner result = petService.updatePet(owner, newPet);

		verify(owner).getPet(2);
		verify(owner).addPet(newPet);
		verify(ownerRepository).save(owner);

		assertThat(result).isEqualTo(owner);
	}

	@Test
	void updatePet_shouldThrowExceptionWhenPetIdIsNull() {
		Owner owner = mock(Owner.class);
		Pet pet = new Pet(); // id is null

		assertThatThrownBy(() -> petService.updatePet(owner, pet)).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("Pet ID must not be null");

		verifyNoInteractions(ownerRepository);
	}

}
