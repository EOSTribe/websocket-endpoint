import json

from websocket import create_connection
path = "streaming"

url = "ws://127.0.0.1:8080/"+path

ws = create_connection(url)
ws.send("subscriber")
ws.close()
