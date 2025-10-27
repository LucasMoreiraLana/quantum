package com.example.quantum.controllers.warning.insertwarning;


import com.example.quantum.controllers.user.insertuser.InsertUserPostMapper;
import com.example.quantum.services.warning.InsertWarningPostInput;
import com.example.quantum.services.warning.InsertWarningPostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/warnings")
public class InsertWarningPostController {

    @Autowired
    private InsertWarningPostService insertWarningPostService;

    @PostMapping
    public ResponseEntity<InsertWarningPostResponse> createWarning(@Valid @RequestBody InsertWarningPostRequest request){

        final var input = InsertWarningPostMapper.toInput(request);
        final var warning = insertWarningPostService.createWarning(input);
        final var response = InsertWarningPostMapper.toResponse(warning);

        return ResponseEntity.ok(response);
    }
}
