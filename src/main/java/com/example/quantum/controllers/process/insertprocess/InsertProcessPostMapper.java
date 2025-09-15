package com.example.quantum.controllers.process.insertprocess;

import com.example.quantum.controllers.document.insertdocument.InsertDocumentPostRequest;
import com.example.quantum.domain.Process;
import com.example.quantum.services.process.InsertProcessPostInput;

public class InsertProcessPostMapper {

    //request -> input
    public static InsertProcessPostInput toInput(InsertProcessPostRequest request){
        return new InsertProcessPostInput(
                request.createdBy(),
                request.nameProcess(),
                request.dateApproval(),
                request.dateConclusion(),
                request.sector(),
                request.cyclePDCA()
        );
    }

    //Domain -> response
    public static InsertProcessPostResponse toResponse(Process process){
        return new InsertProcessPostResponse(
                process.createdBy(),
                process.nameProcess(),
                process.dateApproval(),
                process.dateConclusion(),
                process.sector(),
                process.cycle()
        );
    }


}
