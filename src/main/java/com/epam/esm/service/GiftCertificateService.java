package com.epam.esm.service;

import com.epam.esm.dao.GiftAndTagDao;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.utils.DateTimeUtils;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

import com.epam.esm.exception.RequiredFieldsMissingException;
import com.epam.esm.validator.GiftCertificateValidator;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GiftCertificateService {

    private final GiftCertificateDao giftCertificateDao;
    private final TagService tagService;
    private final GiftAndTagDao giftAndTagDao;
    private final GiftCertificateValidator giftCertificateValidator;

    private static final String VALUE_FOR_SEARCH = " Value used for searching = ";
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    private static final String RESOURCE_NOT_FOUND = "The required resource wasn't found.";
    private static final String REQUIRED_FIELDS_MISSING = "Some of the required fields were missing.";
    private static final String NEGATIVE_VALUE_PROHIBITED = "Negative value was found in a field that is supposed to be positive.";

    public GiftCertificateService(GiftCertificateDao giftCertificateDao, TagService tagService, GiftAndTagDao giftAndTagDao, GiftCertificateValidator giftCertificateValidator) {
        this.giftCertificateDao = giftCertificateDao;
        this.tagService = tagService;
        this.giftAndTagDao = giftAndTagDao;
        this.giftCertificateValidator = giftCertificateValidator;
    }

    public List<GiftCertificate> getAll() {
        List<GiftCertificate> giftCertificates = giftCertificateDao.getAll();
        if (giftCertificates.isEmpty()) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND);
        }

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

    public GiftCertificate getById(long id) {
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateDao.getById(id);
        if (optionalGiftCertificate.isEmpty()) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND + VALUE_FOR_SEARCH + id);
        }

        GiftCertificate giftCertificate = optionalGiftCertificate.get();
        setGiftCertificateTags(giftCertificate);

        return giftCertificate;
    }

    @Transactional
    public void create(GiftCertificate giftCertificate) throws RequiredFieldsMissingException {
        if (!giftCertificateValidator.areAllFieldsFilledForCreation(giftCertificate)) {
            throw new RequiredFieldsMissingException(REQUIRED_FIELDS_MISSING);
        }

        if (giftCertificate.getPrice() < 0 || giftCertificate.getDuration() < 0) {
            throw new IllegalArgumentException(NEGATIVE_VALUE_PROHIBITED);
        }

        List<Tag> tags = giftCertificate.getTags();
        List<Long> createdTagsIds = tagService.createTagsIfNotPresent(tags);

        LocalDateTime currentDateTime = DateTimeUtils.nowOfPattern(DATE_TIME_PATTERN);
        giftCertificate.setCreateDate(currentDateTime);
        giftCertificate.setLastUpdateDate(currentDateTime);

        Long createdCertificateId = giftCertificateDao.create(giftCertificate);

        for (Long tagId : createdTagsIds) {
            giftAndTagDao.create(createdCertificateId, tagId);
        }
    }

    @Transactional
    public void updateById(long id, GiftCertificate newGiftCertificate) {
        GiftCertificate oldGiftCertificate = getById(id);
        if (oldGiftCertificate == null) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND + VALUE_FOR_SEARCH + id);
        }

        List<Tag> tags = newGiftCertificate.getTags();
        List<Long> createdTagsIds = tagService.createTagsIfNotPresent(tags);

        String nameToSave = returnPresentName(oldGiftCertificate, newGiftCertificate);
        String descriptionToSave = returnPresentDescription(oldGiftCertificate, newGiftCertificate);
        Integer priceToSave = returnPresentPrice(oldGiftCertificate, newGiftCertificate);
        Long durationToSave = returnPresentDuration(oldGiftCertificate, newGiftCertificate);

        if (priceToSave < 0 || durationToSave < 0) {
            throw new IllegalArgumentException(NEGATIVE_VALUE_PROHIBITED);
        }

        LocalDateTime createDateToSave = oldGiftCertificate.getCreateDate();
        LocalDateTime lastUpdateDateToSave = DateTimeUtils.nowOfPattern(DATE_TIME_PATTERN);

        GiftCertificate giftCertificateToSave = new GiftCertificate(id, nameToSave, descriptionToSave, priceToSave, durationToSave, createDateToSave, lastUpdateDateToSave);
        giftCertificateDao.update(giftCertificateToSave);

        for (Long tagId : createdTagsIds) {
            giftAndTagDao.create(id, tagId);
        }
    }

    private String returnPresentName(GiftCertificate oldGiftCertificate, GiftCertificate newGiftCertificate) {
        if (newGiftCertificate.getName() == null) {
            return oldGiftCertificate.getName();
        } else {
            return newGiftCertificate.getName();
        }
    }

    private String returnPresentDescription(GiftCertificate oldGiftCertificate, GiftCertificate newGiftCertificate) {
        if (newGiftCertificate.getDescription() == null) {
            return oldGiftCertificate.getDescription();
        } else {
            return newGiftCertificate.getDescription();
        }
    }

    private Integer returnPresentPrice(GiftCertificate oldGiftCertificate, GiftCertificate newGiftCertificate) {
        if (newGiftCertificate.getPrice() == null) {
            return oldGiftCertificate.getPrice();
        } else {
            return newGiftCertificate.getPrice();
        }
    }

    private Long returnPresentDuration(GiftCertificate oldGiftCertificate, GiftCertificate newGiftCertificate) {
        if (newGiftCertificate.getDuration() == null) {
            return oldGiftCertificate.getDuration();
        } else {
            return newGiftCertificate.getDuration();
        }
    }

    public void deleteById(long id) {
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateDao.getById(id);
        if (optionalGiftCertificate.isEmpty()) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND + VALUE_FOR_SEARCH + id);
        }

        giftCertificateDao.deleteById(id);
    }

    public List<GiftCertificate> getByPartOfField(String fieldName, String partOfField) {
        List<GiftCertificate> giftCertificates = giftCertificateDao.getByPartOfField(fieldName, partOfField);
        setGiftCertificateTags(giftCertificates);

        return giftCertificates;
    }

    public List<GiftCertificate> getByTagName(String tagName) {
        Tag tag = tagService.getByName(tagName);
        if (tag == null) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND + VALUE_FOR_SEARCH + tagName);
        }

        Long tagId = tag.getId();
        List<Long> certificateIds = giftAndTagDao.getCertificateIdsByTagId(tagId);

        List<GiftCertificate> giftCertificates = new ArrayList<>();
        for (Long certificateId : certificateIds) {
            GiftCertificate giftCertificate = getById(certificateId);
            giftCertificates.add(giftCertificate);
        }

        return giftCertificates;
    }

    public List<GiftCertificate> sortByFieldInGivenOrder(String fieldName, boolean isAsc) {
        List<GiftCertificate> giftCertificates = giftCertificateDao.sortByFieldInGivenOrder(fieldName, isAsc);
        if (giftCertificates.isEmpty()) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND);
        }

        setGiftCertificateTags(giftCertificates);

        return giftCertificates;
    }
}
