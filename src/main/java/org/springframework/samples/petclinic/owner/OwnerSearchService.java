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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service for searching and managing owner queries.
 * Part of the Strangler Fig refactoring to extract business logic from controllers.
 * Implements Single Responsibility Principle for search operations.
 *
 * @author Tech Debt Refactoring Team
 */
@Service
public class OwnerSearchService {

	private static final int DEFAULT_PAGE_SIZE = 5;

	private final OwnerRepository ownerRepository;

	public OwnerSearchService(OwnerRepository ownerRepository) {
		this.ownerRepository = ownerRepository;
	}

	/**
	 * Search result container with additional metadata.
	 */
	public static class SearchResult {

		private final Page<Owner> owners;

		private final SearchType searchType;

		public SearchResult(Page<Owner> owners, SearchType searchType) {
			this.owners = owners;
			this.searchType = searchType;
		}

		public Page<Owner> getOwners() {
			return owners;
		}

		public SearchType getSearchType() {
			return searchType;
		}

		public boolean isEmpty() {
			return owners.isEmpty();
		}

		public boolean isSingleResult() {
			return owners.getTotalElements() == 1;
		}

		public Owner getSingleResult() {
			if (!isSingleResult()) {
				throw new IllegalStateException("Cannot get single result when multiple or no results exist");
			}
			return owners.iterator().next();
		}

	}

	/**
	 * Enum representing the type of search performed.
	 */
	public enum SearchType {

		NO_RESULTS, SINGLE_RESULT, MULTIPLE_RESULTS

	}

	/**
	 * Searches for owners by last name with pagination.
	 * @param page the page number (1-indexed)
	 * @param lastName the last name to search for (empty string for all owners)
	 * @return the search result with owners and metadata
	 */
	public SearchResult findOwnersByLastName(int page, String lastName) {
		String searchTerm = normalizeSearchTerm(lastName);
		Page<Owner> owners = findPaginatedByLastName(page, searchTerm);

		SearchType type = determineSearchType(owners);
		return new SearchResult(owners, type);
	}

	/**
	 * Normalizes the search term - converts null to empty string for broadest search.
	 * @param lastName the last name to normalize
	 * @return the normalized search term
	 */
	private String normalizeSearchTerm(String lastName) {
		return lastName == null ? "" : lastName;
	}

	/**
	 * Determines the type of search result based on the number of owners found.
	 * @param owners the page of owners
	 * @return the search type
	 */
	private SearchType determineSearchType(Page<Owner> owners) {
		if (owners.isEmpty()) {
			return SearchType.NO_RESULTS;
		}
		else if (owners.getTotalElements() == 1) {
			return SearchType.SINGLE_RESULT;
		}
		else {
			return SearchType.MULTIPLE_RESULTS;
		}
	}

	/**
	 * Finds owners by last name with pagination.
	 * @param page the page number (1-indexed)
	 * @param lastName the last name prefix to search for
	 * @return page of owners
	 */
	private Page<Owner> findPaginatedByLastName(int page, String lastName) {
		Pageable pageable = PageRequest.of(page - 1, DEFAULT_PAGE_SIZE);
		return ownerRepository.findByLastNameStartingWith(lastName, pageable);
	}

}
