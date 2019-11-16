package com.blueark.challenge.challenge4.data;

import com.blueark.challenge.challenge4.entity.UserData;
import com.blueark.challenge.challenge4.entity.UserNotification;
import com.blueark.challenge.challenge4.repository.UsersNotificationRepository;
import com.blueark.challenge.challenge4.repository.UsersRepository;
import com.blueark.challenge.challenge4.resource.rest.UserPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DataStorage {

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private UsersNotificationRepository usersNotificationRepository;
    private final Map<String, List<WaterData>> waterDataById = new HashMap<>();
    private final Map<String, List<ElectricityData>> electricityDataById = new HashMap<>();


    void addDataForElectricity(String key, List<ElectricityData> csvData) {
        electricityDataById.put(key, csvData);
    }

    void addDataForWater(String key, List<WaterData> csvData) {
        waterDataById.put(key, csvData);
    }

    public List<WaterData> getWaterDataById(String id) {
        return new ArrayList<>(waterDataById.get(id));
    }

    public List<ElectricityData> getElectricityDataById(String id) {
        return new ArrayList<>(electricityDataById.get(id));
    }

    public UserData getUserDataById(String id) {
        return usersRepository.getOne(id);
    }

    public void saveUsersData(UserPayload userPayload) {
        final UserData userDataById = getUserDataById(userPayload.getUserId());
        userDataById.setDepartureDate(userPayload.getDepartureDate());
        userDataById.setReturnDate(userPayload.getReturnDate());
        userDataById.setResidenceType(userPayload.getResidenceType());
        usersRepository.save(userDataById);
        usersNotificationRepository.deleteAll(usersNotificationRepository.findByUserId(userPayload.getUserId()));
        usersNotificationRepository.saveAll(userPayload.getNotificationSubscribed().stream()
                .map(s -> new UserNotification(userPayload.getUserId(), s)).collect(Collectors.toList()));
    }
}
