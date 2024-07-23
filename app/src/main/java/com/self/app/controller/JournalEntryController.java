package com.self.app.controller;

import com.self.app.entity.JournalEntry;
import com.self.app.entity.User;
import com.self.app.service.JournalEntryService;
import com.self.app.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
     private JournalEntryService journalEntryService;
   @Autowired
    private UserService userService;

            @GetMapping
            public ResponseEntity<?> getAllJournalEntriesOfUser(){
                    Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
                    String username = authentication.getName();
                    User user=userService.findByUsername(username);
                    List<JournalEntry> all = user.getJournalEntries();
                    if(all!=null&& !all.isEmpty()){
                        return new ResponseEntity<>(all,HttpStatus.OK);
                    }
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }


        @PostMapping
        public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry entry){
           try{
               Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
               String username = authentication.getName();
               journalEntryService.saveEntry(entry,username);
            return new  ResponseEntity<>(entry,HttpStatus.OK);
        }
           catch (Exception e){
               return new  ResponseEntity<>(HttpStatus.NOT_FOUND);
           }
        }
    @GetMapping("/id/{myid}")
    public ResponseEntity<JournalEntry> getEntryById(@PathVariable ObjectId myid){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user=userService.findByUsername(username);
        List<JournalEntry> collect=user.getJournalEntries().stream().filter(x->x.getId().equals(myid)).collect(Collectors.toList());
        if(!collect.isEmpty()){
        Optional<JournalEntry> journalEntry = journalEntryService.findById(myid);
        if(journalEntry.isPresent()){
            return new ResponseEntity<>(journalEntry.get(),HttpStatus.OK);
        }
        }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        @DeleteMapping("/delete/{id}")
        public ResponseEntity<?> deleteEntry(@PathVariable ObjectId id){
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            journalEntryService.deleteById(id,username);
              return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        @PutMapping("id/{id}")
        public ResponseEntity<JournalEntry> updateEntry( @PathVariable  ObjectId id , @RequestBody JournalEntry newEntry ){
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user=userService.findByUsername(username);
            List<JournalEntry> collect=user.getJournalEntries().stream().filter(x->x.getId().equals(id)).collect(Collectors.toList());
            if(!collect.isEmpty()){
                Optional<JournalEntry> journalEntry=journalEntryService.findById(id);
            if(journalEntry.isPresent()){
                JournalEntry oldEntry =journalEntry.get();
                oldEntry.setTitle((newEntry.getTitle())!=null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : oldEntry.getTitle());
                oldEntry.setContent((newEntry.getContent()!=null && !newEntry.getContent().equals("")? newEntry.getContent() :oldEntry.getContent()));
                journalEntryService.saveEntry(oldEntry);
                return new ResponseEntity<>(oldEntry,HttpStatus.OK);
            }
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
}

