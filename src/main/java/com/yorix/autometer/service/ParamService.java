package com.yorix.autometer.service;

import com.yorix.autometer.errors.NoSuchParamsNameException;
import com.yorix.autometer.model.Param;
import com.yorix.autometer.storage.ParamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParamService {
    private final ParamRepository paramRepository;

    @Autowired
    public ParamService(ParamRepository paramRepository) {
        this.paramRepository = paramRepository;
    }

    public void create(Param param) {
        paramRepository.save(param);
    }

    public int read(String name) {
        return paramRepository.getOne(name).getValue();
    }

    public void update(String name, Param param) {
        if (!name.equals(param.getName()))
            throw new NoSuchParamsNameException();
        paramRepository.save(param);
    }
}
