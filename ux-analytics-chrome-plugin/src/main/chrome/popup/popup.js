document.addEventListener('DOMContentLoaded', function () {

    const startButton = document.getElementById('startButton');
    const stopButton = document.getElementById('stopButton');
    const emailInput = document.getElementById('email');

    let successContainer = document.getElementById('successContainer');
    let successText = document.getElementById('successText');
    let errorContainer = document.getElementById('errorContainer');
    let errorText = document.getElementById('errorText');

    // get email from local storage and set it to input
    chrome.storage.sync.get(['email'], function(result) {

        if(result.email === undefined) {
            result.email = "";
        }

        emailInput.value = result.email;
    });

    // get start button pressed from local storage and set it to input
    chrome.storage.sync.get(['startButtonPressed'], function(result) {

        if(result.startButtonPressed === undefined) {
            result.startButtonPressed = false;
        }

        startButton.disabled = result.startButtonPressed;
        stopButton.disabled = !result.startButtonPressed;
    });


    // START BUTTON LISTENER
    startButton.addEventListener('click', function () {

        // save email to local storage so we can use it later when user opens popup again
        chrome.storage.sync.set({email: emailInput.value}, function() {
            console.log('Email is set to ' + emailInput.value);
        });

        chrome.tabs.query({ active: true, currentWindow: true }, function (tabs) {
            chrome.tabs.sendMessage(
                tabs[0].id,
                {
                    action: 'start',
                    email: emailInput.value
                },
                function(response) {
                    console.log(response);

                    if(response.status !== 200) {
                        errorText.innerHTML = response.message;
                        errorContainer.style.display = 'block';
                        successText.innerHTML = '';
                        successContainer.style.display = 'none';
                    } else {
                        errorText.innerHTML = '';
                        errorContainer.style.display = 'none';
                        successText.innerHTML = response.message;
                        successContainer.style.display = 'block';

                        // save that start button is pressed and disable it
                        chrome.storage.sync.set({startButtonPressed: true}, function() {
                            console.log('Start button is pressed');
                        });

                        startButton.disabled = true;
                        stopButton.disabled = false;
                    }
                }
            );
        });
    });


    // STOP BUTTON LISTENER
    stopButton.addEventListener('click', function () {

        chrome.tabs.query({ active: true, currentWindow: true }, function (tabs) {
            chrome.tabs.sendMessage(
                tabs[0].id,
                { action: 'stop' },
                function(response) {
                    console.log(response);

                    if(response.status === 200) {
                        successText.innerHTML = response.message;
                        successContainer.style.display = 'block';
                        errorText.innerHTML = '';
                        errorContainer.style.display = 'none';
                    } else {
                        successText.innerHTML = '';
                        successContainer.style.display = 'none';
                        errorText.innerHTML = response.message;
                        errorContainer.style.display = 'block';
                    }
                }
            );
        });

        // save that start button is pressed and disable it
        chrome.storage.sync.set({startButtonPressed: false}, function() {
            console.log('Stop button is pressed');
        });

        startButton.disabled = false;
        stopButton.disabled = true;
    });
});