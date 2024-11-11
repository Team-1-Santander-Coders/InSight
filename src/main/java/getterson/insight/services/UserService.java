package getterson.insight.services;

import getterson.insight.dtos.SummarySimpleDataDTO;
import getterson.insight.entities.TopicPreferenceEntity;
import getterson.insight.entities.UserEntity;
import getterson.insight.entities.UserPreferenceEntity;
import getterson.insight.exceptions.user.*;
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

import static getterson.insight.utils.UserUtil.validateEmail;

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

    public UserEntity registerUser(String name, String username, String document, LocalDate birthDate, String email, String rawPassword, String phone) throws DuplicatedUserException, InvalidPasswordException, InvalidDocumentException, InvalidEmailException {
        Optional<String> documentOptional = DocumentUtil.validateAndClearDocument(document);
        if (documentOptional.isEmpty()) throw new InvalidDocumentException();
        String password = UserUtil.validatePassword(rawPassword);
        email = validateEmail(email);



        UserEntity userEntity = new UserEntity(name,
                username,
                documentOptional.get(),
                birthDate,
                email,
                BCrypt.hashpw(password, salt),
                phone,
                DocumentUtil.getTypeByDocument(document).get());

        Optional<String> isRegistered = isRegistered(userEntity);
        if (isRegistered.isPresent()) throw new DuplicatedUserException(isRegistered.get());
        userEntity = save(userEntity);
        UserPreferenceEntity userPreferenceEntity = new UserPreferenceEntity(userEntity);
        userEntity.setUserPreference(userPreferenceEntity);
        userEntity = save(userEntity);

        return userEntity;
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

    public Optional<UserEntity> validateUserLogin(String email, String rawPassword) {
        return userRepository.findByEmailAndPassword(email, BCrypt.hashpw(rawPassword, salt));
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
