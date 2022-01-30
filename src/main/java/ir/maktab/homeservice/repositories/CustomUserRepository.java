package ir.maktab.homeservice.repositories;

import ir.maktab.homeservice.entities.users.User;

import java.util.List;
import java.util.Map;

public interface CustomUserRepository {
    public List<User> findByParameters(Map<String, String> parameterMap);

    public List<User> getReport(Map<String, String> parameterMap);
}
