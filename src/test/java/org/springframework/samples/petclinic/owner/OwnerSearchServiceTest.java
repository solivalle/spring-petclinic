package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerSearchServiceTest {

	@Mock
	private OwnerRepository ownerRepository;

	private OwnerSearchService service;

	@BeforeEach
	void setUp() {
		service = new OwnerSearchService(ownerRepository);
	}

	@Test
	void shouldReturnNoResultsWhenPageIsEmpty() {
		Page<Owner> emptyPage = Page.empty();

		when(ownerRepository.findByLastNameStartingWith(anyString(), any(Pageable.class)))
			.thenReturn(emptyPage);

		OwnerSearchService.SearchResult result =
			service.findOwnersByLastName(1, "Smith");

		assertThat(result.getSearchType())
			.isEqualTo(OwnerSearchService.SearchType.NO_RESULTS);

		assertThat(result.isEmpty()).isTrue();
	}

	@Test
	void shouldReturnSingleResultWhenOneOwnerFound() {
		Owner owner = new Owner();
		Page<Owner> page = new PageImpl<>(List.of(owner));

		when(ownerRepository.findByLastNameStartingWith(anyString(), any(Pageable.class)))
			.thenReturn(page);

		OwnerSearchService.SearchResult result =
			service.findOwnersByLastName(1, "Smith");

		assertThat(result.getSearchType())
			.isEqualTo(OwnerSearchService.SearchType.SINGLE_RESULT);

		assertThat(result.isSingleResult()).isTrue();
		assertThat(result.getSingleResult()).isEqualTo(owner);
	}

	@Test
	void shouldReturnMultipleResultsWhenMoreThanOneOwnerFound() {
		Owner owner1 = new Owner();
		Owner owner2 = new Owner();

		Page<Owner> page = new PageImpl<>(List.of(owner1, owner2));

		when(ownerRepository.findByLastNameStartingWith(anyString(), any(Pageable.class)))
			.thenReturn(page);

		OwnerSearchService.SearchResult result =
			service.findOwnersByLastName(1, "Smith");

		assertThat(result.getSearchType())
			.isEqualTo(OwnerSearchService.SearchType.MULTIPLE_RESULTS);

		assertThat(result.isSingleResult()).isFalse();
	}

	@Test
	void shouldThrowExceptionWhenGettingSingleResultButMultipleExist() {
		Owner owner1 = new Owner();
		Owner owner2 = new Owner();

		Page<Owner> page = new PageImpl<>(List.of(owner1, owner2));

		when(ownerRepository.findByLastNameStartingWith(anyString(), any(Pageable.class)))
			.thenReturn(page);

		OwnerSearchService.SearchResult result =
			service.findOwnersByLastName(1, "Smith");

		assertThatThrownBy(result::getSingleResult)
			.isInstanceOf(IllegalStateException.class)
			.hasMessageContaining("Cannot get single result");
	}

	@Test
	void shouldNormalizeNullSearchTermToEmptyString() {
		Page<Owner> emptyPage = Page.empty();

		when(ownerRepository.findByLastNameStartingWith(eq(""), any(Pageable.class)))
			.thenReturn(emptyPage);

		service.findOwnersByLastName(1, null);

		verify(ownerRepository)
			.findByLastNameStartingWith(eq(""), any(Pageable.class));
	}

	@Test
	void shouldUseCorrectPaginationIndex() {
		Page<Owner> emptyPage = Page.empty();

		when(ownerRepository.findByLastNameStartingWith(anyString(), any(Pageable.class)))
			.thenReturn(emptyPage);

		service.findOwnersByLastName(2, "Smith");

		verify(ownerRepository).findByLastNameStartingWith(
			eq("Smith"),
			argThat(pageable ->
				pageable.getPageNumber() == 1 &&
					pageable.getPageSize() == 5
			)
		);
	}
}
