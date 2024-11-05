package getterson.insight.services;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public UserEntity registerUser(String name, String username, String document, LocalDate birthDate, String email, String password) throws DuplicatedUserException, InvalidPasswordException {
            Optional<String> documentOptional = DocumentUtil.validateAndClearDocument(document);
            if(documentOptional.isEmpty()) throw new IllegalArgumentException("Documento não é valido.");


            UserEntity userEntity = new UserEntity(name,
                    username,
                    documentOptional.get(),
                    birthDate,
                    email,
                    UserUtil.validatePassword(password),
                    DocumentUtil.getTypeByDocument(document).get());

            if (isRegistred(userEntity)) throw new DuplicatedUserException();
            return save(userEntity);
    }

    private UserEntity save(UserEntity userEntity){
        return userRepository.saveAndFlush(userEntity);
    }

    private boolean isRegistred(UserEntity userEntity){
        return userRepository.findAll().stream()
                .anyMatch(registredUser -> registredUser.equals(userEntity));
    }

    public UserEntity validateUserByEmailAndPassword(String email, String password) throws InvalidUserDataException {
        Optional<UserEntity> userEntityOptional = userRepository.findByEmailAndPassword(email, password);
        if(userEntityOptional.isEmpty()) throw new InvalidUserDataException();

        return userEntityOptional.get();
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

    public void addUserTopicPreference(UserEntity userEntity, TopicPreferenceEntity topicPreferenceEntity){
        List<TopicPreferenceEntity> topicPreferenceEntityList = userEntity.getTopicPreferenceList();
        if(topicPreferenceEntityList == null) topicPreferenceEntityList = new ArrayList<>();
        topicPreferenceEntityList.add(topicPreferenceEntity);

        userEntity.setTopicPreferenceList(topicPreferenceEntityList);
        save(userEntity);
    }
}
