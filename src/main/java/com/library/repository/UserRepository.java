package com.library.repository;

import com.library.entity.User;
import com.library.utils.projections.UserLoansView;
import com.library.utils.projections.UserView;
import com.library.utils.projections.UsersView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;


@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    @Query(value = """
            SELECT u.user_id,concat(u.first_name,' ', u.last_name) as fullname,
                   u.email,u.email_confirmed,u.phone,u.created_at,u.updated_at,
                   jsonb_agg(r.name) as roles
            FROM user_roles ur
                     INNER JOIN roles r on ur.role_id = r.id
                     INNER JOIN "user" u on u.user_id = ur.user_id
            WHERE u.email = :email
            GROUP BY u.user_id
            """, nativeQuery = true)
    UserView findUserByEmailWithRoles(@Param("email") String email);

    @Query(value = """
            SELECT u.user_id,concat(u.first_name,' ', u.last_name) as fullname,
                   u.email,u.email_confirmed,u.phone,u.created_at,u.updated_at,
                   jsonb_agg(DISTINCT r.name) as roles,
                   jsonb_agg(json_build_object(
                   'borrowDate',l.borrow_date,
                   'dueDate',l.due_date,
                   'returnDate',l.returned_date,
                   'loanExtended',l.loan_extended,
                   'loanStatus',l.loan_status,
                   'sourceTitle',b.source_title,
                   'bookLanguage',b.language,
                   'ISBN',b.isbn,
                   'releaseYear',b.release_year)) as loans
            FROM user_roles ur
                     INNER JOIN roles r on ur.role_id = r.id
                     INNER JOIN "user" u on u.user_id = ur.user_id
                     INNER JOIN loan l on u.user_id = l.user_id
                     INNER JOIN book b on l.book_id = b.book_id
            WHERE u.user_id = :userId
            GROUP BY u.user_id
            """, nativeQuery = true)
    Optional<UserLoansView> findUserByIdWithRolesAndLoans(@Param("userId") Long id);

    @Query(value = """
            SELECT u.user_id,u.email,u.phone,concat(u.first_name,' ',u.last_name) as fullname,
                   u.email_confirmed,u.updated_at,
                   cast(array_agg(r.name) as character varying[]) as roles
            FROM user_roles ur\s
            INNER JOIN roles r ON ur.role_id = r.id\s
            INNER JOIN "user" u ON u.user_id = ur.user_id
            GROUP BY u.user_id,u.created_at,concat(u.first_name,' ',u.last_name)
            ORDER BY
            CASE WHEN :#{#field} = 'first_name' AND :#{#direction} = 'ASC' THEN u.first_name END,
            CASE WHEN :#{#field} = 'created_at' AND :#{#direction} = 'ASC' THEN u.created_at END,
            CASE WHEN :#{#field} = 'first_name' AND :#{#direction} = 'DESC' THEN u.first_name END DESC,
            CASE WHEN :#{#field} = 'created_at' AND :#{#direction} = 'DESC' THEN u.created_at END DESC
            """, nativeQuery = true)
    Page<UsersView> findAllUsers(String field, String direction, Pageable pageable);

}
