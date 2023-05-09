package com.iitbtest.librarymanagementsystem.Service;

import com.iitbtest.librarymanagementsystem.entity.User;
import com.iitbtest.librarymanagementsystem.exception.ResourceNotFoundException;
import com.iitbtest.librarymanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final UserRepository userRepository;

    public Optional<User> getMemberByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public User viewMember(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if(optionalUser.isEmpty() || !optionalUser.get().getRole().name().equals("MEMBER")) {
            throw new ResourceNotFoundException("Member", "Id", id);
        }

        return optionalUser.get();
    }

    public User addMember(User user) {
        return userRepository.save(user);
    }

    public void removeMember(Long id) {

        // we need to check whether the member exists in the given database or not
        userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Member", "Id", id));

        userRepository.deleteById(id);
    }

    public User updateUser(User user, Long id) {

        // we need to check whether the user exists in the given database or not
        User existingMember = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Member", "Id", id));

        if(user.getEmail() != null) {
            existingMember.setEmail(user.getEmail());
        }
        if(user.getFirstName() != null) {
            existingMember.setFirstName(user.getFirstName());
        }
        if(user.getLastName() != null) {
            existingMember.setLastName(user.getLastName());
        }

        userRepository.save(existingMember);

        return existingMember;
    }
}
