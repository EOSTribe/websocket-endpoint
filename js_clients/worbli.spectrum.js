// Worbli Mainnet JS client:

let endpoint = "wss://api.worbli.eostribe.io/streaming";
let apikey = "test-api-key";

function get_blocks(onMessageHandler, onCloseHandler, onErrorHandler) {
	var messageBody = {
   		"apikey": apikey,
   		"event": "subscribe",
   		"type": "get_blocks"
	};
	var socket = new WebSocket(endpoint);
	socket.onopen = function(e) {
  		socket.send(JSON.stringify(messageBody));
	};
	return setHandlers(socket, onMessageHandler, onCloseHandler, onErrorHandler);
}

function get_transaction(account, onMessageHandler, onCloseHandler, onErrorHandler) {
	var messageBody = {
   		"apikey": apikey,
   		"event": "subscribe",
   		"type": "get_transaction",
        "data": {"account": account}
	};
	var socket = new WebSocket(endpoint);
	socket.onopen = function(e) {
  		socket.send(JSON.stringify(messageBody));
	};
	return setHandlers(socket, onMessageHandler, onCloseHandler, onErrorHandler);
}

function get_actions(account, actions, onMessageHandler, onCloseHandler, onErrorHandler) {
	var messageBody = {
   		"apikey": apikey,
   		"event": "subscribe",
   		"type": "get_actions",
        "data": {"account": account, "actions": actions}
	};
	var socket = new WebSocket(endpoint);
	socket.onopen = function(e) {
  		socket.send(JSON.stringify(messageBody));
	};
	return setHandlers(socket, onMessageHandler, onCloseHandler, onErrorHandler);
}

function get_table_rows(account, table, scope, onMessageHandler, onCloseHandler, onErrorHandler) {
	var messageBody = {
   		"apikey": apikey,
   		"event": "subscribe",
   		"type": "get_table_rows",
        "data": {
    		"code": account,
    		"table": table,
    		"scope": scope
   		}
	};
	var socket = new WebSocket(endpoint);
	socket.onopen = function(e) {
  		socket.send(JSON.stringify(messageBody));
	};
	return setHandlers(socket, onMessageHandler, onCloseHandler, onErrorHandler);
}


// Private function:
function setHandlers(socket, onMessageHandler, onCloseHandler, onErrorHandler) {
	socket.onmessage = function(event) {
		if(onMessageHandler != null) {
			onMessageHandler(event.data);
		} else {
			console.log("On message handler not set, got message: "+event.data);
		}
	}
	socket.onclose = function(event) {
		if(onCloseHandler != null) {
			onCloseHandler(event.data);
		} else {
			console.log("On close handler not set, socket connection closed!");
		}
	}
	socket.onerror = function(error) {
		if(onErrorHandler != null) {
			onErrorHandler(error.message);
		} else {
			console.log("On error handler not set, got error: "+error.message);
		}
	}
	return socket;
}


