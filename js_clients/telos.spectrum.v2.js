// Telos Mainnet JS client:

let endpoint = "wss://api.telos.eostribe.io/streaming";
let apikey = "test-api-key";

// Call this method once top open connection:
function open_connection(onOpenHandler, onMessageHandler, onCloseHandler, onErrorHandler) {
	var socket = new WebSocket(endpoint);
	socket.onopen = function(event) {
		if(onOpenHandler != null) {
			onOpenHandler(event);
		} else {
			console.log("On open handler not set, socket connection open!");
		}
	}
	socket.onmessage = function(event) {
		if(onMessageHandler != null) {
			onMessageHandler(event);
		} else {
			console.log("On message handler not set, got message: "+event.data);
		}
	}
	socket.onclose = function(event) {
		if(onCloseHandler != null) {
			onCloseHandler(event);
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


function get_blocks(socket) {
	var messageBody = {
   		"apikey": apikey,
   		"event": "subscribe",
   		"type": "get_blocks"
	};
  	socket.send(JSON.stringify(messageBody));
}

function get_transaction(socket, account) {
	var messageBody = {
   		"apikey": apikey,
   		"event": "subscribe",
   		"type": "get_transaction",
        "data": {"account": account}
	};
  	socket.send(JSON.stringify(messageBody));
}

function get_actions(socket, account, actions) {
	var messageBody = {
   		"apikey": apikey,
   		"event": "subscribe",
   		"type": "get_actions",
        "data": {"account": account, "actions": actions}
	};
  	socket.send(JSON.stringify(messageBody));
}

function get_table_rows(socket, account, table, scope) {
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
  	socket.send(JSON.stringify(messageBody));
}



