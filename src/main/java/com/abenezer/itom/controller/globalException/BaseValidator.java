package com.abenezer.itom.controller.globalException;

import org.apache.log4j.Logger;
import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Date;

/**
 * Superclass for all validators used to check the form data and determine if the data entered into
 * the system is valid.
 *
 * Extend this class and implement the abstract method validateForm in order to add custom
 * validation.
 *
 * @param <T> class definition of the form Javabean
 */
//@Component
public abstract class BaseValidator<T> implements Validator {

  private static final Logger LOGGER = Logger.getLogger(BaseValidator.class);


  @Override
  public boolean supports(Class<?> clazz) {
    Class<?> thisClazz = GenericTypeResolver.resolveTypeArgument(getClass(), BaseValidator.class);
    return thisClazz.isAssignableFrom(clazz);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void validate(Object target, Errors errors) {
    LOGGER.trace("Start validation for " + getFormName());

    if (target == null) {
      return;
    }

    T backingForm = (T) target;
    validateForm(backingForm, errors);

    LOGGER.trace("End validator for " + getFormName() + " with errors " + errors);
  }

  /**
   * Method to override in order to use BaseValidator and add custom validations. This method is
   * called by the BaseValidator.
   *
   * @param form   T JavaBean with form data
   * @param errors Errors containing any validator errors
   */
  public abstract void validateForm(T form, Errors errors);

  /**
   * Specifies the name of the form which is used to retrieve Form configuration
   *
   * @return String name of the form
   */
  public abstract String getFormName();

  /**
   * Specifies if the form has a base entity. This will be used when generating the error keys. By
   * default is true.
   *
   * @return true, if successful
   */
  protected boolean formHasBaseEntity() {
    return true;
  }




  /**
   * Determines if the specified date is in the future or not
   *
   * @param date Date to validate
   * @return boolean if date is after the current timestamp, false is not
   */
  protected boolean isFutureDate(Date date) {
    Date today = new Date();
    if (date.after(today)) {
      return true;
    } else {
      return false;
    }
  }
}
