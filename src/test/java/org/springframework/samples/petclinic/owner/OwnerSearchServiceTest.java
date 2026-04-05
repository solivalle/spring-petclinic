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

		when(ownerRepository.findByLastNameStartingWith(anyString(), any(Pageable.class))).thenReturn(emptyPage);

		OwnerSearchService.SearchResult result = service.findOwnersByLastName(1, "Smith");

		assertThat(result.getSearchType()).isEqualTo(OwnerSearchService.SearchType.NO_RESULTS);

		assertThat(result.isEmpty()).isTrue();
	}

	@Test
	void shouldReturnSingleResultWhenOneOwnerFound() {
		Owner owner = new Owner();
		Page<Owner> page = new PageImpl<>(List.of(owner));

		when(ownerRepository.findByLastNameStartingWith(anyString(), any(Pageable.class))).thenReturn(page);

		OwnerSearchService.SearchResult result = service.findOwnersByLastName(1, "Smith");

		assertThat(result.getSearchType()).isEqualTo(OwnerSearchService.SearchType.SINGLE_RESULT);

		assertThat(result.isSingleResult()).isTrue();
		assertThat(result.getSingleResult()).isEqualTo(owner);
	}

	@Test
	void shouldReturnMultipleResultsWhenMoreThanOneOwnerFound() {
		Owner owner1 = new Owner();
		Owner owner2 = new Owner();

		Page<Owner> page = new PageImpl<>(List.of(owner1, owner2));

		when(ownerRepository.findByLastNameStartingWith(anyString(), any(Pageable.class))).thenReturn(page);

		OwnerSearchService.SearchResult result = service.findOwnersByLastName(1, "Smith");

		assertThat(result.getSearchType()).isEqualTo(OwnerSearchService.SearchType.MULTIPLE_RESULTS);

		assertThat(result.isSingleResult()).isFalse();
	}

	@Test
	void shouldThrowExceptionWhenGettingSingleResultButMultipleExist() {
		Owner owner1 = new Owner();
		Owner owner2 = new Owner();

		Page<Owner> page = new PageImpl<>(List.of(owner1, owner2));

		when(ownerRepository.findByLastNameStartingWith(anyString(), any(Pageable.class))).thenReturn(page);

		OwnerSearchService.SearchResult result = service.findOwnersByLastName(1, "Smith");

		assertThatThrownBy(result::getSingleResult).isInstanceOf(IllegalStateException.class)
			.hasMessageContaining("Cannot get single result");
	}

	@Test
	void shouldNormalizeNullSearchTermToEmptyString() {
		Page<Owner> emptyPage = Page.empty();

		when(ownerRepository.findByLastNameStartingWith(eq(""), any(Pageable.class))).thenReturn(emptyPage);

		service.findOwnersByLastName(1, null);

		verify(ownerRepository).findByLastNameStartingWith(eq(""), any(Pageable.class));
	}

	@Test
	void shouldUseCorrectPaginationIndex() {
		Page<Owner> emptyPage = Page.empty();

		when(ownerRepository.findByLastNameStartingWith(anyString(), any(Pageable.class))).thenReturn(emptyPage);

		service.findOwnersByLastName(2, "Smith");

		verify(ownerRepository).findByLastNameStartingWith(eq("Smith"),
				argThat(pageable -> pageable.getPageNumber() == 1 && pageable.getPageSize() == 5));
	}

	@Test
	void shouldReturnNoResultsWhenPageIsEmptyForSingleOwner() {
		Page<SingleOwner> emptyPage = Page.empty();

		when(ownerRepository.findSingleOwnerByLastNameStartingWith(anyString(), any(Pageable.class)))
			.thenReturn(emptyPage);

		OwnerSearchService.SearchResult result = service.findSingleOwnersByLastName(1, "Smith");

		assertThat(result.getSearchType()).isEqualTo(OwnerSearchService.SearchType.NO_RESULTS);

		assertThat(result.isEmpty()).isTrue();
	}

	@Test
	void shouldReturnSingleResultWhenOneOwnerFoundForSingleOwner() {
		SingleOwner owner = new SingleOwner() {
			@Override
			public String getFirstName() {
				return "";
			}

			@Override
			public String getLastName() {
				return "";
			}

			@Override
			public String getAddress() {
				return "";
			}

			@Override
			public String getCity() {
				return "";
			}

			@Override
			public String getTelephone() {
				return "";
			}

			@Override
			public Integer getId() {
				return 0;
			}
		};
		Page<SingleOwner> page = new PageImpl<>(List.of(owner));

		when(ownerRepository.findSingleOwnerByLastNameStartingWith(anyString(), any(Pageable.class))).thenReturn(page);

		OwnerSearchService.SearchResult result = service.findSingleOwnersByLastName(1, "Smith");

		assertThat(result.getSearchType()).isEqualTo(OwnerSearchService.SearchType.SINGLE_RESULT);

		assertThat(result.isSingleResult()).isTrue();
		assertThat(result.getSingleResult()).isEqualTo(owner);
	}

	@Test
	void shouldReturnMultipleResultsWhenMoreThanOneOwnerFoundForSingleOwner() {
		SingleOwner owner1 = new SingleOwner() {
			@Override
			public String getFirstName() {
				return "";
			}

			@Override
			public String getLastName() {
				return "";
			}

			@Override
			public String getAddress() {
				return "";
			}

			@Override
			public String getCity() {
				return "";
			}

			@Override
			public String getTelephone() {
				return "";
			}

			@Override
			public Integer getId() {
				return 0;
			}
		};
		SingleOwner owner2 = new SingleOwner() {
			@Override
			public String getFirstName() {
				return "";
			}

			@Override
			public String getLastName() {
				return "";
			}

			@Override
			public String getAddress() {
				return "";
			}

			@Override
			public String getCity() {
				return "";
			}

			@Override
			public String getTelephone() {
				return "";
			}

			@Override
			public Integer getId() {
				return 0;
			}
		};

		Page<SingleOwner> page = new PageImpl<>(List.of(owner1, owner2));

		when(ownerRepository.findSingleOwnerByLastNameStartingWith(anyString(), any(Pageable.class))).thenReturn(page);

		OwnerSearchService.SearchResult result = service.findSingleOwnersByLastName(1, "Smith");

		assertThat(result.getSearchType()).isEqualTo(OwnerSearchService.SearchType.MULTIPLE_RESULTS);

		assertThat(result.isSingleResult()).isFalse();
	}

	@Test
	void shouldThrowExceptionWhenGettingSingleResultButMultipleExistForSingleOwner() {
		SingleOwner owner1 = new SingleOwner() {
			@Override
			public String getFirstName() {
				return "";
			}

			@Override
			public String getLastName() {
				return "";
			}

			@Override
			public String getAddress() {
				return "";
			}

			@Override
			public String getCity() {
				return "";
			}

			@Override
			public String getTelephone() {
				return "";
			}

			@Override
			public Integer getId() {
				return 0;
			}
		};
		SingleOwner owner2 = new SingleOwner() {
			@Override
			public String getFirstName() {
				return "";
			}

			@Override
			public String getLastName() {
				return "";
			}

			@Override
			public String getAddress() {
				return "";
			}

			@Override
			public String getCity() {
				return "";
			}

			@Override
			public String getTelephone() {
				return "";
			}

			@Override
			public Integer getId() {
				return 0;
			}
		};

		Page<SingleOwner> page = new PageImpl<>(List.of(owner1, owner2));

		when(ownerRepository.findSingleOwnerByLastNameStartingWith(anyString(), any(Pageable.class))).thenReturn(page);

		OwnerSearchService.SearchResult result = service.findSingleOwnersByLastName(1, "Smith");

		assertThatThrownBy(result::getSingleResult).isInstanceOf(IllegalStateException.class)
			.hasMessageContaining("Cannot get single result");
	}

	@Test
	void shouldNormalizeNullSearchTermToEmptyStringForSingleOwner() {
		Page<SingleOwner> emptyPage = Page.empty();

		when(ownerRepository.findSingleOwnerByLastNameStartingWith(eq(""), any(Pageable.class))).thenReturn(emptyPage);

		service.findSingleOwnersByLastName(1, null);

		verify(ownerRepository).findSingleOwnerByLastNameStartingWith(eq(""), any(Pageable.class));
	}

	@Test
	void shouldUseCorrectPaginationIndexForSingleOwner() {
		Page<SingleOwner> emptyPage = Page.empty();

		when(ownerRepository.findSingleOwnerByLastNameStartingWith(anyString(), any(Pageable.class)))
			.thenReturn(emptyPage);

		service.findSingleOwnersByLastName(2, "Smith");

		verify(ownerRepository).findSingleOwnerByLastNameStartingWith(eq("Smith"),
				argThat(pageable -> pageable.getPageNumber() == 1 && pageable.getPageSize() == 5));
	}

}
