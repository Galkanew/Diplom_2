package helpers;

import models.User;
import java.util.UUID;

public class DataGenerator {

    public static User getRandomUser() {
        String randomString = UUID.randomUUID().toString().substring(0, 8);
        return User.builder()
                .email("test" + randomString + "@yandex.ru")
                .password("password" + randomString)
                .name("User" + randomString)
                .build();
    }

    public static User getUserWithoutName() {
        String randomString = UUID.randomUUID().toString().substring(0, 8);
        return User.builder()
                .email("test" + randomString + "@yandex.ru")
                .password("password" + randomString)
                .build();
    }

    public static User getUserWithoutEmail() {
        String randomString = UUID.randomUUID().toString().substring(0, 8);
        return User.builder()
                .password("password" + randomString)
                .name("User" + randomString)
                .build();
    }

    public static User getUserWithoutPassword() {
        String randomString = UUID.randomUUID().toString().substring(0, 8);
        return User.builder()
                .email("test" + randomString + "@yandex.ru")
                .name("User" + randomString)
                .build();
    }
}