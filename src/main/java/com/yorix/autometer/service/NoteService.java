package com.yorix.autometer.service;

import com.yorix.autometer.model.Car;
import com.yorix.autometer.model.Note;
import com.yorix.autometer.storage.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private final NoteRepository noteRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Note create(Note note) {
        return noteRepository.save(note);
    }

    public Note read(Car car, int id) {
        return noteRepository.getByCarAndId(car, id);
    }

    public List<Note> readAllByCar(Car car) {
        return noteRepository.getAllByCar(car);
    }

    public List<Note> readAll() {
        return noteRepository.findAll();
    }

    public void deleteById(int id) {
        noteRepository.deleteById(id);
    }


    public double getSpendingByCar(Car car) {
        List<Note> notes = readAllByCar(car);
        return getSpending(notes);
    }

    public double getIncomeByCar(Car car) {
        List<Note> notes = readAllByCar(car);
        return getIncome(notes);
    }

    public double getBalance() {
        List<Note> notes = readAll();
        return getSpending(notes) + getIncome(notes);
    }

    private double getSpending(List<Note> notes) {
        double sum = 0;
        for (Note note : notes)
            if (note.getValue() < 0)
                sum += note.getValue();
        return (double) (int) (sum * 100) / 100;
    }

    private double getIncome(List<Note> notes) {
        double sum = 0;
        for (Note note : notes)
            if (note.getValue() > 0)
                sum += note.getValue();
        return (double) (int) (sum * 100) / 100;
    }
}