package com.self.app.service;

import com.self.app.entity.JournalEntry;
import com.self.app.entity.User;
import com.self.app.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;
    @Autowired
    private UserService userService;


    public void saveEntry(JournalEntry journalEntry, String username) {
        User user = userService.findByUsername(username);
        journalEntry.setDate(LocalDateTime.now());
        JournalEntry saved = journalEntryRepository.save(journalEntry);
        user.getJournalEntries().add(saved);
        userService.saveUser(user);
    }

    public List<JournalEntry> getAll(){
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id){
        return journalEntryRepository.findById(id);
    }

    public void deleteById(ObjectId id, String username){
        User user = userService.findByUsername(username);
        boolean remove=user.getJournalEntries().removeIf(x->x.getId().equals(id));
        if(remove) {
            userService.saveUser(user);
            journalEntryRepository.deleteById(id);
        }
    }
    public void saveEntry(JournalEntry journalEntry) {
        journalEntryRepository.save(journalEntry);
    }



}
