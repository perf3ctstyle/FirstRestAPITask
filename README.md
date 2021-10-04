# FirstRestAPITask

The system should expose REST APIs to perform the following operations:
1. CRUD operations for GiftCertificate. If new tags are passed during creation/modification – they should be created in the DB. For update operation - update only fields, that pass in request, others should not be updated.
2. CRD operations for Tag.
3. Get certificates with tags:
3.1. by tag name (ONE tag)
3.2. search by part of name/description
3.3. sort by date or by name ASC/DESC).
