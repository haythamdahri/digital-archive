package org.digital.archive.services.impl;

import org.digital.archive.entities.Professor;
import org.digital.archive.repositories.ProfessorRepository;
import org.digital.archive.services.ProfessorService;
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
public class ProfessorServiceImpl implements ProfessorService {

    private ProfessorRepository professorRepository;

    @Autowired
    public void setProfessorRepository(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    @Override
    public Professor saveProfessor(Professor professor) {
        return this.professorRepository.save(professor);
    }

    @Override
    public Professor getProfessor(Long id) {
        Optional<Professor> professorOptional = this.professorRepository.findById(id);
        return professorOptional.orElse(null);
    }

    @Override
    public Professor getProfessor(String email) {
        return this.professorRepository.findByEmail(email);
    }

    @Override
    public boolean deleteProfessor(Long id) {
        this.professorRepository.delete(this.getProfessor(id));
        return true;
    }

    @Override
    public Collection<Professor> getProfessors() {
        Collection<Professor> professors = new ArrayList<>();
        Iterable<Professor> professorIterable = this.professorRepository.findAll();
        professorIterable.forEach(professors::add);
        return professors;
    }

    @Override
    public Page<Professor> getProfessors(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        return this.professorRepository.findAll(pageRequest);
    }

    @Override
    public Page<Professor> getProfessors(String search, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        return this.professorRepository.findByUsernameContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search, search, search, pageRequest);
    }

}
