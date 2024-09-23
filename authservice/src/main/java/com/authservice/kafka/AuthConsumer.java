package com.authservice.kafka;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.authservice.models.User;
import com.authservice.services.UserDetailsServiceImpl;
import com.basedomain.dto.Role;
import com.basedomain.dto.StudentEvent;
import com.basedomain.dto.UserDTO;

@Service
@Transactional
public class AuthConsumer {

        @Autowired
        private UserDetailsServiceImpl userService;

        @Autowired
        private StudentProducer studentProducer;

        @Autowired
        private PasswordEncoder passwordEncoder;

        private static final Logger LOGGER = LoggerFactory.getLogger(AuthConsumer.class);

        @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
        public void consume(UserDTO event) {

                LOGGER.info(String.format("AuthService received user event => %s ", event.toString()));

                /**
                 * SAVE THE DATA TO THE DATABASE
                 */
                LocalDate localDate = event.getDob();// For reference
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                String password = localDate.format(formatter);
                System.out.println(event);
                User user = User.builder()
                                .fullname(event.getFullname())
                                .email(event.getEmail())
                                .phoneNo(event.getPhoneNo())
                                .role(Role.USER)
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build();

                user.setPassword(passwordEncoder.encode(password));
                userService.save(user);

                StudentEvent student = StudentEvent.builder()
                                .fullname(user.getFullname())
                                .email(user.getEmail())
                                .phoneNumber(user.getPhoneNo())
                                .message("Welcome Message is Send to the User Eamil...")
                                .build();

                System.out.println("User : " + user);
                System.out.println("Student : " + student);

                // Send welcome email to the notification...
                studentProducer.sendMessage(student);

        }

}
