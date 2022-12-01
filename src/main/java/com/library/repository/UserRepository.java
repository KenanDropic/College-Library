package com.library.repository;

import com.library.entity.User;
import com.library.utils.projections.UserLoansView;
import com.library.utils.projections.UsersView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;


@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    String userRoles = "select role1_.name as role_name from user_roles roles0_ " +
            "inner join roles role1_ on roles0_.role_id = role1_.id where roles0_.user_id = :userId";

    String userLoans = """
            SELECT u.email,concat(u.first_name,' ',u.last_name) as fullname,u.phone,
                   l.borrow_date ,l.due_date,l.loan_status,
                   l.return_obligation,l.returned_date,
                   b.source_title
            FROM "user" u
            INNER JOIN loan l on u.user_id = l.user_id
            INNER JOIN book b on b.book_id = l.book_id
            WHERE u.user_id = :userId
            """;

    User findByEmail(String email);

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

    @Query(value = userRoles, nativeQuery = true)
    List<String> findUserRoles(@Param("userId") Long userId);

    @Query(value = userLoans, nativeQuery = true)
    List<UserLoansView> findUserLoans(@Param("userId") Long userId);

}
