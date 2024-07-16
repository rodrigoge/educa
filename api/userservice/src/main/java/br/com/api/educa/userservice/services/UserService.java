package br.com.api.educa.userservice.services;

import br.com.api.educa.userservice.db.UserRepository;
import br.com.api.educa.userservice.db.Users;
import br.com.api.educa.userservice.dto.GetUsersRequest;
import br.com.api.educa.userservice.dto.GetUsersResponse;
import br.com.api.educa.userservice.dto.UserRequest;
import br.com.api.educa.userservice.dto.UserResponse;
import br.com.api.educa.userservice.exception.FlowException;
import br.com.api.educa.userservice.mappers.UserMapper;
import br.com.api.educa.userservice.utils.Utils;
import br.com.api.educa.userservice.validators.UserValidator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
public class UserService {

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Utils utils;

    @Autowired
    private EntityManager entityManager;

    public UserResponse createUser(UserRequest userRequest) {
        log.info("UserService.createUser - starting flow");
        validateUserFields(userRequest);
        log.info("Mapping request to valid object");
        var user = userMapper.toUsers(userRequest);
        log.info("UserService.createUser - validating user password");
        if (!userValidator.hasPasswordValid(user.getPassword()))
            throw new FlowException(
                    HttpStatus.BAD_REQUEST,
                    LocalDateTime.now(),
                    "Password must have at least 4 letters and 4 numbers"
            );
        log.info("UserService.createUser - encrypting user password");
        utils.encryptPassword(user);
        log.info("UserService.createUser - saving user in database");
        var userSaved = userRepository.save(user);
        log.info("UserService.createUser - mapping response to valid object");
        var userResponse = userMapper.toUserResponse(userSaved);
        log.info("UserService.createUser - exiting flow");
        return userResponse;
    }

    private void validateUserFields(UserRequest userRequest) {
        log.info("UserService.validateUserFields - entering flow");
        if (!userRequest.name().isEmpty()) userValidator.hasFieldValid(userRequest.name());
        if (!userRequest.email().isEmpty()) userValidator.hasFieldValid(userRequest.email());
        if (!userRequest.login().isEmpty()) userValidator.hasFieldValid(userRequest.login());
        if (!userRequest.password().isEmpty()) userValidator.hasFieldValid(userRequest.password());
        if (!userRequest.profile().name().isEmpty()) userValidator.hasFieldValid(userRequest.profile().name());
        log.info("UserService.validateUserFields - finishing flow");
    }

    public String deleteUser(UUID userId) {
        log.info("UserService.deleteUser - entering flow");
        var user = userRepository.findById(userId);
        if (user.isEmpty())
            throw new FlowException(
                    HttpStatus.BAD_REQUEST,
                    LocalDateTime.now(),
                    "This user doesn't exists"
            );
        userRepository.deleteById(userId);
        log.info("UserService.deleteUser - finishing flow");
        return "User has been deleted successfully";
    }

    public UserResponse updateUser(UUID userId, UserRequest userRequest) {
        log.info("UserService.updateUser - entering flow");
        var optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty())
            throw new FlowException(
                    HttpStatus.BAD_REQUEST,
                    LocalDateTime.now(),
                    "This user doesn't exists"
            );
        validateUserFields(userRequest);
        var user = optionalUser.get();
        var userMapped = mappingUserUpdate(user, userRequest);
        log.info("UserService.updateUser - validating user password");
        if (!userValidator.hasPasswordValid(userMapped.getPassword()))
            throw new FlowException(
                    HttpStatus.BAD_REQUEST,
                    LocalDateTime.now(),
                    "Password must have at least 4 letters and 4 numbers"
            );
        log.info("UserService.updateUser - encrypting user password");
        utils.encryptPassword(userMapped);
        log.info("UserService.updateUser - saving user in database");
        var userSaved = userRepository.save(user);
        log.info("UserService.updateUser - mapping response to valid object");
        var userResponse = userMapper.toUserResponse(userSaved);
        log.info("UserService.updateUser - finishing flow");
        return userResponse;
    }

    private Users mappingUserUpdate(Users user, UserRequest userRequest) {
        log.info("UserService.mappingUserUpdate - entering flow");
        if (!userRequest.name().isEmpty()) user.setName(userRequest.name());
        if (!userRequest.email().isEmpty()) user.setEmail(userRequest.email());
        if (!userRequest.login().isEmpty()) user.setLogin(userRequest.login());
        if (!userRequest.password().isEmpty()) user.setPassword(userRequest.password());
        if (userRequest.profile() != null) user.setProfile(userRequest.profile());
        if (userRequest.preferences() != null) user.setPreferences(userRequest.preferences());
        log.info("UserService.mappingUserUpdate - finishing flow");
        return user;
    }

    public GetUsersResponse getUsers(GetUsersRequest usersRequest) {
        log.info("UserService.getUsers - entering");
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var criteriaQuery = criteriaBuilder.createQuery(Users.class);
        var root = criteriaQuery.from(Users.class);
        var predicates = buildPredicates(usersRequest, criteriaBuilder, root);
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        addSortAndOrderUsers(usersRequest, criteriaQuery, criteriaBuilder, root);
        var typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(usersRequest.offset());
        typedQuery.setMaxResults(usersRequest.limit());
        var usersResponse = typedQuery.getResultList();
        var users = buildUsers(usersResponse);
        log.info("UserService.getUsers - exiting");
        return new GetUsersResponse(users, users.size());
    }

    private List<Predicate> buildPredicates(GetUsersRequest usersRequest,
                                            CriteriaBuilder criteriaBuilder,
                                            Root<Users> root) {
        log.info("UserService.buildPredicates - entering");
        List<Predicate> predicates = new ArrayList<>();
        if (StringUtils.isNotBlank(usersRequest.name())) {
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")),
                    "%" + usersRequest.name() + "%"
            ));
        }
        if (StringUtils.isNotBlank(usersRequest.email())) {
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("email")),
                    "%" + usersRequest.email() + "%"
            ));
        }
        if (StringUtils.isNotBlank(usersRequest.login())) {
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("login")),
                    "%" + usersRequest.login() + "%"
            ));
        }
        if (ObjectUtils.isNotEmpty(usersRequest.profile())) {
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("profile")),
                    "%" + usersRequest.profile().name() + "%"
            ));
        }
        if (ObjectUtils.isNotEmpty(usersRequest.preferences())) {
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("preferences")),
                    "%" + usersRequest.preferences() + "%"
            ));
        }
        log.info("UserService.buildPredicates - finishing");
        return predicates;
    }

    private List<UserResponse> buildUsers(List<Users> users) {
        return users
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    private void addSortAndOrderUsers(GetUsersRequest usersRequest,
                                      CriteriaQuery<Users> criteriaQuery,
                                      CriteriaBuilder criteriaBuilder,
                                      Root<Users> root) {
        if (StringUtils.isNotBlank(usersRequest.sort().name())) {
            if ("desc".equalsIgnoreCase(usersRequest.order().name())) {
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get(usersRequest.sort().name().toLowerCase())));
            } else {
                criteriaQuery.orderBy(criteriaBuilder.asc(root.get(usersRequest.sort().name().toLowerCase())));
            }
        }
    }
}
