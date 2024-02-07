class ServerClient {
    static SERVER_URL = "http://localhost:8080";
    static userEmail = "";

    // Initializes user journey on the server
    static createUserJourney(email, scrollHeight = document.body.scrollHeight, scrollWidth = document.body.scrollWidth) {

        this.userEmail = email;

        return new Promise((resolve, reject) => {
            this.userEmail = email;

            const data = JSON.stringify({
                user: {
                    email: email
                },
                screenInfo: {
                    pageLengthInPixels: scrollHeight,
                    pageWidthInPixels: scrollWidth
                }
            });

            const xhr = new XMLHttpRequest();

            xhr.addEventListener("load", function () {
                if (xhr.status === 200) {
                    console.log("User journey created successfully");
                    resolve({ status: xhr.status, message: "User journey created successfully" });
                } else {
                    console.log("Error creating user journey: " + xhr.responseText);
                    let errorMessage = "Error creating user journey";
                    try {
                        const json = JSON.parse(xhr.responseText);
                        errorMessage = json.message || errorMessage;
                    } catch (parseError) {
                        console.log("Error parsing response JSON:", parseError);
                    }
                    reject({ status: xhr.status, message: errorMessage });
                }
            });

            xhr.addEventListener("error", function () {
                console.log("Network error occurred");
                reject({ status: 0, message: "Network error occurred" });
            });

            xhr.open("POST", this.SERVER_URL + "/user-journeys", true);
            xhr.setRequestHeader("Content-Type", "application/json");
            xhr.setRequestHeader("Accept", "application/json");

            xhr.send(data);
        });
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

        xhr.send(data);
    }

    // Terminate user journey on the server
    static terminate() {
        return new Promise((resolve, reject) => {
            const xhr = new XMLHttpRequest();

            xhr.addEventListener("load", function () {
                if (xhr.status === 200) {
                    console.log("User journey terminated successfully");
                    resolve({ status: xhr.status, message: "User journey terminated successfully" });
                } else {
                    console.log("Error terminating user journey: " + xhr.statusText);
                    let errorMessage = "Error terminating user journey";
                    try {
                        const json = JSON.parse(xhr.responseText);
                        errorMessage = json.message || errorMessage;
                    } catch (parseError) {
                        console.log("Error parsing response JSON:", parseError);
                    }
                    reject({ status: xhr.status, message: errorMessage });
                }
            });

            xhr.addEventListener("error", function () {
                console.log("Network error occurred");
                reject({ status: 0, message: "Network error occurred" });
            });

            const requestUrl = this.SERVER_URL + "/user-journeys/" + this.userEmail + "/terminate";

            xhr.open("POST", requestUrl, true);
            xhr.setRequestHeader("Content-Type", "application/json");
            xhr.setRequestHeader("Accept", "application/json");

            xhr.send();
        });
    }
}