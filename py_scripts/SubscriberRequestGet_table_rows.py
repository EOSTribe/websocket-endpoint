import json
import pprint

from websocket import create_connection

path="streaming"
url = "ws://localhost:8080/"+path

data = {"code":"eosio",
        "scope":"eosio",
        "table":"global"}

messageBody ={
    "apikey":"test-api-key",
              "event":"subscribe",
               "type":"get_table_rows",
              "data": data
              }
ws = create_connection(url)
messageJson = json.dumps(messageBody)
ws.send(messageJson)

while True:
    pprint.pprint(json.loads(ws.recv()))
ws.close()
