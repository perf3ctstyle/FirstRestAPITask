package com.epam.esm.controllers;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.RequiredFieldsMissingException;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private static final String OPERATION_SUCCESSFUL = "operation.successful";
    private static final String REQUIRED_FIELDS_MISSING = "field.missing";

    @Autowired
    public TagController(TagService tagService, MessageSource messageSource) {
        this.tagService = tagService;
        this.messageSource = messageSource;
    }

    @GetMapping(produces = JSON)
    public ResponseEntity<?> getAll(Locale locale) {
        List<Tag> tagList = tagService.getAll();
        if (tagList.isEmpty()) {
            return new ResponseEntity<>(messageSource.getMessage(RESOURCE_NOT_FOUND, null, locale), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(tagList, HttpStatus.OK);
    }

    @GetMapping(value = ID_PATH, produces = JSON)
    public ResponseEntity<?> getById(@PathVariable(ID) long id, Locale locale) {
        Tag tag = tagService.getById(id);
        if (tag == null) {
            return new ResponseEntity<>(messageSource.getMessage(RESOURCE_NOT_FOUND, null, locale), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(tag, HttpStatus.OK);
    }

    @PostMapping(produces = JSON)
    public ResponseEntity<?> create(@RequestBody Tag tag, Locale locale) {
        ResponseEntity<String> responseEntity;
        try {
            tagService.create(tag);
            responseEntity = new ResponseEntity<>(messageSource.getMessage(OPERATION_SUCCESSFUL, null, locale), HttpStatus.CREATED);
        } catch (RequiredFieldsMissingException e) {
            responseEntity = new ResponseEntity<>(messageSource.getMessage(REQUIRED_FIELDS_MISSING, null, locale), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityAlreadyExistsException e) {
            responseEntity = new ResponseEntity<>(messageSource.getMessage(RESOURCE_ALREADY_EXISTS, null, locale), HttpStatus.CONFLICT);
        }

        return responseEntity;
    }

    @DeleteMapping(value = "/{id}", produces = JSON)
    public ResponseEntity<?> deleteById(@PathVariable(ID) long id, Locale locale) {
        Tag tag = tagService.getById(id);
        if (tag == null) {
            return new ResponseEntity<>(messageSource.getMessage(RESOURCE_NOT_FOUND, null, locale), HttpStatus.NOT_FOUND);
        }

        tagService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
