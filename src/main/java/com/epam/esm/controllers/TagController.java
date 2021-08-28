package com.epam.esm.controllers;

import com.epam.esm.entity.ErrorInfo;
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

    @GetMapping(produces = JSON)
    public ResponseEntity<?> getAll() {
        List<Tag> tagList = tagService.getAll();

        return new ResponseEntity<>(tagList, HttpStatus.OK);
    }

    @GetMapping(value = ID_PATH, produces = JSON)
    public ResponseEntity<?> getById(@PathVariable(ID) long id) {
        Tag tag = tagService.getById(id);

        return new ResponseEntity<>(tag, HttpStatus.OK);
    }

    @PostMapping(produces = JSON)
    public ResponseEntity<?> create(@RequestBody Tag tag) {
        tagService.create(tag);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

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
