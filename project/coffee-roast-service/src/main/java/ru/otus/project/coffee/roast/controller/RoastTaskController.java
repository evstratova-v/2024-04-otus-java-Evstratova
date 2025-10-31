package ru.otus.project.coffee.roast.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.project.coffee.roast.dto.rest.RoastTaskDto;
import ru.otus.project.coffee.roast.model.RoastStatus;
import ru.otus.project.coffee.roast.service.RoastService;

@RestController
@RequiredArgsConstructor
public class RoastTaskController {

    private final RoastService roastService;

    @GetMapping("/api/v1/roast-task")
    public List<RoastTaskDto> getRoastTasks() {
        return roastService.findAll();
    }

    @PatchMapping("/api/v1/roast-task/{id}")
    public RoastTaskDto changeRoastTaskStatus(@PathVariable long id, @RequestParam("status") RoastStatus status) {
        return roastService.changeStatus(id, status);
    }
}
