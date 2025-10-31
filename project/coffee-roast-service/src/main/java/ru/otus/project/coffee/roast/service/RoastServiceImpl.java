package ru.otus.project.coffee.roast.service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.project.coffee.roast.dto.kafka.RoastDto;
import ru.otus.project.coffee.roast.dto.kafka.RoastResult;
import ru.otus.project.coffee.roast.dto.rest.RoastTaskDto;
import ru.otus.project.coffee.roast.exception.RoastChangeStatusException;
import ru.otus.project.coffee.roast.exception.RoastTaskException;
import ru.otus.project.coffee.roast.exception.SendingRoastResultException;
import ru.otus.project.coffee.roast.mapper.RoastTaskMapper;
import ru.otus.project.coffee.roast.model.RoastItem;
import ru.otus.project.coffee.roast.model.RoastStatus;
import ru.otus.project.coffee.roast.model.RoastTask;
import ru.otus.project.coffee.roast.repository.RoastTaskRepository;

@Service
@Slf4j
public class RoastServiceImpl implements RoastService {

    private final String topicResponse;

    private final KafkaTemplate<String, RoastResult> template;

    private final RoastTaskRepository roastTaskRepository;

    private final RoastTaskMapper roastTaskMapper;

    public RoastServiceImpl(
            @Value("${application.kafka.topic-response}") String topicResponse,
            KafkaTemplate<String, RoastResult> template,
            RoastTaskRepository roastTaskRepository,
            RoastTaskMapper roastTaskMapper) {
        this.topicResponse = topicResponse;
        this.template = template;
        this.roastTaskRepository = roastTaskRepository;
        this.roastTaskMapper = roastTaskMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public List<RoastTaskDto> findAll() {
        return roastTaskRepository.findAll().stream()
                .map(roastTaskMapper::roastTaskToDto)
                .toList();
    }

    @Transactional
    @Override
    public void saveRoastTasks(List<RoastDto> roastDtos) {
        log.info("saveRoastTasks, size: {}", roastDtos.size());
        List<RoastTask> roastTasks = roastDtos.stream()
                .map(roastDto -> {
                    log.info("saving: {}", roastDto);
                    List<RoastItem> roastItems = roastDto.getRoastItems().stream()
                            .map(item -> new RoastItem(item.getCoffee(), item.getRoastDegree(), item.getWeight()))
                            .toList();
                    return new RoastTask(
                            0, roastDto.getOrderId(), roastDto.getCustomerId(), roastItems, RoastStatus.NEW);
                })
                .toList();
        for (RoastTask roastTask : roastTasks) {
            roastTaskRepository.save(roastTask);
        }
    }

    @Transactional
    @Override
    public RoastTaskDto changeStatus(long id, RoastStatus status) {
        log.info("changeStatus, id: {}, status: {}", id, status);
        var roastTask = roastTaskRepository.findById(id).orElseThrow(() -> new RoastTaskException(id));
        if (roastTask.getRoastStatus().equals(RoastStatus.CLOSE)) {
            throw new RoastChangeStatusException("RoastTask with id %s already closed".formatted(roastTask.getId()));
        }

        roastTask.setRoastStatus(status);
        var roastTaskUpdated = roastTaskRepository.save(roastTask);

        if (status.equals(RoastStatus.CLOSE)) {
            try {
                var roastResult = new RoastResult(roastTaskUpdated.getOrderId(), roastTaskUpdated.getCustomerId());
                log.info("sending {}", roastResult);
                template.send(topicResponse, roastResult)
                        .whenComplete((result, ex) -> {
                            if (ex == null) {
                                log.info(
                                        "{} was sent, offset: {}",
                                        roastResult,
                                        result.getRecordMetadata().offset());
                            } else {
                                log.error("{} was not sent", roastResult, ex);
                            }
                        })
                        .get();
            } catch (ExecutionException e) {
                throw new SendingRoastResultException(
                        "error sending result for RoastTask with id: %s, for orderId: %s"
                                .formatted(roastTaskUpdated.getId(), roastTaskUpdated.getOrderId()),
                        e);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new SendingRoastResultException(e);
            }
        }
        return roastTaskMapper.roastTaskToDto(roastTaskUpdated);
    }
}
