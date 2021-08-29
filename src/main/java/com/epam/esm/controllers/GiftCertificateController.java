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

/**
 * This is a class that represents an API and provides basic operations for interactions with the application.
 *
 * @author Nikita Torop
 */
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

    /**
     * Returns a {@link ResponseEntity} object with a {@link HttpStatus} and a {@link List} object containing {@link GiftCertificate} objects
     * that are retrieved from a database or an {@link ErrorInfo} object if nothing was retrieved.
     * @return {@link ResponseEntity} with a {@link HttpStatus} and a {@link List} of {@link GiftCertificate} objects or a {@link ErrorInfo} object.
     * @throws {@link ResourceNotFoundException} if nothing is retrieved from a database.
     */
    @GetMapping(produces = JSON)
    public ResponseEntity<?> getAll() {
        List<GiftCertificate> giftCertificates = giftCertificateService.getAll();
        return new ResponseEntity<>(giftCertificates, HttpStatus.OK);
    }

    /**
     * Returns a {@link ResponseEntity} object with a {@link HttpStatus} and a {@link GiftCertificate} object
     * that is retrieved from a database by its id or an {@link ErrorInfo} object if nothing was retrieved.
     * @param id - the {@link GiftCertificate} object's id that is to be retrieved from a database.
     * @return {@link ResponseEntity} with a {@link HttpStatus} and a {@link GiftCertificate} object or a {@link ErrorInfo} object.
     * @throws {@link ResourceNotFoundException} if nothing is retrieved from a database
     */
    @GetMapping(value = ID_PATH, produces = JSON)
    public ResponseEntity<?> getById(@PathVariable(ID) long id) {
        GiftCertificate giftCertificate = giftCertificateService.getById(id);
        return new ResponseEntity<>(giftCertificate, HttpStatus.OK);
    }

    /**
     * Creates a {@link GiftCertificate} object in a database and returns with a {@link ResponseEntity} object containing {@link HttpStatus}
     * that represents the result of execution and a {@link ErrorInfo} object if the object wasn't created in a database.
     * @param giftCertificate - the {@link GiftCertificate} object that is to be created in a database.
     * @return {@link ResponseEntity} with a {@link HttpStatus} alone or additionally with a {@link ErrorInfo} object.
     * @throws {@link RequiredFieldsMissingException} if the parameter object lacks any values required for its creation in a database.
     * @throws {@link IllegalArgumentException} if the parameter object's price or duration values are lower than 0.
     */
    @PostMapping(produces = JSON)
    public ResponseEntity<?> create(@RequestBody GiftCertificate giftCertificate) {
        giftCertificateService.create(giftCertificate);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Updates a {@link GiftCertificate} object in a database and returns with a {@link ResponseEntity} object containing {@link HttpStatus}
     * that represents the result of execution and a {@link ErrorInfo} object if the object wasn't updated in a database.
     * @param id - the {@link GiftCertificate} object's id that is to be updated in a database.
     * @param newGiftCertificate - the {@link GiftCertificate} object which has the new values for update in a database.
     * @return {@link ResponseEntity} with a {@link HttpStatus} alone or additionally with a {@link ErrorInfo} object.
     * @throws {@link ResourceNotFoundException} if the object with such an id doesn't exist in a database.
     * @throws {@link IllegalArgumentException} if the parameter object's price or duration values are lower than 0.
     */
    @PatchMapping(value = ID_PATH, produces = JSON)
    public ResponseEntity<?> updateById(@PathVariable long id, @RequestBody GiftCertificate newGiftCertificate) {
        giftCertificateService.updateById(id, newGiftCertificate);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Deletes a {@link GiftCertificate} object in a database and returns with a {@link ResponseEntity} object containing {@link HttpStatus}
     * that represents the result of execution and a {@link ErrorInfo} object if the object wasn't deleted in a database.
     * @param id - the {@link GiftCertificate} object's id that is to be deleted in a database.
     * @return {@link ResponseEntity} with a {@link HttpStatus} alone or additionally with a {@link ErrorInfo} object.
     * @throws {@link ResourceNotFoundException} if the object with such an id doesn't exist in a database.
     */
    @DeleteMapping(value = ID_PATH, produces = JSON)
    public ResponseEntity<?> deleteById(@PathVariable long id) {
        giftCertificateService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Returns a {@link ResponseEntity} object with a {@link HttpStatus} and a {@link List} object containing {@link GiftCertificate} objects
     * that are retrieved from a database by part of one of their fields or an {@link ErrorInfo} object if nothing was retrieved.
     * @param fieldName - the name of the field which will be used for searching {@link GiftCertificate} objects in a database.
     * @param partOfField - the value that will be used for searching
     * @return {@link ResponseEntity} with a {@link HttpStatus} and a {@link List} of {@link GiftCertificate} objects or a {@link ErrorInfo} object.
     * @throws {@link ResourceNotFoundException} if nothing is retrieved from a database.
     * @throws {@link IllegalArgumentException} if the fieldName doesn't match any of the fields in a database.
     */
    @GetMapping(params = { FIELD_NAME, PART_OF_FIELD }, produces = JSON)
    public ResponseEntity<?> getByPartOfField(@RequestParam String fieldName, @RequestParam String partOfField) {
        List<GiftCertificate> giftCertificates = giftCertificateService.getByPartOfField(fieldName, partOfField);
        return new ResponseEntity<>(giftCertificates, HttpStatus.OK);
    }

    /**
     * Returns a {@link ResponseEntity} object with a {@link HttpStatus} and a {@link List} object containing {@link GiftCertificate} objects
     * that are retrieved from a database by the name of the tag that is associated with them or an {@link ErrorInfo} object if nothing was retrieved.
     * @param tagName - the name of the tag which will be used for searching {@link GiftCertificate} objects in a database.
     * @return {@link ResponseEntity} with a {@link HttpStatus} and a {@link List} of {@link GiftCertificate} objects or a {@link ErrorInfo} object.
     * @throws {@link ResourceNotFoundException} if a tag with such a name doesn't exist or if nothing is retrieved from a database.
     */
    @GetMapping(params = TAG_NAME, produces = JSON)
    public ResponseEntity<?> getByTagName(@RequestParam String tagName) {
        List<GiftCertificate> giftCertificates = giftCertificateService.getByTagName(tagName);
        return new ResponseEntity<>(giftCertificates, HttpStatus.OK);
    }

    /**
     * Returns a {@link ResponseEntity} object with a {@link HttpStatus} and a {@link List} object containing {@link GiftCertificate} objects
     * that are retrieved from a database and sorted by a given field in a given order or an {@link ErrorInfo} object if nothing was retrieved.
     * @param fieldName - the name of the tag which will be used for searching {@link GiftCertificate} objects in a database.
     * @param isAsc - the sorting order. The order will be ascending if this parameter equals true and descending if it equals false.
     * @return {@link ResponseEntity} with a {@link HttpStatus} and a {@link List} of {@link GiftCertificate} objects or a {@link ErrorInfo} object.
     * @throws {@link ResourceNotFoundException} if nothing is retrieved from a database.
     * @throws {@link IllegalArgumentException} if the fieldName doesn't match any of the fields in a database.
     */
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
