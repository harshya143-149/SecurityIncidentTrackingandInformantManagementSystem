package com.harsh.SITIMS.service;

import com.harsh.SITIMS.entity.User;

import java.util.List;

public interface OfficerService {

    User createOfficer(User officer);

    User updateOfficer(Long id, User officer);

    void deleteOfficer(Long id);

    List<User> getAllOfficers();

    User getOfficerById(Long id);

    User getOfficerByEmail(String email);
}
