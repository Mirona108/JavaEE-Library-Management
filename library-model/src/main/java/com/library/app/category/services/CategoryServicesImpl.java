package com.library.app.category.services;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.library.app.category.exception.CategoryExistentException;
import com.library.app.category.exception.CategoryNotFoundException;
import com.library.app.category.model.Category;
import com.library.app.category.repository.CategoryRepository;
import com.library.app.common.exception.FieldNotValidException;

public class CategoryServicesImpl implements CategoryServices {

	Validator validator;
	CategoryRepository categoryRepository;

	@Override
	public Category add(Category category) {
		validateCategory(category);

		return categoryRepository.add(category);
	}

	@Override
	public void update(Category category) {
		validateCategory(category);

		if (!categoryRepository.existsById(category.getId())) {
			throw new CategoryNotFoundException();
		}

		categoryRepository.update(category);
	}

	@Override
	public List<Category> findAll() {
		return categoryRepository.findAll("name");
	}

	@Override
	public Category findById(Long id) throws CategoryNotFoundException {
		Category category = categoryRepository.findById(id);
		if (category == null) {
			throw new CategoryNotFoundException();
		}
		return category;
	}

	private void validateCategory(Category category) {
		validateCategoryFields(category);

		if (categoryRepository.alreadyExists(category)) {
			throw new CategoryExistentException();
		}
	}

	private void validateCategoryFields(Category category) {
		Set<ConstraintViolation<Category>> errors = validator.validate(category);
		Iterator<ConstraintViolation<Category>> itErrors = errors.iterator();
		if (itErrors.hasNext()) {
			ConstraintViolation<Category> violation = itErrors.next();
			throw new FieldNotValidException(violation.getPropertyPath().toString(), violation.getMessage());
		}
	}

}
