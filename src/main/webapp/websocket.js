let ws;

function connect() {
    const username = document.getElementById("username").value;
    console.log(username); // ekaterine_gurgenidze
    const host = document.location.host;
    const pathName = document.location.pathname;
    console.log("ws://" + host + pathName + "chat/" + username); // ws://localhost:8080/web_socket-1.0-SNAPSHOT/chat/ekaterine_gurgenidze
    ws = new WebSocket("ws://" + host + pathName + "chat/" + username);
    ws.onmessage = function (event) {
        const log = document.getElementById("log");
        console.log("event data", event.data);
        const message = JSON.parse(event.data);
        log.innerHTML += message.from + ": " + message.content + "\n";
    }
}

function send() {
    const content = document.getElementById("msg").value;
    const json = JSON.stringify({"content": content}) // converts a JavaScript value to a JSON string
    ws.send(json)
}