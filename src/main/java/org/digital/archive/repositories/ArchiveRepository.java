package org.digital.archive.repositories;

import org.digital.archive.entities.Archive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;

/**
 * @author Haytham DAHRI
 */
@Repository
@RepositoryRestResource(path = "archives")
public interface ArchiveRepository extends PagingAndSortingRepository<Archive, Long> {

    Page<Archive> findByTitleContainingIgnoreCase(@Param("title") String title, @PageableDefault Pageable pageable);

    Page<Archive> findByPublisherIdAndTitleContainingIgnoreCase(@Param("id") Long publisherId, @Param("title") String title, @PageableDefault Pageable pageable);

    Page<Archive> findByPublisherId(@Param("id") Long publisherId, @PageableDefault Pageable pageable);


}
