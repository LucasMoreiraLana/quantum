package com.example.quantum.controllers.process.insertprocess;




import com.example.quantum.services.process.InsertProcessPostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/process")
public class InsertProcessPostController {

    @Autowired
    private InsertProcessPostService insertProcessPostService;

    @PostMapping
    public ResponseEntity<InsertProcessPostResponse> create(@Valid @RequestBody InsertProcessPostRequest request){

        //request -> input
        final var input = InsertProcessPostMapper.toInput(request);

        //input -> domain
        final var process = insertProcessPostService.insertProcess(input);

        //domain -> response
        final var response = InsertProcessPostMapper.toResponse(process);

        return ResponseEntity.ok(response);

    }
}
