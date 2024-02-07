class KeyboardService {

    static EVENTS_BATCH_SIZE_FOR_SEND = 20;
    static events = [];

    //////////////////////////////
    // main listener
    static eventsListener = (e) => {
        const KeyID = e.keyCode;

        console.log("KeyboardEventListeners");

        // find element's position
        let elementX = 0;
        let elementY = 0;

        const obj = this.getScreenCoordinates(e.target);
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

        this.events.push(event);

        if (this.events.length > this.EVENTS_BATCH_SIZE_FOR_SEND) {
            ServerClient.sendEventsToServer([], this.events, []);
            this.events = [];
        }
    };

    //////////////////////////////
    // small "private" functions for better readability
    static getScreenCoordinates(target) {

        let x = 0;
        let y = 0;

        while (target) {
            x += target.offsetLeft;
            y += target.offsetTop;
            target = target.offsetParent;
        }
        return { x: x, y: y };
    }
}