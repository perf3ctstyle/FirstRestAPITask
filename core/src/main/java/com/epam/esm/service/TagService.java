package com.epam.esm.service;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.ErrorInfo;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.ResourceAlreadyExistsException;
import com.epam.esm.exception.RequiredFieldsMissingException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.validator.TagValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This is a class that encapsulates the {@link Tag} business logic and also acts as a transaction boundary.
 *
 * @author Nikita Torop
 */
@Service
public class TagService {

    private final TagDao tagDao;
    private final TagValidator tagValidator;

    private static final String RESOURCE_NOT_FOUND = "The required resource wasn't found.";
    private static final String TAG_WITH_NAME_ALREADY_EXISTS = "Unfortunately, a tag with this name already exists.";

    public TagService(TagDao tagDao, TagValidator tagValidator) {
        this.tagDao = tagDao;
        this.tagValidator = tagValidator;
    }

    /**
     * Returns all {@link Tag} objects from a database.
     *
     * @return {@link List} of {@link Tag} objects.
     */
    public List<Tag> getAll() {
        return tagDao.getAll();
    }

    /**
     * Returns a {@link Tag} object from a database by its id or throws {@link ResourceNotFoundException} if nothing is retrieved from a database
     * or {@link DaoException} in the case of unexpected behaviour on a Dao level.
     *
     * @param id - the {@link Tag} object's id that is to be retrieved from a database.
     * @return {@link Tag} object.
     */
    public Tag getById(long id) {
        Optional<Tag> optionalTag = tagDao.getById(id);

        if (optionalTag.isEmpty()) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND);
        }

        return optionalTag.get();
    }

    /**
     * Returns a {@link Tag} object from a database by its name or throws {@link ResourceNotFoundException} if nothing is retrieved from a database
     * or {@link DaoException} in the case of unexpected behaviour on a Dao level.
     *
     * @param name - the {@link Tag} object's name that is to be retrieved from a database.
     * @return {@link Tag} object.
     */
    public Tag getByName(String name) {
        Optional<Tag> optionalTag = tagDao.getByName(name);

        if (optionalTag.isEmpty()) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND);
        }

        return optionalTag.get();
    }

    /**
     * Creates a {@link Tag} object in a database or throws {@link RequiredFieldsMissingException} if some fields
     * required for creation are missing or {@link ResourceAlreadyExistsException} if the tag with the same name already exists.
     *
     * @param tag - the {@link Tag} object that is to be created in a database.
     */
    public void create(Tag tag) {
        tagValidator.validateForCreation(tag);

        Optional<Tag> optionalTag = tagDao.getByName(tag.getName());

        if (optionalTag.isPresent()) {
            throw new ResourceAlreadyExistsException(TAG_WITH_NAME_ALREADY_EXISTS);
        }

        tagDao.create(tag);
    }

    /**
     * Creates {@link Tag} objects in a database or throws {@link RequiredFieldsMissingException} if some fields
     * required for creation are missing.
     *
     * @param tags - {@link Tag} objects that are to be created in a database if they have not been created already.
     * @return {@link List} of {@link Tag} ids in the same order as the one in the method parameter.
     */
    public List<Long> createTagsIfNotCreated(List<Tag> tags) {
        List<Long> ids = new ArrayList<>();

        for (Tag tag : tags) {
            tagValidator.validateForCreation(tag);

            String tagName = tag.getName();
            Optional<Tag> optionalTag = tagDao.getByName(tagName);

            if (optionalTag.isEmpty()) {
                Tag tagToCreate = new Tag(null, tagName);

                Long id = tagDao.create(tagToCreate);
                ids.add(id);
            } else {
                Tag tagFromDatabase = optionalTag.get();

                Long id = tagFromDatabase.getId();
                ids.add(id);
            }
        }

        return ids;
    }

    /**
     * Returns {@link Tag} objects from a database by their id or {@link DaoException} in the case of unexpected behaviour
     * on a Dao level.
     *
     * @param tagIds - the {@link Tag} objects ids that are to be retrieved from a database.
     * @return {@link List} of {@link Tag} objects.
     */
    public List<Tag> getTagsByListOfIds(List<Long> tagIds) {
        List<Tag> tags = new ArrayList<>();

        tagIds.forEach(tagId -> {
            Optional<Tag> optionalTag = tagDao.getById(tagId);
            optionalTag.ifPresent(tags::add);
        });

        return tags;
    }

    /**
     * Deletes a {@link Tag} object in a database by its id or throws {@link ResourceNotFoundException} if the object
     * with such id doesn't exist.
     *
     * @param id - the {@link Tag} object's id that is to be deleted in a database.
     */
    public void deleteById(long id) {
        Optional<Tag> optionalTag = tagDao.getById(id);

        if (optionalTag.isEmpty()) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND);
        }

        tagDao.deleteById(id);
    }
}
