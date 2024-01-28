// Initializes user journey on the server
let createUserJourney = (email, scrollHeight = document.body.scrollHeight, scrollWidth = document.body.scrollWidth) => {

    const data = JSON.stringify({
        "user": {
            "email": email
        }, "screenInfo": {
            "pageLengthInPixels": scrollHeight,
            "pageWidthInPixels": scrollWidth
        }
    });

    const xhr = new XMLHttpRequest();
    // xhr.withCredentials = true;

    xhr.addEventListener("readystatechange", function () {
        if (this.readyState === 4) {
            console.log(this.responseText);
        }
    });

    xhr.open("POST", SERVER_URL + "/user-journeys", true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.setRequestHeader("Accept", "application/json");

    xhr.send(data);
}


// Sends mouse and keyboard events to the server
let sendEventsToServer = (mouseActions, keyboardActions) => {

    // clone mouse events array to avoid sending same events multiple times
    let data = JSON.stringify({
        "mouseActions": mouseActions,
        "keyboardActions": keyboardActions
    });

    const xhr = new XMLHttpRequest();

    xhr.addEventListener("readystatechange", function() {
        if(this.readyState === 4) {
            console.log(this.responseText);
        }
    });
    let requestUrl = SERVER_URL + "/user-journeys/" + userEmail;

    xhr.open("PUT", requestUrl, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.setRequestHeader("Accept", "application/json");

    xhr.send(data);
}
