package com.epam.esm.controller;

import com.epam.esm.entity.ErrorInfo;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.RequiredFieldsMissingException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.util.ControllerUtils;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;

/**
 * This is a class that represents an API and provides basic operations for manipulations with Gift Certificate entities.
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
    private static final String INTERNAL_ERROR = "error.internal";

    private static final int BAD_CERTIFICATE_RECEIVED_CODE = 40001;
    private static final int CERTIFICATE_NOT_FOUND_CODE = 40401;
    private static final int DAO_EXCEPTION_CODE = 50001;

    public GiftCertificateController(GiftCertificateService giftCertificateService, MessageSource messageSource) {
        this.giftCertificateService = giftCertificateService;
        this.messageSource = messageSource;
    }

    /**
     * Returns all {@link GiftCertificate} objects from a database.
     *
     * @return {@link ResponseEntity} with a {@link HttpStatus} and a {@link List} of {@link GiftCertificate} objects.
     */
    @GetMapping(produces = JSON)
    public ResponseEntity<?> getAll() {
        List<GiftCertificate> giftCertificates = giftCertificateService.getAll();
        return new ResponseEntity<>(giftCertificates, HttpStatus.OK);
    }

    /**
     * Returns a {@link GiftCertificate} object from a database by its id or throws {@link ResourceNotFoundException} if nothing is retrieved from a database
     * or {@link DaoException} in the case of unexpected behaviour on a Dao level.
     *
     * @param id - the {@link GiftCertificate} object's id that is to be retrieved from a database.
     * @return {@link ResponseEntity} with a {@link HttpStatus} and a {@link GiftCertificate} object or a {@link ErrorInfo} object.
     */
    @GetMapping(value = ID_PATH, produces = JSON)
    public ResponseEntity<?> getById(@PathVariable(ID) long id) {
        GiftCertificate giftCertificate = giftCertificateService.getById(id);
        return new ResponseEntity<>(giftCertificate, HttpStatus.OK);
    }

    /**
     * Creates a {@link GiftCertificate} object in a database or throws {@link RequiredFieldsMissingException} if some fields
     * required for creation are missing or {@link IllegalArgumentException} if the parameter object's price or duration values are lower than 0.
     *
     * @param giftCertificate - the {@link GiftCertificate} object that is to be created in a database.
     * @return {@link ResponseEntity} with a {@link HttpStatus} alone or additionally with a {@link ErrorInfo} object.
     */
    @PostMapping(produces = JSON)
    public ResponseEntity<?> create(@RequestBody GiftCertificate giftCertificate) {
        giftCertificateService.create(giftCertificate);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Updates a {@link GiftCertificate} object in a database by its id or throws {@link ResourceNotFoundException} if the object
     * with such id doesn't exist or {@link IllegalArgumentException} if the parameter object's price or duration values are lower than 0.
     *
     * @param id              - the {@link GiftCertificate} object's id that is to be updated in a database.
     * @param giftCertificate - the {@link GiftCertificate} object which has the new values for update in a database.
     * @return {@link ResponseEntity} with a {@link HttpStatus} alone or additionally with a {@link ErrorInfo} object.
     */
    @PatchMapping(value = ID_PATH, produces = JSON)
    public ResponseEntity<?> updateById(@PathVariable long id, @RequestBody GiftCertificate giftCertificate) {
        giftCertificateService.updateById(id, giftCertificate);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Deletes a {@link GiftCertificate} object in a database by its id or throws {@link ResourceNotFoundException} if the object
     * with such id doesn't exist.
     *
     * @param id - the {@link GiftCertificate} object's id that is to be deleted in a database.
     * @return {@link ResponseEntity} with a {@link HttpStatus} alone or additionally with a {@link ErrorInfo} object.
     */
    @DeleteMapping(value = ID_PATH, produces = JSON)
    public ResponseEntity<?> deleteById(@PathVariable long id) {
        giftCertificateService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Returns all {@link GiftCertificate} objects from a database that are found by part of one of their fields or
     * throws {@link IllegalArgumentException} if the fieldName doesn't match any of the fields in a database.
     *
     * @param fieldName   - the name of the field which will be used for searching {@link GiftCertificate} objects in a database.
     * @param partOfField - the value that will be used for searching
     * @return {@link ResponseEntity} with a {@link HttpStatus} and a {@link List} of {@link GiftCertificate} objects or a {@link ErrorInfo} object.
     */
    @GetMapping(params = {FIELD_NAME, PART_OF_FIELD}, produces = JSON)
    public ResponseEntity<?> getByPartOfField(@RequestParam String fieldName, @RequestParam String partOfField) {
        List<GiftCertificate> giftCertificates = giftCertificateService.getByPartOfField(fieldName, partOfField);
        return new ResponseEntity<>(giftCertificates, HttpStatus.OK);
    }

    /**
     * Returns all {@link GiftCertificate} objects from a database by the name of the tag that is associated with them or
     * throws {@link ResourceNotFoundException} if a tag with this name doesn't exist
     * or {@link DaoException} in the case of unexpected behaviour on a Dao-level.
     *
     * @param tagName - the name of the tag which will be used for searching {@link GiftCertificate} objects in a database.
     * @return {@link ResponseEntity} with a {@link HttpStatus} and a {@link List} of {@link GiftCertificate} objects or a {@link ErrorInfo} object.
     */
    @GetMapping(params = TAG_NAME, produces = JSON)
    public ResponseEntity<?> getByTagName(@RequestParam String tagName) {
        List<GiftCertificate> giftCertificates = giftCertificateService.getByTagName(tagName);
        return new ResponseEntity<>(giftCertificates, HttpStatus.OK);
    }

    /**
     * Returns all {@link GiftCertificate} objects sorted by a given field in a given order
     * or throws {@link IllegalArgumentException} if the fieldName doesn't match any of the fields in a database.
     *
     * @param fieldName - the name of the tag which will be used for searching {@link GiftCertificate} objects in a database.
     * @param isAsc     - the sorting order. The order will be ascending if this parameter equals true and descending if it equals false.
     * @return {@link ResponseEntity} with a {@link HttpStatus} and a {@link List} of {@link GiftCertificate} objects or a {@link ErrorInfo} object.
     */
    @GetMapping(params = {FIELD_NAME, IS_ASC}, produces = JSON)
    public ResponseEntity<?> sortByFieldInGivenOrder(@RequestParam String fieldName, @RequestParam boolean isAsc) {
        List<GiftCertificate> giftCertificates = giftCertificateService.sortByFieldInGivenOrder(fieldName, isAsc);
        return new ResponseEntity<>(giftCertificates, HttpStatus.OK);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorInfo> handleIllegalArgumentException(Locale locale) {
        return ControllerUtils.createResponseEntityWithSpecifiedErrorInfo(
                messageSource.getMessage(ILLEGAL_ARGUMENT, null, locale),
                BAD_CERTIFICATE_RECEIVED_CODE,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RequiredFieldsMissingException.class)
    public ResponseEntity<ErrorInfo> handleRequiredFieldsMissingException(Locale locale) {
        return ControllerUtils.createResponseEntityWithSpecifiedErrorInfo(
                messageSource.getMessage(REQUIRED_FIELDS_MISSING, null, locale),
                BAD_CERTIFICATE_RECEIVED_CODE,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorInfo> handleResourceNotFoundException(Locale locale) {
        return ControllerUtils.createResponseEntityWithSpecifiedErrorInfo(
                messageSource.getMessage(RESOURCE_NOT_FOUND, null, locale),
                CERTIFICATE_NOT_FOUND_CODE,
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DaoException.class)
    public ResponseEntity<ErrorInfo> handleDaoException(Locale locale) {
        return ControllerUtils.createResponseEntityWithSpecifiedErrorInfo(
                messageSource.getMessage(INTERNAL_ERROR, null, locale),
                DAO_EXCEPTION_CODE,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
