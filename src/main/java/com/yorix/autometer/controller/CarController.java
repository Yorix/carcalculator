package com.yorix.autometer.controller;

import com.yorix.autometer.config.CardTextProperties;
import com.yorix.autometer.model.Car;
import com.yorix.autometer.model.CarDTO;
import com.yorix.autometer.model.Note;
import com.yorix.autometer.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cars/")
public class CarController {
    private final CarService carService;
    private final CardTextProperties properties;

    @Autowired
    public CarController(CarService carService, CardTextProperties properties) {
        this.carService = carService;
        this.properties = properties;
    }

    @PostMapping
    public String create(Car car) {
        carService.create(car);
        return String.format("redirect:/cars/%s/", car.getId());
    }

    @GetMapping
    public ModelAndView getAll() {
        ModelAndView modelAndView = new ModelAndView("index");
        List<CarDTO> cars = carService.readAll()
                .stream()
                .map(CarDTO::new)
                .collect(Collectors.toList());
        modelAndView.addObject("cars", cars);
        modelAndView.addObject("textProperties", properties);
        return modelAndView;
    }

    @GetMapping("{id}/")
    public ModelAndView get(@PathVariable("id") int id) {
        CarDTO car = new CarDTO(carService.read(id));
        ModelAndView modelAndView = new ModelAndView("car");
        modelAndView.addObject("car", car);
        modelAndView.addObject("note", new Note());
        return modelAndView;
    }

    @GetMapping("new-car/")
    public ModelAndView newCarPage() {
        ModelAndView modelAndView = new ModelAndView("new-car");
        modelAndView.addObject("car", new Car());
        return modelAndView;
    }

    @PutMapping("{id}/")
    public String update(@PathVariable("id") int id, Car car) {
        carService.update(id, car);
        return String.format("redirect:/cars/%s/", id);
    }

    @DeleteMapping("{id}/")
    public void delete(@PathVariable("id") int id) {
        carService.deleteById(id);
    }
}
