const KEYBOARD_EVENTS_BATCH_SIZE_FOR_SEND = 20;

let keyboardEvents = [];

function getScreenCoordinates(target) {

    let x = 0;
    let y = 0;

    while (target) {
        x += target.offsetLeft;
        y += target.offsetTop;
        target = target.offsetParent;
    }
    return { x: x, y: y };
}

let keyboardEventsListener = (e) => {
    const KeyID = e.keyCode;

    console.log("KeyboardEventListeners - " + e);

    // find element's position
    let elementX = 0;
    let elementY = 0;

    const obj = getScreenCoordinates(e.target);
    elementX = obj.x;
    elementY = obj.y;

    const event = {
        "keyId": KeyID,
        "key": String.fromCharCode(KeyID),
        "elementX": elementX,
        "elementY": elementY,
        "elementId": e.target.id,
        "elementClass": e.target.className,
        "timestamp": new Date().toISOString()
    }

    keyboardEvents.push(event);

    if (keyboardEvents.length > KEYBOARD_EVENTS_BATCH_SIZE_FOR_SEND) {
        sendEventsToServer([], keyboardEvents);
        keyboardEvents = [];
    }
};