package com.example.quantum.controllers.process.insertprocess;

import com.example.quantum.domain.Process;
import com.example.quantum.services.process.InsertProcessPostInput;

public class InsertProcessPostMapper {


    //Domain -> response
    public static InsertProcessPostResponse toResponse(Process process){
        // Mantenha o toResponse como está
        return new InsertProcessPostResponse(
                process.createdBy(),
                process.nameProcess(),
                process.dateApproval(),
                process.dateConclusion(),
                process.sector(),
                process.cyclePDCA()
        );
    }


}
