
chrome.runtime.onMessage.addListener(function (request, sender, sendResponse) {

    if (request.action === 'start') {
        // Logic to start functionality
        console.log("UX Analytics - start event listener")

        ServerClient.createUserJourney(request.email);

        // attach all listeners here
        // keyboard
        document.addEventListener("keydown", KeyboardService.eventsListener);

        // mouse
        document.addEventListener("mousemove", MouseService.eventsListener);
        document.addEventListener("mouseover", MouseService.eventsListener);
        document.addEventListener("mouseout", MouseService.eventsListener);
        document.addEventListener("click", MouseService.eventsListener);

        // scroll
        document.addEventListener("scroll", ScrollService.eventsListener);

    } else if (request.action === 'stop') {
        // Logic to stop functionality
        console.log("UX Analytics - stop event listener")

        // remove all listeners
        // keyboard
        document.removeEventListener("keydown", KeyboardService.eventsListener);

        // mouse
        document.removeEventListener("mousemove", MouseService.eventsListener);
        document.removeEventListener("mouseover", MouseService.eventsListener);
        document.removeEventListener("mouseout", MouseService.eventsListener);
        document.removeEventListener("click", MouseService.eventsListener);

        // scroll
        document.removeEventListener("scroll", ScrollService.eventsListener);

        // send the last batch of events to server
        ServerClient.sendEventsToServer(MouseService.events, KeyboardService.events, ScrollService.events);
    }

    return true;
});