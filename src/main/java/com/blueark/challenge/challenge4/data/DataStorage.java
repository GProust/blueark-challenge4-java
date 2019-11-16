package com.blueark.challenge.challenge4.data;

import com.blueark.challenge.challenge4.entity.UserData;
import com.blueark.challenge.challenge4.entity.UserNotification;
import com.blueark.challenge.challenge4.repository.UsersNotificationRepository;
import com.blueark.challenge.challenge4.repository.UsersRepository;
import com.blueark.challenge.challenge4.resource.rest.UserPayload;
import com.blueark.challenge.challenge4.service.WaterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DataStorage {

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private UsersNotificationRepository usersNotificationRepository;
    @Autowired
    private WaterService waterService;
    private final Map<String, List<WaterData>> waterDataById = new HashMap<>();
    private final Map<String, List<ElectricityData>> electricityDataById = new HashMap<>();


    void addDataForElectricity(String key, List<ElectricityData> csvData) {
        electricityDataById.put(key, csvData);
    }

    void addDataForWater(String key, List<WaterData> csvData) {
        waterDataById.put(key, csvData);
    }

    public List<WaterData> getWaterDataById(String id) {
        return waterDataById.get(id);
    }

    public List<ElectricityData> getElectricityDataById(String id) {
        return electricityDataById.get(id);
    }

    public UserPayload getUserDataById(String id) {
        final UserData user = usersRepository.getOne(id);
        if (user.getAverageConsumption() == null) {
            user.setAverageConsumption(waterService.getAverageConsumption(id));
            usersRepository.save(user);
        }
        final List<String> userNotifications = usersNotificationRepository.findByUserId(id)
                .stream()
                .map(UserNotification::getNotificationType)
                .collect(Collectors.toList());
        return new UserPayload(id, user.getDepartureDate(), user.getReturnDate(), user.getResidenceType(), userNotifications, user.getAverageConsumption());
    }

    @Transactional
    public void saveUsersData(UserPayload userPayload) {
        final UserData userDataById = usersRepository.getOne(userPayload.getUserId());
        userDataById.setDepartureDate(userPayload.getDepartureDate());
        userDataById.setReturnDate(userPayload.getReturnDate());
        userDataById.setResidenceType(userPayload.getResidenceType());
        usersRepository.save(userDataById);
        usersNotificationRepository.deleteAll(usersNotificationRepository.findByUserId(userPayload.getUserId()));
        usersNotificationRepository.saveAll(userPayload.getNotificationSubscribed().stream()
                .map(s -> new UserNotification(userPayload.getUserId(), s)).collect(Collectors.toList()));
        log.info("UserData {}", userDataById);
    }
}
