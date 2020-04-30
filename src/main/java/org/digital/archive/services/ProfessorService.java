package org.digital.archive.services;

import org.digital.archive.entities.Professor;
import org.springframework.data.domain.Page;

import java.util.Collection;

/**
 * @author Haytham DAHRI
 */
public interface ProfessorService {

    Professor saveProfessor(Professor professor);

    Professor getProfessor(Long id);

    Professor getProfessor(String email);

    boolean deleteProfessor(Long id);

    Collection<Professor> getProfessors();

    Page<Professor> getProfessors(int page, int size);

    Page<Professor> getProfessors(String search, int page, int size);

}
