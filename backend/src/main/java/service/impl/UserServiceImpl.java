package service.impl;

import entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.UserRepository;
import service.UserService;

@Service
public class UserServiceImpl implements UserService {

//  // add this to call userRepository
//  @Autowired
//  private UserRepository userRepository;
//
//  // TODO: this is a sample Repository usage, feel free to add any if you need in the UserRepository
//  // TODO:
//  public User getUserByUsername(String username){
//    return userRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));
//  };
}
