package com.epam.esm.service;

import com.epam.esm.dao.GiftAndTagDao;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.validator.GiftCertificateValidator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;

public class GiftCertificateServiceTest {

    private final GiftCertificateDao giftCertificateDao = Mockito.mock(GiftCertificateDao.class);
    private final TagService tagService = Mockito.mock(TagService.class);
    private final GiftAndTagDao giftAndTagDao = Mockito.mock(GiftAndTagDao.class);
    private final GiftCertificateValidator giftCertificateValidator = Mockito.mock(GiftCertificateValidator.class);
    private final GiftCertificateService giftCertificateService = new GiftCertificateService(giftCertificateDao, tagService, giftAndTagDao, giftCertificateValidator);

    @Test
    public void testShouldReturnListOfGiftCertificatesInGetAll() {
        List<GiftCertificate> expected = Arrays.asList(new GiftCertificate(), new GiftCertificate());
        Mockito.when(giftCertificateDao.getAll()).thenReturn(expected);
        Mockito.when(giftAndTagDao.getTagIdsByCertificateId(null)).thenReturn(null);
        Mockito.when(tagService.getTagsByListOfIds(null)).thenReturn(null);

        List<GiftCertificate> actual = giftCertificateService.getAll();

        assertEquals(expected, actual);
    }

    @Test
    public void testShouldReturnGiftCertificateInGetById() {
        long id = 0;
        GiftCertificate expected = new GiftCertificate();
        Mockito.when(giftCertificateDao.getById(id)).thenReturn(Optional.of(expected));
        Mockito.when(giftAndTagDao.getTagIdsByCertificateId(null)).thenReturn(null);
        Mockito.when(tagService.getTagsByListOfIds(null)).thenReturn(null);

        GiftCertificate actual = giftCertificateService.getById(id);

        assertEquals(expected, actual);
    }

    @Test
    public void testShouldThrowExceptionWhenResourceIsNotFoundById() {
        assertThrows(ResourceNotFoundException.class, () -> {
            long id = 0;
            Mockito.when(giftCertificateDao.getById(id)).thenReturn(Optional.empty());

            giftCertificateService.getById(id);
        });
    }

    @Test
    public void testShouldWorkCorrectlyInCreate() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setPrice(100);
        giftCertificate.setDuration(100L);
        doNothing().when(giftCertificateValidator).validateForCreation(giftCertificate);
        List<Long> tagIds = new ArrayList<>();
        Mockito.when(tagService.createTagsIfNotCreated(null)).thenReturn(tagIds);
        Long certificateId = 0L;
        Mockito.when(giftCertificateDao.create(giftCertificate)).thenReturn(certificateId);

        giftCertificateService.create(giftCertificate);
    }

    @Test
    public void testShouldWorkCorrectlyInDeleteById() {
        long id = 0;
        Mockito.when(giftCertificateDao.getById(id)).thenReturn(Optional.of(new GiftCertificate()));
        doNothing().when(giftCertificateDao).deleteById(id);

        giftCertificateService.deleteById(id);
    }

    @Test
    public void testShouldThrowExceptionWhenResourceIsNotFoundBeforeDeleting() {
        assertThrows(ResourceNotFoundException.class, () -> {
            long id = 0;
            Mockito.when(giftCertificateDao.getById(0)).thenReturn(Optional.empty());

            giftCertificateService.deleteById(id);
        });
    }

    @Test
    public void testShouldReturnListOfGiftCertificatesInGetByPartOfField() {
        List<GiftCertificate> expected = Arrays.asList(new GiftCertificate(), new GiftCertificate());
        Mockito.when(giftCertificateDao.getByPartOfField(null, null)).thenReturn(expected);
        Mockito.when(giftAndTagDao.getTagIdsByCertificateId(null)).thenReturn(null);
        Mockito.when(tagService.getTagsByListOfIds(null)).thenReturn(null);

        List<GiftCertificate> actual = giftCertificateService.getByPartOfField(null, null);

        assertEquals(expected, actual);
    }

    @Test
    public void testShouldReturnListOfGiftCertificatesInGetByTagName() {
        Tag tag = new Tag();
        Mockito.when(tagService.getByName(null)).thenReturn(tag);
        Long certificateId = 1L;
        List<Long> certificateIds = List.of(certificateId);
        Mockito.when(giftAndTagDao.getCertificateIdsByTagId(null)).thenReturn(certificateIds);
        GiftCertificate giftCertificate = new GiftCertificate();
        List<GiftCertificate> expected = List.of(giftCertificate);
        Mockito.when(giftCertificateDao.getById(certificateId)).thenReturn(Optional.of(giftCertificate));

        List<GiftCertificate> actual = giftCertificateService.getByTagName(null);

        assertEquals(expected, actual);
    }

    @Test
    public void testShouldReturnListOfGiftCertificatesInSortByFieldInGivenOrder() {
        List<GiftCertificate> expected = Arrays.asList(new GiftCertificate(), new GiftCertificate());
        Mockito.when(giftCertificateDao.sortByFieldInGivenOrder(null, false)).thenReturn(expected);
        Mockito.when(giftAndTagDao.getTagIdsByCertificateId(null)).thenReturn(null);
        Mockito.when(tagService.getTagsByListOfIds(null)).thenReturn(null);

        List<GiftCertificate> actual = giftCertificateService.sortByFieldInGivenOrder(null, false);

        assertEquals(expected, actual);
    }
}
