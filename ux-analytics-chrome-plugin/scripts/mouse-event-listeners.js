const MILLISECONDS_TICK_INTERVAL = 100;
const MOUSE_EVENTS_BATCH_SIZE_FOR_SEND = 20;

let mouseEvents = [];
let millisecondsFromLastMouseMovementRecorded = 0;
let mouseOverElementId = "";
let mouseOverElementClass = "";


// timer which counts time from last event happen
setInterval(() => {
    millisecondsFromLastMouseMovementRecorded += MILLISECONDS_TICK_INTERVAL;
}, MILLISECONDS_TICK_INTERVAL)


//////////////////////////////
// main listener
let mouseEventsListener = (e) => {
    console.log("MouseEventListeners - " + e);

    // if e.target exists, set mouseOverElementId and mouseOverElementClass
    if(e.target && e.target.id && e.target.className) {
        mouseOverElementId = e.target.id;
        mouseOverElementClass = e.target.className;
    }

    // if mouse is out of some element, clear it's id and class
    if(e.type === "mouseout") {
        mouseOverElementId = "";
        mouseOverElementClass = "";
    }

    if(mouseEventForSending(e)) {

        appendEventToList(e);

        // if mouse events array is bigger than 20, send it to server
        if (mouseEvents.length > MOUSE_EVENTS_BATCH_SIZE_FOR_SEND) {
            sendEventsToServer(mouseEvents, []);
            mouseEvents = [];
        }
    }
}

//////////////////////////////

// small "private" functions for better readability
function mouseEventForSending(event) {
    if(event.type === "mousemove") {
        return true;
    } else if (event.type === "click") {
        return true;
    }
    return false;
}

function mouseEventToActionType(e) {
    let action = "";
    if (e.type === "mousemove") {
        action = "MOVEMENT";
    } else if (e.type === "click") {
        action = "CLICK";
    }
    return action;
}

function appendEventToList(e) {

    // define action type
    let action = mouseEventToActionType(e);

    if (action !== "") {
        if (action === "MOVEMENT" && millisecondsFromLastMouseMovementRecorded >= MILLISECONDS_TICK_INTERVAL) {
            mouseEvents.push({
                "action": action,
                "x": e.pageX,
                "y": e.pageY,
                "overElementId": mouseOverElementId,
                "overElementClass": mouseOverElementClass,
                "timestamp": new Date().toISOString()
            });
            millisecondsFromLastMouseMovementRecorded = 0;
        } else if (action === "CLICK") {
            mouseEvents.push({
                "action": action,
                "x": e.pageX,
                "y": e.pageY,
                "overElementId": mouseOverElementId,
                "overElementClass": mouseOverElementClass,
                "timestamp": new Date().toISOString()
            });
        }
    }
}