class ServerClient {
    static SERVER_URL = "http://localhost:8080";
    static userEmail = "";

    // Initializes user journey on the server
    static createUserJourney(email, scrollHeight = document.body.scrollHeight, scrollWidth = document.body.scrollWidth) {
        this.userEmail = email;

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

        xhr.open("POST", this.SERVER_URL + "/user-journeys", true);
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.setRequestHeader("Accept", "application/json");

        // send and handle error response
        xhr.send(data);

        xhr.onerror = function() {
            console.log("Error: " + xhr.status);
        }
    }

    // Sends mouse and keyboard events to the server
    static sendEventsToServer(mouseActions, keyboardActions, scrollActions) {
        let data = JSON.stringify({
            "mouseActions": mouseActions,
            "keyboardActions": keyboardActions,
            "scrollActions": scrollActions
        });

        const xhr = new XMLHttpRequest();

        xhr.addEventListener("readystatechange", function() {
            if(this.readyState === 4) {
                console.log(this.responseText);
            }
        });
        let requestUrl = this.SERVER_URL + "/user-journeys/" + this.userEmail;

        xhr.open("PUT", requestUrl, true);
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.setRequestHeader("Accept", "application/json");

        // send and handle error response
        xhr.send(data);

        xhr.onerror = function() {
            console.log("Error: " + xhr.status);
        };
    }

    // Terminate user journey on the server
    static terminate() {

        const xhr = new XMLHttpRequest();

        xhr.addEventListener("readystatechange", function() {
            if(this.readyState === 4) {
                console.log(this.responseText);
            }
        });
        let requestUrl = this.SERVER_URL + "/user-journeys/" + this.userEmail + "/terminate";

        xhr.open("POST", requestUrl, true);
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.setRequestHeader("Accept", "application/json");

        // send and handle error response
        xhr.send();

        xhr.onerror = function() {
            console.log("Error: " + xhr.status);
        };
    }
}