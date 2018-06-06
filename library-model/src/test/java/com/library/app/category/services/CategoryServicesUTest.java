package com.library.app.category.services;

import static com.library.app.commontests.category.CategoryForTestsRepository.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;

import com.library.app.category.exception.CategoryExistentException;
import com.library.app.category.exception.CategoryNotFoundException;
import com.library.app.category.model.Category;
import com.library.app.category.repository.CategoryRepository;
import com.library.app.common.exception.FieldNotValidException;

public class CategoryServicesUTest {

	private CategoryServices categoryServices;
	private CategoryRepository categoryRepository;
	private Validator validator;

	@Before
	public void initTestCase() {
		categoryServices = new CategoryServicesImpl();
		categoryRepository = mock(CategoryRepository.class);

		validator = Validation.buildDefaultValidatorFactory().getValidator();
		((CategoryServicesImpl) categoryServices).validator = validator;
		((CategoryServicesImpl) categoryServices).categoryRepository = categoryRepository;
	}

	@Test
	public void addCategoryWithNullName() {
		addCategoryWithInvalidName(null);
	}

	@Test
	public void addCategoryWithShortName() {
		addCategoryWithInvalidName("A");
	}

	@Test
	public void addCategoryWithLongName() {
		addCategoryWithInvalidName("sfdsdfsdfdgfdgfdgfdgdfgdfgfdgdfgfdgfdgfdgfdgfgfgfdgfdgfdgfdg");
	}

	@Test
	public void updateCategoryWithNullName() {
		updateCategoryWithInvalidName(null);
	}

	@Test
	public void updateCategoryWithShortName() {
		updateCategoryWithInvalidName("A");
	}

	@Test
	public void updateCategoryWithLongName() {
		updateCategoryWithInvalidName("sfdsdfsdfdgfdgfdgfdgdfgdfgfdgdfgfdgfdgfdgfdgfgfgfdgfdgfdgfdg");
	}

	@Test(expected = CategoryExistentException.class)
	public void addCategoryWithExistentName() {
		when(categoryRepository.alreadyExists(java())).thenReturn(true);

		categoryServices.add(java());
	}

	@Test(expected = CategoryExistentException.class)
	public void updateCategoryWithExistentName() {
		when(categoryRepository.alreadyExists(categoryWithId(java(), 1L))).thenReturn(true);

		categoryServices.update(categoryWithId(java(), 1L));
	}

	@Test(expected = CategoryNotFoundException.class)
	public void updateCategoryNotFound() {
		when(categoryRepository.alreadyExists(categoryWithId(java(), 1L))).thenReturn(false);
		when(categoryRepository.existsById(1L)).thenReturn(false);

		categoryServices.update(categoryWithId(java(), 1L));
	}

	@Test
	public void updateValidCategory() {
		when(categoryRepository.alreadyExists(categoryWithId(java(), 1L))).thenReturn(false);
		when(categoryRepository.existsById(1L)).thenReturn(true);

		categoryServices.update(categoryWithId(java(), 1L));
		verify(categoryRepository).update(categoryWithId(java(), 1L));
	}

	@Test
	public void addValidCategory() {
		when(categoryRepository.alreadyExists(java())).thenReturn(false);
		when(categoryRepository.add(java())).thenReturn(categoryWithId(java(), 1L));

		Category categoryAdded = categoryServices.add(java());
		assertThat(categoryAdded.getId(), is(equalTo(1L)));
	}

	@Test
	public void findCategoryById() {
		when(categoryRepository.findById(1L)).thenReturn(categoryWithId(java(), 1L));

		final Category category = categoryServices.findById(1L);
		assertThat(category, is(notNullValue()));
		assertThat(category.getId(), is(equalTo(1L)));
		assertThat(category.getName(), is(equalTo(java().getName())));
	}

	@Test(expected = CategoryNotFoundException.class)
	public void findCategoryByIdNotFound() {
		when(categoryRepository.findById(1L)).thenReturn(null);

		categoryServices.findById(1L);
	}

	@Test
	public void findAllNoCategories() {
		when(categoryRepository.findAll("name")).thenReturn(new ArrayList<>());

		List<Category> categories = categoryServices.findAll();
		assertThat(categories.isEmpty(), is(equalTo(true)));
	}

	@Test
	public void findAllCategories() {
		when(categoryRepository.findAll("name")).thenReturn(
				Arrays.asList(categoryWithId(java(), 1L), categoryWithId(networks(), 2L)));

		final List<Category> categories = categoryServices.findAll();
		assertThat(categories.size(), is(equalTo(2)));
		assertThat(categories.get(0).getName(), is(equalTo(java().getName())));
		assertThat(categories.get(1).getName(), is(equalTo(networks().getName())));

	}

	private void addCategoryWithInvalidName(String name) {
		try {
			categoryServices.add(new Category(name));
			fail("An error should have been thrown");
		} catch (FieldNotValidException e) {
			assertThat(e.getFieldName(), is(equalTo("name")));
		}
	}

	private void updateCategoryWithInvalidName(String name) {
		try {
			categoryServices.update(new Category(name));
			fail("An error should have been thrown");
		} catch (FieldNotValidException e) {
			assertThat(e.getFieldName(), is(equalTo("name")));
		}
	}
}
