package com.epam.esm.service;

import com.epam.esm.constant.GiftCertificateConstants;
import com.epam.esm.dao.GiftAndTagDao;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.RequiredFieldsMissingException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.util.DateTimeUtils;
import com.epam.esm.validator.GiftCertificateValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This is a class that encapsulates the {@link GiftCertificate} business logic and also acts as a transaction boundary.
 *
 * @author Nikita Torop
 */
@Service
public class GiftCertificateService {

    private final GiftCertificateDao giftCertificateDao;
    private final TagService tagService;
    private final GiftAndTagDao giftAndTagDao;
    private final GiftCertificateValidator giftCertificateValidator;

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    private static final String RESOURCE_NOT_FOUND = "The required resource wasn't found.";

    public GiftCertificateService(GiftCertificateDao giftCertificateDao,
                                  TagService tagService,
                                  GiftAndTagDao giftAndTagDao,
                                  GiftCertificateValidator giftCertificateValidator) {
        this.giftCertificateDao = giftCertificateDao;
        this.tagService = tagService;
        this.giftAndTagDao = giftAndTagDao;
        this.giftCertificateValidator = giftCertificateValidator;
    }

    /**
     * Returns all {@link GiftCertificate} objects from a database.
     *
     * @return {@link List} of {@link GiftCertificate} objects.
     */
    public List<GiftCertificate> getAll() {
        List<GiftCertificate> giftCertificates = giftCertificateDao.getAll();
        setGiftCertificateTags(giftCertificates);

        return giftCertificates;
    }

