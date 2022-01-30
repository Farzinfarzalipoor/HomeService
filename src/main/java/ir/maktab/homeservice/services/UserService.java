package ir.maktab.homeservice.services;

import ir.maktab.homeservice.entities.users.User;
import ir.maktab.homeservice.repositories.UserRepository;
import ir.maktab.homeservice.services.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public User findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new UserNotFoundException());
    }

    public List<User> findByParameters(Map<String, String> parameterMap) {
        return repository.findByParameters(parameterMap).stream().collect(Collectors.toList());
    }

    public List<User> getReportByParameters(Map<String, String> parameterMap) {
        return repository.getReport(parameterMap).stream().collect(Collectors.toList());
    }
}
