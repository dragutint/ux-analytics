class ScrollService {
    static EVENTS_BATCH_SIZE_FOR_SEND = 20;
    static scrollActionTimeout = 100;
    static events = [];
    static isScrolling = false;
    static scrollingStartTimestamp;
    static timeout;

    static totalScrollDeltaY = 0;
    static totalScrollDeltaX = 0;

    static lastScrollY = window.scrollY || document.documentElement.scrollTop;
    static lastScrollX = window.scrollX || document.documentElement.scrollLeft;

    //////////////////////////////
    // main listener
    static eventsListener = (e) => {
        console.log("ScrollEventListener");

        if (!this.isScrolling) {
            console.log('Scrolling started');
            this.isScrolling = true;
            this.scrollingStartTimestamp = new Date().toISOString();
        }

        clearTimeout(this.timeout);

        this.timeout = setTimeout(function() {
            // The user has paused scrolling
            console.log('Scrolling stopped');

            console.log('Total Scroll Delta Y:', ScrollService.totalScrollDeltaX);
            console.log('Total Scroll Delta X:', ScrollService.totalScrollDeltaY);

            ScrollService.appendEventToList(
                ScrollService.totalScrollDeltaY,
                ScrollService.totalScrollDeltaX,
                ScrollService.scrollingStartTimestamp,
                new Date().toISOString()
            );

            ScrollService.isScrolling = false;
            ScrollService.totalScrollDeltaY = 0;
            ScrollService.totalScrollDeltaX = 0;

            // if mouse events array is bigger than 20, send it to server
            if (ScrollService.events.length > ScrollService.EVENTS_BATCH_SIZE_FOR_SEND) {
                ServerClient.sendEventsToServer([], [], ScrollService.events);
                ScrollService.events = [];
            }
        }, this.scrollActionTimeout);

        const currentScrollY = window.scrollY || document.documentElement.scrollTop;
        const currentScrollX = window.scrollX || document.documentElement.scrollLeft;

        const scrollDeltaY = currentScrollY - this.lastScrollY;
        const scrollDeltaX = currentScrollX - this.lastScrollX;

        this.totalScrollDeltaY += scrollDeltaY;
        this.totalScrollDeltaX += scrollDeltaX;

        // Update lastScrollY for the next scroll event
        this.lastScrollY = currentScrollY;
        this.lastScrollX = currentScrollX;
    }

    //////////////////////////////
    // small "private" functions for better readability
    static appendEventToList(deltaY, deltaX, startTimestamp, endTimestamp) {

        this.events.push({
            "startTimestamp": startTimestamp,
            "endTimestamp": endTimestamp,
            "deltaX": deltaX,
            "deltaY": deltaY,
            "timestamp": new Date().toISOString()
        });
    }
}
