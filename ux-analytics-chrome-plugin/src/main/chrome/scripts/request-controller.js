
chrome.runtime.onMessage.addListener(function (request, sender, sendResponse) {

    let response = {};

    if (request.action === 'start') {

        // Logic to start functionality
        console.log("UX Analytics - start event listener")

        ServerClient.createUserJourney(request.email)
            .then(response => {
                if(response.status === 200) {
                    // attach all listeners here
                    console.log("UX Analytics - attaching event listeners");

                    // keyboard
                    document.addEventListener("keydown", KeyboardService.eventsListener);
                    document.addEventListener("keyup", KeyboardService.eventsListener);

                    // mouse
                    document.addEventListener("mousemove", MouseService.eventsListener);
                    document.addEventListener("mouseover", MouseService.eventsListener);
                    document.addEventListener("mouseout", MouseService.eventsListener);
                    document.addEventListener("click", MouseService.eventsListener);

                    // scroll
                    document.addEventListener("scroll", ScrollService.eventsListener);
                }

                sendResponse(response);
            })
            .catch(error => {
                sendResponse(error);
            });

        return true;

    } else if (request.action === 'stop') {

        // Logic to stop functionality
        console.log("UX Analytics - stop event listener")

        // remove all listeners
        // keyboard
        document.removeEventListener("keydown", KeyboardService.eventsListener);
        document.removeEventListener("keyup", KeyboardService.eventsListener);

        // mouse
        document.removeEventListener("mousemove", MouseService.eventsListener);
        document.removeEventListener("mouseover", MouseService.eventsListener);
        document.removeEventListener("mouseout", MouseService.eventsListener);
        document.removeEventListener("click", MouseService.eventsListener);

        // scroll
        document.removeEventListener("scroll", ScrollService.eventsListener);

        // send the last batch of events to server
        ServerClient.sendEventsToServer(MouseService.events, KeyboardService.events, ScrollService.events)

        ServerClient.terminate()
            .then(r => {
                sendResponse(r);
            })
            .catch(e => {
                sendResponse(e);
            });

        return true;
    }
});