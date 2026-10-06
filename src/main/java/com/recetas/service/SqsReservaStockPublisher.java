package com.recetas.service;

import com.recetas.dto.ReservaStockRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import tools.jackson.databind.json.JsonMapper;

@Service
public class SqsReservaStockPublisher implements ReservaStockPublisher {

    private final SqsClient sqsClient;
    private final JsonMapper jsonMapper;

    @Value("${aws.sqs.reserva-url}")
    private String queueUrl;

    public SqsReservaStockPublisher(
            SqsClient sqsClient,
            JsonMapper jsonMapper) {

        this.sqsClient = sqsClient;
        this.jsonMapper = jsonMapper;
    }

    @Override
    public void publicarReserva(ReservaStockRequestDTO solicitud) {

        String mensaje = jsonMapper.writeValueAsString(solicitud);

        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(mensaje)
                .build();

        sqsClient.sendMessage(request);
    }
}