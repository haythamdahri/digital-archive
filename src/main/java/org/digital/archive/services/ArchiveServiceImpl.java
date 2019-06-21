package org.digital.archive.services;

import org.digital.archive.entities.Archive;
import org.digital.archive.repositories.ArchiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class ArchiveServiceImpl implements ArchiveService {

    @Autowired
    private ArchiveRepository archiveRepository;

    @Override
    public Archive saveArchive(Archive archive) {
        return this.archiveRepository.save(archive);
    }

    @Override
    public Archive getArchive(Long id) {
        Optional<Archive> archiveOptional = this.archiveRepository.findById(id);
        if (archiveOptional.isPresent()) {
            return archiveOptional.get();
        }
        return null;
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
    public boolean deleteArchive(Long id) {
        this.archiveRepository.delete(this.getArchive(id));
        return true;
    }

    @Override
    public Page<Archive> getArchives(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("publishDate").descending());
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
