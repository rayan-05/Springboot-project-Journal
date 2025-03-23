package com.rayanmitra.journalApp.repository;

import com.rayanmitra.journalApp.entity.JournalEntry;
import com.rayanmitra.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId> {

    User findByUserName(String userName);



}
