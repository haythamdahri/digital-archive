package org.digital.archive.services;

import org.digital.archive.entities.Archive;
import org.springframework.data.domain.Page;

import java.util.Collection;

/**
 * @author Haytham DAHRI
 */
public interface ArchiveService {

    Archive saveArchive(Archive archive);

    Archive getArchive(Long id);

    Page<Archive> getArchives(String title, int page, int size);

    Page<Archive> getArchives(Long publisherId, int page, int size);

    Page<Archive> getArchives(Long publisherId, String title, int page, int size);

    boolean deleteArchive(Long id);

    Page<Archive> getArchives(int page, int size);

    Page<Archive> getTrendingArchives(int size);

    Collection<Archive> getArchives();

}
