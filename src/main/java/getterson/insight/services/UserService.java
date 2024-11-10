package getterson.insight.services;

import getterson.insight.dtos.SummarySimpleDataDTO;
import getterson.insight.entities.TopicPreferenceEntity;
import getterson.insight.entities.UserEntity;
import getterson.insight.entities.UserPreferenceEntity;
import getterson.insight.exceptions.user.DuplicatedUserException;
import getterson.insight.exceptions.user.InvalidPasswordException;
import getterson.insight.exceptions.user.InvalidUserDataException;
import getterson.insight.exceptions.user.UserNotFoundException;
import getterson.insight.repositories.UserRepository;
import getterson.insight.utils.DocumentUtil;
import getterson.insight.utils.UserUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final String salt;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, @Value("${my.salt}") String salt) {
        this.userRepository = userRepository;
        this.salt = salt;
    }

    public Optional<UserEntity> findById(long id) {
        return userRepository.findById(id);
    }

    private UserEntity save(UserEntity userEntity){
        return userRepository.saveAndFlush(userEntity);
    }

    public UserEntity registerUser(String name, String username, String document, LocalDate birthDate, String email, String rawPassword) throws DuplicatedUserException, InvalidPasswordException {
        Optional<String> documentOptional = DocumentUtil.validateAndClearDocument(document);
        if (documentOptional.isEmpty()) throw new IllegalArgumentException("Documento não é valido.");
        String password = UserUtil.validatePassword(rawPassword);

        UserEntity userEntity = new UserEntity(name,
                username,
                documentOptional.get(),
                birthDate,
                email,
                BCrypt.hashpw(password, salt),
                DocumentUtil.getTypeByDocument(document).get());

        Optional<String> isRegistered = isRegistered(userEntity);
        if (isRegistered.isPresent()) throw new DuplicatedUserException(isRegistered.get());
        return save(userEntity);
    }

    private Optional<String> isRegistered(UserEntity userEntity){
        boolean isRegistered = userRepository.findAll().stream()
                .anyMatch(registredUser -> registredUser.equals(userEntity));

        if (isRegistered) {
            if (userRepository.findByDocument(userEntity.getDocument()).isPresent()) return Optional.of("Já existe um cliente com este documento.");
            if (userRepository.findByEmail(userEntity.getEmail()).isPresent()) return Optional.of("Já existe um cliente com este email.");
            if (userRepository.findByUsername(userEntity.getUsername()).isPresent()) return Optional.of("Já existe um cliente com este username.");
        }

        return Optional.empty();
    }

    public Optional<List<SummarySimpleDataDTO>> findAllAsSimpleDTO(Long id){
        return userRepository.findAllAsSimpleDTO(id);
    }

    public Optional<UserEntity> validateUserLogin(String email, String rawPassword) throws InvalidUserDataException {
        Optional<UserEntity> userEntityOptional = userRepository.findByEmailAndPassword(email, BCrypt.hashpw(rawPassword, salt));
        if(userEntityOptional.isEmpty()) throw new InvalidUserDataException();
        return userEntityOptional;
    }

    public UserEntity getUserByEmail(String email) throws UserNotFoundException {
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(email);
        if(userEntityOptional.isEmpty()) throw new UserNotFoundException();

        return userEntityOptional.get();
    }

    public UserEntity getUserByUsername(String username) throws UserNotFoundException {
        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(username);
        if(userEntityOptional.isPresent())  return userEntityOptional.get();
        throw new UserNotFoundException();
    }

    public void setUserPreference(UserEntity userEntity, UserPreferenceEntity userPreferenceEntity){
        userEntity.setUserPreference(userPreferenceEntity);
        save(userEntity);
    }

    public void addTopicPreference(UserEntity userEntity, TopicPreferenceEntity topicPreferenceEntity){
        List<TopicPreferenceEntity> topicPreferenceEntityList = userEntity.getTopicPreferenceList();
        if(topicPreferenceEntityList == null) topicPreferenceEntityList = new ArrayList<>();
        topicPreferenceEntityList.add(topicPreferenceEntity);

        userEntity.setTopicPreferenceList(topicPreferenceEntityList);
        save(userEntity);
    }
}
