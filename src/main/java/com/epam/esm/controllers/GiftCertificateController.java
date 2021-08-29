package com.epam.esm.controllers;

import com.epam.esm.entity.ErrorInfo;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.RequiredFieldsMissingException;
import com.epam.esm.exception.ResourceNotFoundException;
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
    private static final String REQUIRED_FIELDS_MISSING = "field.missing";
    private static final String ILLEGAL_ARGUMENT = "argument.illegal";

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService, MessageSource messageSource) {
        this.giftCertificateService = giftCertificateService;
        this.messageSource = messageSource;
    }

    @GetMapping(produces = JSON)
    public ResponseEntity<?> getAll() {
        List<GiftCertificate> giftCertificates = giftCertificateService.getAll();
        return new ResponseEntity<>(giftCertificates, HttpStatus.OK);
    }

    @GetMapping(value = ID_PATH, produces = JSON)
    public ResponseEntity<?> getById(@PathVariable(ID) long id) {
        GiftCertificate giftCertificate = giftCertificateService.getById(id);
        return new ResponseEntity<>(giftCertificate, HttpStatus.OK);
    }

    @PostMapping(produces = JSON)
    public ResponseEntity<?> create(@RequestBody GiftCertificate giftCertificate) {
        giftCertificateService.create(giftCertificate);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping(value = ID_PATH, produces = JSON)
    public ResponseEntity<?> updateById(@PathVariable long id, @RequestBody GiftCertificate newGiftCertificate) {
        giftCertificateService.updateById(id, newGiftCertificate);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = ID_PATH, produces = JSON)
    public ResponseEntity<?> deleteById(@PathVariable long id) {
        giftCertificateService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(params = { FIELD_NAME, PART_OF_FIELD }, produces = JSON)
    public ResponseEntity<?> getByPartOfField(@RequestParam String fieldName, @RequestParam String partOfField) {
        List<GiftCertificate> giftCertificates = giftCertificateService.getByPartOfField(fieldName, partOfField);
        return new ResponseEntity<>(giftCertificates, HttpStatus.OK);
    }

    @GetMapping(params = TAG_NAME, produces = JSON)
    public ResponseEntity<?> getByTagName(@RequestParam String tagName) {
        List<GiftCertificate> giftCertificates = giftCertificateService.getByTagName(tagName);
        return new ResponseEntity<>(giftCertificates, HttpStatus.OK);
    }

    @GetMapping(params = { FIELD_NAME, IS_ASC }, produces = JSON)
    public ResponseEntity<?> sortByFieldInGivenOrder(@RequestParam String fieldName, @RequestParam boolean isAsc) {
        List<GiftCertificate> giftCertificates = giftCertificateService.sortByFieldInGivenOrder(fieldName, isAsc);
        return new ResponseEntity<>(giftCertificates, HttpStatus.OK);

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorInfo> handleIllegalArgumentException(Locale locale) {
        ErrorInfo errorInfo = new ErrorInfo(messageSource.getMessage(ILLEGAL_ARGUMENT, null, locale), 40001);
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RequiredFieldsMissingException.class)
    public ResponseEntity<ErrorInfo> handleRequiredFieldsMissingException(Locale locale) {
        ErrorInfo errorInfo = new ErrorInfo(messageSource.getMessage(REQUIRED_FIELDS_MISSING, null, locale), 40001);
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorInfo> handleResourceNotFoundException(Locale locale) {
        ErrorInfo errorInfo = new ErrorInfo(messageSource.getMessage(RESOURCE_NOT_FOUND, null, locale), 40401);
        return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
    }
}
