package org.digital.archive.services.impl;

import org.digital.archive.entities.Archive;
import org.digital.archive.repositories.ArchiveRepository;
import org.digital.archive.services.ArchiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * @author Haytham DAHRI
 */
@Service
public class ArchiveServiceImpl implements ArchiveService {

    private ArchiveRepository archiveRepository;

    @Autowired
    public void setArchiveRepository(ArchiveRepository archiveRepository) {
        this.archiveRepository = archiveRepository;
    }

    @Override
    public Archive saveArchive(Archive archive) {
        return this.archiveRepository.save(archive);
    }

    @Override
    public Archive getArchive(Long id) {
        Optional<Archive> archiveOptional = this.archiveRepository.findById(id);
        return archiveOptional.orElse(null);
    }

    @Override
    public Page<Archive> getArchives(String title, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("publishDate").descending());
        return this.archiveRepository.findByTitleContainingIgnoreCase(title, pageRequest);
    }

    @Override
    public Page<Archive> getArchives(Long publisherId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("publishDate").descending());
        return this.archiveRepository.findByPublisherId(publisherId, pageRequest);
    }

    @Override
    public Page<Archive> getArchives(Long publisherId, String title, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("publishDate").descending());
        return this.archiveRepository.findByPublisherIdAndTitleContainingIgnoreCase(publisherId, title, pageRequest);
    }

    @Override
    public boolean deleteArchive(Long id) {
        this.archiveRepository.deleteById(id);
        return true;
    }

    @Override
    public Page<Archive> getArchives(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("publishDate").descending());
        return this.archiveRepository.findAll(pageRequest);
    }

    @Override
    public Page<Archive> getTrendingArchives(int size) {
        PageRequest pageRequest = PageRequest.of(0, size, Sort.by("views").descending());
        return this.archiveRepository.findAll(pageRequest);
    }

    @Override
    public Collection<Archive> getArchives() {
        Collection<Archive> archives = new ArrayList<>();
        Iterable<Archive> archiveIterable = this.archiveRepository.findAll();
        archiveIterable.forEach(archives::add);
        return archives;
    }
}
