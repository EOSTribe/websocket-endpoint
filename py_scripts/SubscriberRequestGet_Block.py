import json
import pprint
from websocket import create_connection

path="streaming"
url = "wss://api.telos.eostribe.io/"+path

messageBody = {
    "apikey":"test-api-key",
              "event":"subscribe",
               "type":"get_blocks",
              }
ws = create_connection(url)
ws.send(json.dumps(messageBody))

while True:
    pprint.pprint(json.loads(ws.recv()))
ws.close()
