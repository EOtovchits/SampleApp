package cft.sample.app.controller;

import cft.sample.app.config.ServerProperties;
import cft.sample.app.container.Container;
import cft.sample.app.exceptions.SampleAppException;
import cft.sample.app.validators.RequestValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;

/**
 * A simple controller to accept the incoming data (group id and item id)
 */

@Slf4j
@RestController
@RequestMapping("/next")
public class EndpointController {

    @Autowired
    ServerProperties serverProperties;
    @Autowired
    RequestValidator requestValidator;
    @Autowired
    Container container;

    @RequestMapping(method = RequestMethod.GET, value = "/group/{groupId}/item/{itemId}")
    @ResponseBody
    public ResponseEntity processRequest(@PathVariable String groupId, @PathVariable String itemId) {

        try {

            Long grId = Long.valueOf(groupId);
            Long itmId = Long.valueOf(itemId);

            log.info("Received the following: groupId = [{}], itemId = [{}]", grId, itmId);

            requestValidator.validateIncomingIdOrder(grId, itmId);

            container.validateQueueCapacity(grId, itmId);

            container.addItem(grId, itmId);

            return ResponseEntity.ok("Request processed w/o any errors");

        } catch (NumberFormatException ex) {
            log.error("Smth. went wrong: ", ex);
            return new ResponseEntity<>(MessageFormat.format("Got NumberFormatException when parsing groupId = [{}] and itemId = [{}]",
                    groupId, itemId), null, HttpStatus.BAD_REQUEST);

        } catch (SampleAppException ex) {
            log.error("Smth. went wrong: ", ex);
            return new ResponseEntity<>(MessageFormat.format("Got SampleAppException w/ code [{}] when parsing groupId = [{}] and itemId = [{}]",
                    ex.getErrMess(), groupId, itemId), null, HttpStatus.BAD_REQUEST);
        }
    }
}
