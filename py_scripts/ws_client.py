import json

from websocket import create_connection
import websocket
url = "ws://127.0.0.1:8080/"
with open('trx.json') as json_file:
    data = json.load(json_file)
try:
   ws = create_connection(url)
   ws.ping()
   while True:
       ws.send_binary(json.dumps(data))
   ws.close()

except Exception as ex:
    print(ex)
