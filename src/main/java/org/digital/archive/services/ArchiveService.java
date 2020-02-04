package org.digital.archive.services;


import org.digital.archive.entities.Archive;
import org.digital.archive.entities.Professor;
import org.springframework.data.domain.Page;

import java.util.Collection;

public interface ArchiveService {

    public Archive saveArchive(Archive archive);

    public Archive getArchive(Long id);

    public Page<Archive> getArchives(String title, int page, int size);

    public Page<Archive> getArchives(Long publisherId, int page, int size);

    public Page<Archive> getArchives(Long publisherId, String title, int page, int size);

    public boolean deleteArchive(Long id);

    public Page<Archive> getArchives(int page, int size);

    public Page<Archive> getTrendingArchives(int size);

    public Collection<Archive> getArchives();

}
