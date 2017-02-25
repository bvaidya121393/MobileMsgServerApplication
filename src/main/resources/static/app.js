var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
	var credential = getCredentials();
	$('#mobileNumber').text(credential.mobNo);
    var socket = new SockJS('/simple-mobile-msg-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings/'+credential.destinationId, function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
    });
}

function getCredentials(){
	var response
	$.ajax({
	    url: window.location.href +"/mobile/getcredentials",
	    type: 'GET',
	    async:false,
	    success: function(data) { 
	    	console.log(data)
	    	response = data}
	});
	return response
	
}

function disconnect() {
	$.ajax({
	    url: window.location.href +"/mobile/revokecredentials?mobNo="+$('#mobileNumber').text(),
	    type: 'POST',
	    async:false,
	    success: function(data) { 
	    	console.log(data)
	    	response = data}
	});
	$('#mobileNumber').text("");
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {
    stompClient.send("/app/hello/"+$("#mobNo").val()+"/"+$('#mobileNumber').text(), {}, JSON.stringify({'message': $("#message").val()}));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendMessage(); });
});