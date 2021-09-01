package com.epam.esm.service;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceAlreadyExistsException;
import com.epam.esm.exception.RequiredFieldsMissingException;
import com.epam.esm.exception.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TagService {

    private final TagDao tagDao;

    private static final String RESOURCE_NOT_FOUND = "The required resource wasn't found.";
    private static final String REQUIRED_FIELD_MISSING = "Unfortunately, some required fields were missing.";
    private static final String TAG_WITH_NAME_ALREADY_EXISTS = "Unfortunately, a tag with this name already exists.";

    public TagService(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    public List<Tag> getAll() {
        List<Tag> tags = tagDao.getAll();
        if (tags.isEmpty()) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND);
        }

        return tags;
    }

    public Tag getById(long id) {
        Optional<Tag> optionalTag = tagDao.getById(id);
        if (optionalTag.isEmpty()) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND);
        }

        return optionalTag.get();
    }

    public Tag getByName(String name) {
        Optional<Tag> optionalTag = tagDao.getByName(name);
        if (optionalTag.isEmpty()) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND);
        }

        return optionalTag.get();
    }

    public void create(Tag tag) {
        if (tag.getName() == null) {
            throw new RequiredFieldsMissingException(REQUIRED_FIELD_MISSING);
        }

        Optional<Tag> optionalTag = tagDao.getByName(tag.getName());
        if (optionalTag.isPresent()) {
            throw new ResourceAlreadyExistsException(TAG_WITH_NAME_ALREADY_EXISTS);
        }

        tagDao.create(tag);
    }

    public List<Long> createTagsIfNotPresent(List<Tag> tags) {
        List<Long> ids = new ArrayList<>();
        for (Tag tag : tags) {
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

    public List<Tag> getTagsByListOfIds(List<Long> tagIds) {
        List<Tag> tags = new ArrayList<>();

        for (Long tagId : tagIds) {
            Optional<Tag> optionalTag = tagDao.getById(tagId);
            optionalTag.ifPresent(tags::add);
        }

        return tags;
    }

    public void deleteById(long id) {
        Optional<Tag> optionalTag = tagDao.getById(id);
        if (optionalTag.isEmpty()) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND);
        }

        tagDao.deleteById(id);
    }
}
