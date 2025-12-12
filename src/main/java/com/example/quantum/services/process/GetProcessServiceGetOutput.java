package com.example.quantum.services.process;

import java.util.Optional;
import com.example.quantum.domain.Process;

public record GetProcessServiceGetOutput(
        Process process,
        Optional<String> createdByName
){}
