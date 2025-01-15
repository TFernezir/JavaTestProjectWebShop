package example.WebShopTrening.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import example.WebShopTrening.UserService.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>
{
    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.roles WHERE u.userName = :userName")
    Optional<UserEntity> findByUserName(@Param("userName") String userName);
    Optional<UserEntity> findByEmail(String email);
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
}