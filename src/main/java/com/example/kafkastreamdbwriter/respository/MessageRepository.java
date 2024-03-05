package com.example.kafkastreamdbwriter.respository;

import com.example.kafkastreamdbwriter.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
