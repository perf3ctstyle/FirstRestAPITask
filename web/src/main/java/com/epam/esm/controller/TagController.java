package com.epam.esm.controller;

import com.epam.esm.entity.ErrorInfo;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.ResourceAlreadyExistsException;
import com.epam.esm.exception.RequiredFieldsMissingException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.TagService;
import com.epam.esm.util.ControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Locale;

/**
 * This is a class that represents an API and provides basic operations for manipulations with Tag entities.
 *
 * @author Nikita Torop
 */
@RestController
@RequestMapping("/tag")
public class TagController {

    public static final int BAD_TAG_RECEIVED_CODE = 40002;
    public static final int TAG_ALREADY_EXISTS_CODE = 40902;
    public static final int TAG_NOT_FOUND_CODE = 40402;
    public static final int DAO_EXCEPTION_CODE = 50002;
    private final TagService tagService;
    private final MessageSource messageSource;

    private static final String ID = "id";
    private static final String ID_PATH = "/{id}";

    private static final String JSON = "application/json";
    private static final String RESOURCE_NOT_FOUND = "resource.not.found";
    private static final String RESOURCE_ALREADY_EXISTS = "resource.exists";
    private static final String REQUIRED_FIELDS_MISSING = "field.missing";
    private static final String INTERNAL_ERROR = "error.internal";

    @Autowired
    public TagController(TagService tagService, MessageSource messageSource) {
        this.tagService = tagService;
        this.messageSource = messageSource;
    }

    /**
     * Returns all {@link Tag} objects from a database.
     * @return {@link ResponseEntity} with a {@link HttpStatus} and a {@link List} of {@link Tag} objects.
     */
    @GetMapping(produces = JSON)
    public ResponseEntity<?> getAll() {
        List<Tag> tagList = tagService.getAll();
        return new ResponseEntity<>(tagList, HttpStatus.OK);
    }

    /**
     * Returns a {@link Tag} object from a database by its id or throws {@link ResourceNotFoundException} if nothing is retrieved from a database
     * or {@link DaoException} in the case of unexpected behaviour on a Dao level.
     * @param id - the {@link Tag} object's id that is to be retrieved from a database.
     * @return {@link ResponseEntity} with a {@link HttpStatus} and a {@link Tag} object or a {@link ErrorInfo} object.
     */
    @GetMapping(value = ID_PATH, produces = JSON)
    public ResponseEntity<?> getById(@PathVariable(ID) long id) {
        Tag tag = tagService.getById(id);
        return new ResponseEntity<>(tag, HttpStatus.OK);
    }

    /**
     * Creates a {@link Tag} object in a database or throws {@link RequiredFieldsMissingException} if some fields
     * required for creation are missing or {@link ResourceAlreadyExistsException} if the tag with the same name already exists.
     * @param tag - the {@link Tag} object that is to be created in a database.
     * @return {@link ResponseEntity} with a {@link HttpStatus} alone or additionally with a {@link ErrorInfo} object.
     */
    @PostMapping(produces = JSON)
    public ResponseEntity<?> create(@RequestBody Tag tag) {
        tagService.create(tag);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Deletes a {@link Tag} object in a database by its id or throws {@link ResourceNotFoundException} if the object
     * with such id doesn't exist.
     * @param id - the {@link Tag} object's id that is to be deleted in a database.
     * @return {@link ResponseEntity} with a {@link HttpStatus} alone or additionally with a {@link ErrorInfo} object.
     */
    @DeleteMapping(value = "/{id}", produces = JSON)
    public ResponseEntity<?> deleteById(@PathVariable(ID) long id) {
        tagService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(RequiredFieldsMissingException.class)
    public ResponseEntity<ErrorInfo> handleRequiredFieldsMissingException(Locale locale) {
        return ControllerUtils.createResponseEntityWithSpecifiedErrorInfo(messageSource.getMessage(REQUIRED_FIELDS_MISSING, null, locale),
                BAD_TAG_RECEIVED_CODE,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorInfo> handleResourceAlreadyExistsException(Locale locale) {
        return ControllerUtils.createResponseEntityWithSpecifiedErrorInfo(messageSource.getMessage(RESOURCE_ALREADY_EXISTS, null, locale),
                TAG_ALREADY_EXISTS_CODE,
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorInfo> handleResourceNotFoundException(Locale locale) {
        return ControllerUtils.createResponseEntityWithSpecifiedErrorInfo(messageSource.getMessage(RESOURCE_NOT_FOUND, null, locale),
                TAG_NOT_FOUND_CODE,
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DaoException.class)
    public ResponseEntity<ErrorInfo> handleDaoException(Locale locale) {
        return ControllerUtils.createResponseEntityWithSpecifiedErrorInfo(messageSource.getMessage(INTERNAL_ERROR, null, locale),
                DAO_EXCEPTION_CODE,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
