class MouseService {
    static MILLISECONDS_TICK_INTERVAL = 100;
    static EVENTS_BATCH_SIZE_FOR_SEND = 20;

    static events = [];
    static millisecondsFromLastMouseMovementRecorded = 0;
    static mouseOverElementId = "";
    static mouseOverElementClass = "";

    //////////////////////////////
    // main listener
    static eventsListener = (e) => {
        console.log("MouseEventListeners");

        // if e.target exists, set mouseOverElementId and mouseOverElementClass
        if(e.target && e.target.id && e.target.className) {
            this.mouseOverElementId = e.target.id;
            this.mouseOverElementClass = e.target.className;
        }

        // if mouse is out of some element, clear it's id and class
        if(e.type === "mouseout") {
            this.mouseOverElementId = "";
            this.mouseOverElementClass = "";
        }

        this.appendEventToList(e);

        // if mouse events array is bigger than 20, send it to server
        if (this.events.length > this.EVENTS_BATCH_SIZE_FOR_SEND) {
            ServerClient.sendEventsToServer(this.events, [], []);
            this.events = [];
        }
    }

    //////////////////////////////
    // small "private" functions for better readability
    static appendEventToList(e) {

        let action = e.type;

        if (action !== "") {
            if (action === "mousemove" && this.millisecondsFromLastMouseMovementRecorded >= this.MILLISECONDS_TICK_INTERVAL) {
                this.events.push({
                    "action": action,
                    "x": e.pageX,
                    "y": e.pageY,
                    "overElementId": this.mouseOverElementId,
                    "overElementClass": this.mouseOverElementClass,
                    "timestamp": new Date().toISOString()
                });
                this.millisecondsFromLastMouseMovementRecorded = 0;
            } else {
                this.events.push({
                    "action": action,
                    "x": e.pageX,
                    "y": e.pageY,
                    "overElementId": this.mouseOverElementId,
                    "overElementClass": this.mouseOverElementClass,
                    "timestamp": new Date().toISOString()
                });
            }
        }
    }
}

// timer which counts time from last event happen
setInterval(() => {
    MouseService.millisecondsFromLastMouseMovementRecorded += MouseService.MILLISECONDS_TICK_INTERVAL;
}, MouseService.MILLISECONDS_TICK_INTERVAL)