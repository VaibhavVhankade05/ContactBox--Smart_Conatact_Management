package com.smart.contacts.implementation;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.smart.contacts.entity.Contact;
import com.smart.contacts.entity.User;
import com.smart.contacts.helper.ResourceNotFoundException;
import com.smart.contacts.repository.ContactRepository;
import com.smart.contacts.service.ContactService;


@Service
public class ContactServiceImplementation implements ContactService

{

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public Contact save(Contact contact) {

        String contactId = UUID.randomUUID().toString();
        contact.setId(contactId);
        return contactRepository.save(contact);

    }

    @Override
    public Contact update(Contact contact) {

        var contactOld = contactRepository.findById(contact.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found"));
        contactOld.setName(contact.getName());
        contactOld.setEmail(contact.getEmail());
        contactOld.setPhoneNumber(contact.getPhoneNumber());
        contactOld.setAddress(contact.getAddress());
        contactOld.setDescription(contact.getDescription());
        contactOld.setPicture(contact.getPicture());
        contactOld.setFavorite(contact.isFavorite());
        contactOld.setWebsiteLink(contact.getWebsiteLink());
        contactOld.setLinkedInLink(contact.getLinkedInLink());
        contactOld.setCloudinaryImagePublicId(contact.getCloudinaryImagePublicId());

        return contactRepository.save(contactOld);
    }

    @Override
    public List<Contact> getAll() {
        return contactRepository.findAll();
    }

    @Override
    public Contact getById(String id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with given id " + id));
    }

    @Override
    public void delete(String id) {
        var contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with given id " + id));
        contactRepository.delete(contact);

    }

    @Override
    public List<Contact> getByUserId(String userId) {
        return contactRepository.findByUserId(userId);

    }

    @Override
    public Page<Contact> getByUser(User user, int page, int size, String sortBy, String direction) {

        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        var pageable = PageRequest.of(page, size, sort);

        return contactRepository.findByUser(user, pageable);

    }

    @Override
    public Page<Contact> searchByName(String nameKeyword, int size, int page, String sortBy, String order, User user) {

        Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepository.findByUserAndNameContaining(user, nameKeyword, pageable);
    }

    @Override
    public Page<Contact> searchByEmail(String emailKeyword, int size, int page, String sortBy, String order,
            User user) {
        Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepository.findByUserAndEmailContaining(user, emailKeyword, pageable);
    }

    @Override
    public Page<Contact> searchByPhoneNumber(String phoneNumberKeyword, int size, int page, String sortBy,
            String order, User user) {

        Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepository.findByUserAndPhoneNumberContaining(user, phoneNumberKeyword, pageable);
    }

}

