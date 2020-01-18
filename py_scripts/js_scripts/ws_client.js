var socket = new WebSocket("wss://testnet.telos.eostribe.io/streaming");

var actionsList = ["transfer","buyram"];
var messageBody = {
    "apikey":"test-api-key",
    "event":"subscribe",
    "type":"get_actions",
    "data": {"account":"eostribeprod"}
};

socket.onopen = function(e) {
    console.log("[open] Connection established");
    console.log("Sending to server");
    socket.send(messageBody);
};

socket.onmessage = function(event) {
    console.log("[message] Data received from server: ${event.data}");
};

socket.onclose = function(event) {
    if (event.wasClean) {
        console.log("[close] Connection closed cleanly, code=${event.code} reason=${event.reason}");
    } else { console.log("[close] Connection died");
    }
};

socket.onerror = function(error) {
    console.log("[error] ${error.message}");
};