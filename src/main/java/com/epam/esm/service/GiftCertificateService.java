package com.epam.esm.service;

import com.epam.esm.dao.GiftAndTagDao;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.util.DateTimeUtils;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

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

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    private static final String RESOURCE_NOT_FOUND = "The required resource wasn't found.";

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

    public GiftCertificate getById(long id) {
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateDao.getById(id);
        if (optionalGiftCertificate.isEmpty()) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND);
        }

        GiftCertificate giftCertificate = optionalGiftCertificate.get();
        setGiftCertificateTags(giftCertificate);

        return giftCertificate;
    }

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

    @Transactional
    public void updateById(long id, GiftCertificate giftCertificate) {
        GiftCertificate oldGiftCertificate = getById(id);
        if (oldGiftCertificate == null) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND);
        }

        giftCertificateValidator.validateForUpdate(giftCertificate);

        LocalDateTime lastUpdateDate = DateTimeUtils.nowOfPattern(DATE_TIME_PATTERN);
        giftCertificate.setLastUpdateDate(lastUpdateDate);

        giftCertificateDao.update(id, giftCertificate);

        List<Tag> tags = giftCertificate.getTags();
        if (tags != null && !(tags.isEmpty())) {
            updateGiftsAndTags(id, tags);
        }
    }

    public void deleteById(long id) {
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateDao.getById(id);
        if (optionalGiftCertificate.isEmpty()) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND);
        }

        giftCertificateDao.deleteById(id);
    }

    public List<GiftCertificate> getByPartOfField(String fieldName, String partOfField) {
        List<GiftCertificate> giftCertificates = giftCertificateDao.getByPartOfField(fieldName, partOfField);
        if (giftCertificates.isEmpty()) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND);
        }

        setGiftCertificateTags(giftCertificates);

        return giftCertificates;
    }

    public List<GiftCertificate> getByTagName(String tagName) {
        Tag tag = tagService.getByName(tagName);
        if (tag == null) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND);
        }

        Long tagId = tag.getId();
        List<Long> certificateIds = giftAndTagDao.getCertificateIdsByTagId(tagId);

        List<GiftCertificate> giftCertificates = new ArrayList<>();
        for (Long certificateId : certificateIds) {
            Optional<GiftCertificate> optionalGiftCertificate = giftCertificateDao.getById(certificateId);
            optionalGiftCertificate.ifPresent(giftCertificates::add);
        }

        if (giftCertificates.isEmpty()) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND);
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
        List<Long> tagIds = tagService.createTagsIfNotPresent(tags);
        for (Long tagId : tagIds) {
            List<Long> linkedGiftCertificateIds = giftAndTagDao.getCertificateIdsByTagId(tagId);

            if (!linkedGiftCertificateIds.contains(giftCertificateId)) {
                giftAndTagDao.create(giftCertificateId, tagId);
            }
        }
    }
}
