package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MessageController {

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	@Autowired
	private MessageService msgService;

    @MessageMapping("/hello/{recieverMobNo}/{senderMobNo}")
    public void greeting(@DestinationVariable String recieverMobNo,@DestinationVariable String senderMobNo, IncomingMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        Integer destinationId = msgService.fetchByMobNo(recieverMobNo);
        Integer originId = msgService.fetchByMobNo(senderMobNo);
        if(destinationId == null){
        	simpMessagingTemplate.convertAndSend("/topic/greetings/" + originId,  new OutgoingMessage("Mobile Number "+recieverMobNo+" does not exist"));
        	return;
        }
        Boolean isDuplicate = msgService.addToChatTranscript(destinationId,message);
        if(!isDuplicate){
	        simpMessagingTemplate.convertAndSend("/topic/greetings/" + destinationId,  new OutgoingMessage(message.getMessage()));
        }
        else{
        	simpMessagingTemplate.convertAndSend("/topic/greetings/" + originId,  new OutgoingMessage("Duplicate Message:{"+message.getMessage()+"}.Not sent!"));
        }
    }

    @RequestMapping(value = "/mobile/getcredentials", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CredentialMessage> getCredentials() {
    	CredentialMessage msg = msgService.fetchCredentials();
    	if(msg == null){
    		Boolean isCreated = msgService.createCredentials();
    		if(isCreated){
    			msg = msgService.fetchCredentials();
    		}
    	}
    	return new ResponseEntity<CredentialMessage>(msg, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/mobile/revokecredentials", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> getCredentials(@RequestParam String mobNo) {
    	Boolean isRevoked = msgService.revokeCredentials(mobNo);
    	return new ResponseEntity<Boolean>(isRevoked, HttpStatus.OK);
    }
}
