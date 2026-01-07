package sn.axa.apiaxacnaas.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sn.axa.apiaxacnaas.mappers.SinistreMapper;
import sn.axa.apiaxacnaas.repositories.SinistreRepository;

@Service
@RequiredArgsConstructor
public class SinistreService {
    private final SinistreRepository sinistreRepository;
    private final SinistreMapper sinistreMapper;
}
