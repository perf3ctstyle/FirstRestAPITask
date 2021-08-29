package com.epam.esm.controllers;

import com.epam.esm.entity.ErrorInfo;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceAlreadyExistsException;
import com.epam.esm.exception.RequiredFieldsMissingException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/tag")
public class TagController {

    private final TagService tagService;
    private final MessageSource messageSource;

    private static final String ID = "id";
    private static final String ID_PATH = "/{id}";

    private static final String JSON = "application/json";
    private static final String RESOURCE_NOT_FOUND = "resource.not.found";
    private static final String RESOURCE_ALREADY_EXISTS = "resource.exists";
    private static final String REQUIRED_FIELDS_MISSING = "field.missing";

    @Autowired
    public TagController(TagService tagService, MessageSource messageSource) {
        this.tagService = tagService;
        this.messageSource = messageSource;
    }

    /**
     * Returns a {@link ResponseEntity} object with a {@link HttpStatus} and a {@link List} object containing {@link Tag} objects
     * that are retrieved from a database or an {@link ErrorInfo} object if nothing was retrieved.
     * @return {@link ResponseEntity} with a {@link HttpStatus} and a {@link List} of {@link Tag} objects or a {@link ErrorInfo} object.
     * @throws {@link ResourceNotFoundException} if nothing is retrieved from a database.
     */
    @GetMapping(produces = JSON)
    public ResponseEntity<?> getAll() {
        List<Tag> tagList = tagService.getAll();
        return new ResponseEntity<>(tagList, HttpStatus.OK);
    }

    /**
     * Returns a {@link ResponseEntity} object with a {@link HttpStatus} and a {@link Tag} object
     * that is retrieved from a database by its id or an {@link ErrorInfo} object if nothing was retrieved.
     * @param id - the {@link Tag} object's id that is to be retrieved from a database.
     * @return {@link ResponseEntity} with a {@link HttpStatus} and a {@link Tag} object or a {@link ErrorInfo} object.
     * @throws {@link ResourceNotFoundException} if nothing is retrieved from a database
     */
    @GetMapping(value = ID_PATH, produces = JSON)
    public ResponseEntity<?> getById(@PathVariable(ID) long id) {
        Tag tag = tagService.getById(id);
        return new ResponseEntity<>(tag, HttpStatus.OK);
    }

    /**
     * Creates a {@link Tag} object in a database and returns with a {@link ResponseEntity} object containing {@link HttpStatus}
     * that represents the result of execution and a {@link ErrorInfo} object if the object wasn't created in a database.
     * @param tag - the {@link Tag} object that is to be created in a database.
     * @return {@link ResponseEntity} with a {@link HttpStatus} alone or additionally with a {@link ErrorInfo} object.
     * @throws {@link RequiredFieldsMissingException} if the parameter object lacks any values required for its creation in a database.
     * @throws {@link ResourceAlreadyExistsException} if a tag with the same name already exists in a database.
     */
    @PostMapping(produces = JSON)
    public ResponseEntity<?> create(@RequestBody Tag tag) {
        tagService.create(tag);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Deletes a {@link Tag} object in a database and returns with a {@link ResponseEntity} object containing {@link HttpStatus}
     * that represents the result of execution and a {@link ErrorInfo} object if the object wasn't deleted in a database.
     * @param id - the {@link Tag} object's id that is to be deleted in a database.
     * @return {@link ResponseEntity} with a {@link HttpStatus} alone or additionally with a {@link ErrorInfo} object.
     * @throws {@link ResourceNotFoundException} if the object with such an id doesn't exist in a database.
     */
    @DeleteMapping(value = "/{id}", produces = JSON)
    public ResponseEntity<?> deleteById(@PathVariable(ID) long id) {
        tagService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(RequiredFieldsMissingException.class)
    public ResponseEntity<ErrorInfo> handleRequiredFieldsMissingException(Locale locale) {
        ErrorInfo errorInfo = new ErrorInfo(messageSource.getMessage(REQUIRED_FIELDS_MISSING, null, locale), 40002);
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorInfo> handleResourceAlreadyExistsException(Locale locale) {
        ErrorInfo errorInfo = new ErrorInfo(messageSource.getMessage(RESOURCE_ALREADY_EXISTS, null, locale), 40902);
        return new ResponseEntity<>(errorInfo, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorInfo> handleResourceNotFoundException(Locale locale) {
        ErrorInfo errorInfo = new ErrorInfo(messageSource.getMessage(RESOURCE_NOT_FOUND, null, locale), 40402);
        return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
    }
}
