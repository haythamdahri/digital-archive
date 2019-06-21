package org.digital.archive.repositories;

import org.digital.archive.entities.Archive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(path = "/api/archives")
public interface ArchiveRepository extends PagingAndSortingRepository<Archive, Long> {

    public Page<Archive> findByTitleContainingIgnoreCase(@Param("title") String title, @PageableDefault Pageable pageable);

    public Page<Archive> findByPublisherId(@Param("id") Long publisherId, @PageableDefault Pageable pageable);


}
