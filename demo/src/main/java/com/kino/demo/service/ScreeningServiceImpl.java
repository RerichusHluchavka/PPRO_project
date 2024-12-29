package com.kino.demo.service;

import com.kino.demo.model.Screening;
import com.kino.demo.repository.ScreeningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScreeningServiceImpl implements ScreeningService {
    private ScreeningRepository screeningRepository;

    @Autowired
    public ScreeningServiceImpl(ScreeningRepository screeningRepository) {
        this.screeningRepository = screeningRepository;
    }

    @Override
    public Screening getScreeningById(long id) {
        return screeningRepository.findById(id).orElse(null);
    }

    @Override
    public void createScreening(Screening screening){
        screeningRepository.save(screening);
    }

    @Override
    public List<Screening> getAllScreenings() {
        return screeningRepository.findAll();
    }
    @Override
    public Screening deleteScreeningById(long id){
        Optional<Screening> screeningDB = screeningRepository.findById(id);
        if(screeningDB.isPresent()){
            Screening screening = screeningDB.get();
            screeningRepository.delete(screening);
            return screening;
        }
        return null;
    }

    @Override
    public void updateScreening(Screening screening) {
        Optional<Screening> screeningDB = screeningRepository.findById(screening.getId());
        if(screeningDB.isPresent()){
            screeningRepository.save(screening);
        }
    }
}