    /**
     * Returns a {@link GiftCertificate} object from a database by its id or throws {@link ResourceNotFoundException} if nothing is retrieved from a database
     * or {@link DaoException} in the case of unexpected behaviour on a Dao level.
     *
     * @param id - the {@link GiftCertificate} object's id that is to be retrieved from a database.
     * @return {@link GiftCertificate} object.
     */
    public GiftCertificate getById(long id) {
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateDao.getById(id);

        if (optionalGiftCertificate.isEmpty()) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND);
        }

        GiftCertificate giftCertificate = optionalGiftCertificate.get();
        setGiftCertificateTags(giftCertificate);

        return giftCertificate;
    }

    /**
     * Creates a {@link GiftCertificate} object in a database or throws {@link RequiredFieldsMissingException} if some fields
     * required for creation are missing or {@link IllegalArgumentException} if the parameter object's price or duration values are lower than 0.
     *
     * @param giftCertificate - the {@link GiftCertificate} object that is to be created in a database.
     */
    @Transactional
    public void create(GiftCertificate giftCertificate) {
        giftCertificateValidator.validateForCreation(giftCertificate);

        LocalDateTime currentDateTime = DateTimeUtils.nowOfPattern(DATE_TIME_PATTERN);
        giftCertificate.setCreateDate(currentDateTime);
        giftCertificate.setLastUpdateDate(currentDateTime);

        Long giftCertificateId = giftCertificateDao.create(giftCertificate);

        List<Tag> tags = giftCertificate.getTags();

        if (tags != null && !(tags.isEmpty())) {
            updateGiftsAndTags(giftCertificateId, tags);
        }
    }

    /**
     * Updates a {@link GiftCertificate} object in a database by its id or throws {@link ResourceNotFoundException} if the object
     * with such id doesn't exist or {@link IllegalArgumentException} if the parameter object's price or duration values are lower than 0.
     *
     * @param id              - the {@link GiftCertificate} object's id that is to be updated in a database.
     * @param giftCertificate - the {@link GiftCertificate} object which has the new values for update in a database.
     */
    @Transactional
    public void updateById(long id, GiftCertificate giftCertificate) {
        Optional<GiftCertificate> oldGiftCertificate = giftCertificateDao.getById(id);

        if (oldGiftCertificate.isEmpty()) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND);
        }

        giftCertificateValidator.validateForUpdate(giftCertificate);

        LocalDateTime lastUpdateDate = DateTimeUtils.nowOfPattern(DATE_TIME_PATTERN);
        giftCertificate.setLastUpdateDate(lastUpdateDate);

        Map<String, String> fieldNameValueForUpdate = toMap(giftCertificate);
        giftCertificateDao.update(id, fieldNameValueForUpdate);

        List<Tag> tags = giftCertificate.getTags();

        if (tags != null && !(tags.isEmpty())) {
            updateGiftsAndTags(id, tags);
        }
    }

    /**
     * Deletes a {@link GiftCertificate} object in a database by its id or throws {@link ResourceNotFoundException} if the object
     * with such id doesn't exist.
     *
     * @param id - the {@link GiftCertificate} object's id that is to be deleted in a database.
     */
    public void deleteById(long id) {
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateDao.getById(id);

        if (optionalGiftCertificate.isEmpty()) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND);
        }

        giftCertificateDao.deleteById(id);
    }

    /**
     * Returns all {@link GiftCertificate} objects from a database that are found by part of one of their fields or
     * throws {@link IllegalArgumentException} if the fieldName doesn't match any of the fields in a database.
     *
     * @param fieldName   - the name of the field which will be used for searching {@link GiftCertificate} objects in a database.
     * @param partOfField - the value that will be used for searching
     * @return {@link List} of {@link GiftCertificate} objects.
     */
    public List<GiftCertificate> getByPartOfField(String fieldName, String partOfField) {
        List<GiftCertificate> giftCertificates = giftCertificateDao.getByPartOfField(fieldName, partOfField);
        setGiftCertificateTags(giftCertificates);

        return giftCertificates;
    }

    /**
     * Returns all {@link GiftCertificate} objects from a database by the name of the tag that is associated with them or
     * throws {@link DaoException} in the case of unexpected behaviour on a Dao-level.
     *
     * @param tagName - the name of the tag which will be used for searching {@link GiftCertificate} objects in a database.
     * @return {@link List} of {@link GiftCertificate} objects.
     */
    public List<GiftCertificate> getByTagName(String tagName) {
        Tag tag = tagService.getByName(tagName);

        Long tagId = tag.getId();
        List<Long> certificateIds = giftAndTagDao.getCertificateIdsByTagId(tagId);

        List<GiftCertificate> giftCertificates = new ArrayList<>();

        certificateIds.forEach(certificateId -> {
            Optional<GiftCertificate> optionalGiftCertificate = giftCertificateDao.getById(certificateId);
            optionalGiftCertificate.ifPresent(giftCertificates::add);
        });

        return giftCertificates;
    }

    /**
     * Returns all {@link GiftCertificate} objects sorted by a given field in a given order
     * or throws {@link IllegalArgumentException} if the fieldName doesn't match any of the fields in a database.
     *
     * @param fieldName - the name of the tag which will be used for searching {@link GiftCertificate} objects in a database.
     * @param isAsc     - the sorting order. The order will be ascending if this parameter equals true and descending if it equals false.
     * @return {@link List} of {@link GiftCertificate} objects.
     */
    public List<GiftCertificate> sortByFieldInGivenOrder(String fieldName, boolean isAsc) {
        List<GiftCertificate> giftCertificates = giftCertificateDao.sortByFieldInGivenOrder(fieldName, isAsc);
        setGiftCertificateTags(giftCertificates);

        return giftCertificates;
    }


    private void setGiftCertificateTags(GiftCertificate giftCertificate) {
        List<Long> tagIds = giftAndTagDao.getTagIdsByCertificateId(giftCertificate.getId());
        List<Tag> tags = tagService.getTagsByListOfIds(tagIds);
        giftCertificate.setTags(tags);
    }

    private void setGiftCertificateTags(List<GiftCertificate> giftCertificates) {
        for (GiftCertificate giftCertificate : giftCertificates) {
            setGiftCertificateTags(giftCertificate);
        }
    }

    private void updateGiftsAndTags(Long giftCertificateId, List<Tag> tags) {
        List<Long> linkedTagIdsBeforeUpdate = giftAndTagDao.getTagIdsByCertificateId(giftCertificateId);
        List<Long> tagIdsAfterUpdate = tagService.createTagsIfNotCreated(tags);

        tagIdsAfterUpdate.stream()
                .filter(tagId -> !linkedTagIdsBeforeUpdate.contains(tagId))
                .forEach(tagId -> giftAndTagDao.create(giftCertificateId, tagId));

        linkedTagIdsBeforeUpdate.stream()
                .filter(linkedTagIdBeforeUpdate -> !tagIdsAfterUpdate.contains(linkedTagIdBeforeUpdate))
                .forEach(linkedTagIdBeforeUpdate -> giftAndTagDao.delete(giftCertificateId, linkedTagIdBeforeUpdate));
    }

    private Map<String, String> toMap(GiftCertificate giftCertificate) {
        Map<String, String> presentFields = new HashMap<>();

        String name = giftCertificate.getName();
        String description = giftCertificate.getDescription();
        Integer price = giftCertificate.getPrice();
        Long duration = giftCertificate.getDuration();
        LocalDateTime createDate = giftCertificate.getCreateDate();
        LocalDateTime lastUpdateDate = giftCertificate.getLastUpdateDate();

        presentFields.put(GiftCertificateConstants.NAME, name);
        presentFields.put(GiftCertificateConstants.DESCRIPTION, description);
        presentFields.put(GiftCertificateConstants.PRICE, price.toString());
        presentFields.put(GiftCertificateConstants.DURATION, duration.toString());
        presentFields.put(GiftCertificateConstants.CREATE_DATE, createDate.toString());
        presentFields.put(GiftCertificateConstants.LAST_UPDATE_DATE, lastUpdateDate.toString());

        return presentFields;
    }
}
