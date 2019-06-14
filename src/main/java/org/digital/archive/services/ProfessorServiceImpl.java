package org.digital.archive.services;

import org.digital.archive.entities.Professor;
import org.digital.archive.entities.Role;
import org.digital.archive.repositories.ProfessorRepository;
import org.digital.archive.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class ProfessorServiceImpl implements ProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;

    @Override
    public Professor saveProfessor(Professor professor) {
        return this.professorRepository.save(professor);
    }

    @Override
    public Professor getProfessor(Long id) {
        Optional<Professor> professorOptional = this.professorRepository.findById(id);
        if( professorOptional.isPresent() ) {
            return professorOptional.get();
        }
        return null;
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
        return this.professorRepository.findAll();
    }
}
