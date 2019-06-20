package org.digital.archive.services;


import org.digital.archive.entities.Professor;

import java.util.Collection;

public interface ProfessorService {

    public Professor saveProfessor(Professor professor);

    public Professor getProfessor(Long id);

    public Professor getProfessor(String email);

    public boolean deleteProfessor(Long id);

    public Collection<Professor> getProfessors();

}
