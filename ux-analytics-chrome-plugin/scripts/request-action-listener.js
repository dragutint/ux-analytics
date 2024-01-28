const SERVER_URL = 'http://localhost:8080';
let userEmail = '';

chrome.runtime.onMessage.addListener(function (request, sender, sendResponse) {

    if (request.action === 'start') {
        // Logic to start functionality
        console.log("UX Analytics - start event listener")

        userEmail = request.email;
        createUserJourney(request.email);

        // attach all listeners here
        // keyboard
        document.addEventListener("keydown", keyboardEventsListener);

        // mouse
        document.addEventListener("mousemove", mouseEventsListener);
        document.addEventListener("mouseover", mouseEventsListener);
        document.addEventListener("mouseout", mouseEventsListener);
        document.addEventListener("click", mouseEventsListener);

    } else if (request.action === 'stop') {
        // Logic to stop functionality
        console.log("UX Analytics - stop event listener")

        // remove all listeners
        // keyboard
        document.removeEventListener("keydown", keyboardEventsListener);

        // mouse
        document.removeEventListener("mousemove", mouseEventsListener);
        document.removeEventListener("mouseover", mouseEventsListener);
        document.removeEventListener("mouseout", mouseEventsListener);
        document.removeEventListener("click", mouseEventsListener);

        // send the last batch of events to server
        sendEventsToServer(mouseEvents, keyboardEvents);
    }

    return true;
});