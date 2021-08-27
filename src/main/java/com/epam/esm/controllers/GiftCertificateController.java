package com.epam.esm.controllers;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.RequiredFieldsMissingException;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping(value = "/gift")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;
    private final MessageSource messageSource;

    private static final String ID = "id";
    private static final String ID_PATH = "/{id}";
    private static final String FIELD_NAME = "fieldName";
    private static final String PART_OF_FIELD = "partOfField";
    private static final String TAG_NAME = "tagName";
    private static final String IS_ASC = "isAsc";

    private static final String JSON = "application/json";
    private static final String RESOURCE_NOT_FOUND = "resource.not.found";
    private static final String SUCCESSFUL_OPERATION = "operation.successful";
    private static final String REQUIRED_FIELDS_MISSING = "field.missing";
    private static final String ILLEGAL_ARGUMENT = "argument.illegal";

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService, MessageSource messageSource) {
        this.giftCertificateService = giftCertificateService;
        this.messageSource = messageSource;
    }

    @GetMapping(produces = JSON)
    public ResponseEntity<?> getAll(Locale locale) {
        List<GiftCertificate> giftCertificates = giftCertificateService.getAll();
        if (giftCertificates.isEmpty()) {
            return new ResponseEntity<>(messageSource.getMessage(RESOURCE_NOT_FOUND, null, locale), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(giftCertificates, HttpStatus.OK);
    }

    @GetMapping(value = ID_PATH, produces = JSON)
    public ResponseEntity<?> getById(@PathVariable(ID) long id, Locale locale) {
        GiftCertificate giftCertificate = giftCertificateService.getById(id);
        if (giftCertificate == null) {
            return new ResponseEntity<>(messageSource.getMessage(RESOURCE_NOT_FOUND, null, locale), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(giftCertificate, HttpStatus.OK);
    }

    @PostMapping(produces = JSON)
    public ResponseEntity<?> create(@RequestBody GiftCertificate giftCertificate, Locale locale) {
        ResponseEntity<String> responseEntity;

        try {
            giftCertificateService.create(giftCertificate);
            responseEntity = new ResponseEntity<>(messageSource.getMessage(SUCCESSFUL_OPERATION, null, locale), HttpStatus.CREATED);
        } catch (RequiredFieldsMissingException e) {
            responseEntity = new ResponseEntity<>(messageSource.getMessage(REQUIRED_FIELDS_MISSING, null, locale), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            responseEntity = new ResponseEntity<>(messageSource.getMessage(ILLEGAL_ARGUMENT, null, locale), HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }

    @PatchMapping(value = ID_PATH, produces = JSON)
    public ResponseEntity<?> updateById(@PathVariable long id, @RequestBody GiftCertificate newGiftCertificate, Locale locale) {
        GiftCertificate oldGiftCertificate = giftCertificateService.getById(id);
        if (oldGiftCertificate == null) {
            return new ResponseEntity<>(messageSource.getMessage(RESOURCE_NOT_FOUND, null, locale), HttpStatus.NOT_FOUND);
        }

        ResponseEntity<?> responseEntity;
        try {
            giftCertificateService.updateById(id, oldGiftCertificate, newGiftCertificate);
            responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            responseEntity = new ResponseEntity<>(messageSource.getMessage(ILLEGAL_ARGUMENT, null, locale), HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }

    @DeleteMapping(value = ID_PATH, produces = JSON)
    public ResponseEntity<?> deleteById(@PathVariable long id, Locale locale) {
        GiftCertificate giftCertificate = giftCertificateService.getById(id);
        if (giftCertificate == null) {
            return new ResponseEntity<>(messageSource.getMessage(RESOURCE_NOT_FOUND, null, locale), HttpStatus.NOT_FOUND);
        }

        giftCertificateService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(params = { FIELD_NAME, PART_OF_FIELD }, produces = JSON)
    public ResponseEntity<?> getByPartOfField(@RequestParam String fieldName, @RequestParam String partOfField, Locale locale) {
        ResponseEntity<?> responseEntity;
        List<GiftCertificate> giftCertificates;

        try {
            giftCertificates = giftCertificateService.getByPartOfField(fieldName, partOfField);

            if (giftCertificates.isEmpty()) {
                responseEntity = new ResponseEntity<>(RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND);
            } else {
                responseEntity = new ResponseEntity<>(giftCertificates, HttpStatus.OK);
            }
        } catch (IllegalArgumentException e) {
            responseEntity = new ResponseEntity<>(messageSource.getMessage(ILLEGAL_ARGUMENT, null, locale), HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }

    @GetMapping(params = TAG_NAME, produces = JSON)
    public ResponseEntity<?> getByTagName(@RequestParam String tagName, Locale locale) {
        List<GiftCertificate> giftCertificates = giftCertificateService.getByTagName(tagName);
        if (giftCertificates.isEmpty()) {
            return new ResponseEntity<>(messageSource.getMessage(RESOURCE_NOT_FOUND, null, locale), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(giftCertificates, HttpStatus.OK);
    }

    @GetMapping(params = { FIELD_NAME, IS_ASC } , produces = JSON)
    public ResponseEntity<?> sortByFieldInGivenOrder(@RequestParam String fieldName, @RequestParam boolean isAsc, Locale locale) {
        ResponseEntity<?> responseEntity;
        List<GiftCertificate> giftCertificates;
        try {
            giftCertificates = giftCertificateService.sortByFieldInGivenOrder(fieldName, isAsc);

            if (giftCertificates.isEmpty()) {
                responseEntity = new ResponseEntity<>(messageSource.getMessage(RESOURCE_NOT_FOUND, null, locale), HttpStatus.NOT_FOUND);
            } else {
                responseEntity = new ResponseEntity<>(giftCertificates, HttpStatus.OK);
            }
        } catch (IllegalArgumentException e) {
            responseEntity = new ResponseEntity<>(messageSource.getMessage(ILLEGAL_ARGUMENT, null, locale), HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }
}
