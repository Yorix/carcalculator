package com.yorix.autometer.controller;

import com.yorix.autometer.model.Car;
import com.yorix.autometer.model.CarViewDTO;
import com.yorix.autometer.model.Note;
import com.yorix.autometer.service.CarService;
import com.yorix.autometer.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/cars/{carId}/notes/")
public class NoteController {
    private final NoteService noteService;
    private final CarService carService;

    @Autowired
    public NoteController(NoteService noteService, CarService carService) {
        this.noteService = noteService;
        this.carService = carService;
    }

    @PostMapping
    public String create(@PathVariable("carId") int carId, Note note) {
        Car car = carService.read(carId);
        note.setCar(car);
        noteService.create(note);
        return String.format("redirect:/cars/%s/", carId);
    }

    @GetMapping
    public ModelAndView getByCar(@PathVariable("carId") int carId) {
        Car car = carService.read(carId);
        ModelAndView modelAndView = new ModelAndView("notes");
        modelAndView.addObject("car", new CarViewDTO(car));
        modelAndView.addObject("notes", noteService.readAllByCar(car));
        return modelAndView;
    }

    @GetMapping("new-note/")
    public ModelAndView newNoteFrame(@PathVariable("carId") int carId) {
        ModelAndView modelAndView = new ModelAndView("frame-new-note");
        modelAndView.addObject("carId", carId);
        modelAndView.addObject("note", new Note());
        return modelAndView;
    }

    @DeleteMapping("{noteId}/")
    public String delete(@PathVariable("carId") int carId, @PathVariable("noteId") int noteId) {
        noteService.deleteById(noteId);
        return String.format("redirect:/cars/%s/notes/", carId);
    }
}
