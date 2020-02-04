package org.digital.archive.services;


import org.digital.archive.entities.Professor;
import org.springframework.data.domain.Page;

import java.util.Collection;

public interface ProfessorService {

    public Professor saveProfessor(Professor professor);

    public Professor getProfessor(Long id);

    public Professor getProfessor(String email);

    public boolean deleteProfessor(Long id);

    public Collection<Professor> getProfessors();

    public Page<Professor> getProfessors(int page, int size);

    public Page<Professor> getProfessors(String search, int page, int size);

}
