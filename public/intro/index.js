// Global Variables
var db;
var MESSAGE_TEMPLATE =
    '<div class="message-container" style="margin: 20px 0px; margin: 20px 0px;">' +
      '<div class="message" style="background-color: #70acef; color: #fff; padding: 15px 20px; max-width: 400px; border-radius: 10px;"></div>' +
      '<div class="name" style="padding: 5px 10px 0px 5px;"></div>' +
    '</div>';
var messageListElement;

// Initializes Page
document.addEventListener("DOMContentLoaded", function(event) {
    //Check For Needed Libraries
    checkSetup();

    //Setup Firebase Stuff
    db = firebase.firestore();
    db.settings({
        timestampsInSnapshots: true
    });
    firebase.auth().onAuthStateChanged(authStateObserver);
    messageListElement = document.getElementById('chat-messages');
    document.getElementById('message-form').addEventListener('submit', sendMessage);

    //Setup Login/Logout Buttons
    $("#google-login").click(function(e) {
        var provider = new firebase.auth.GoogleAuthProvider();
        firebase.auth().signInWithPopup(provider);
    });
    $("#logout").click(function(e) {
        firebase.auth().signOut();
    });
});

// Checks Firebase Is Setip
function checkSetup() {
    if (!window.firebase || !(firebase.app instanceof Function) || !firebase.app().options) {
        window.alert('You have not configured and imported the Firebase SDK. ' +
            'Make sure you go through the codelab setup instructions and make ' +
            'sure you are running the codelab using `firebase serve`');
    }
}

// Runs When Login State Changes
function authStateObserver(user) {
    if (user) { // User is signed in!
        console.log("Logged In!")
        $("#profile-pic").attr("src", firebase.auth().currentUser.photoURL || '/images/profile_placeholder.png')
        $("#welcome").text("Hello there " + firebase.auth().currentUser.displayName + "!");

        $.each($(".login"), function() {
            $(this).css("display", "none");
        });
        $.each($(".protected"), function() {
            $(this).css("display", "block");
        });

        loadMessages();
    } else {
        console.log("Logged Out")
        $("#profile-pic").attr("src", 'images/profile_placeholder.png')

        $.each($(".login"), function() {
            $(this).css("display", "block");
        });
        $.each($(".protected"), function() {
            $(this).css("display", "none");
        });
    }
}

// Send Message to Firestore
function sendMessage(e){
    db.collection("intro-to-firebase")
        .add({
            message: $("#message_input").val(),
            time: Date.now(),
            uid: firebase.auth().currentUser.uid,
            name: firebase.auth().currentUser.displayName
        })
        .then(function() {
            $("#message_input").val("");
        });
}

// Load Messages from Firestore
function loadMessages() {
    db.collection("intro-to-firebase")
        .orderBy("time")
        .onSnapshot(function(snapshot) {
            console.log("snapshot found", snapshot)
            snapshot.forEach(function(msg) {
                console.log(msg.id, msg.data())
                displayMessage(msg.id, msg.data().name, msg.data().message)
            })
        });
}

// Some Example Firebase Stuff
function displayMessage(key, name, text) {
    var div = document.getElementById(key);
    // If an element for that message does not exists yet we create it.
    if (!div) {
        var container = document.createElement('div');
        container.innerHTML = MESSAGE_TEMPLATE;
        div = container.firstChild;
        div.setAttribute('id', key);
        messageListElement.appendChild(div);
    }
    div.querySelector('.name').textContent = "— " + name;
    var messageElement = div.querySelector('.message');
    if (text) { // If the message is text.
        messageElement.textContent = text;
        // Replace all line breaks by <br>.
        messageElement.innerHTML = messageElement.innerHTML.replace(/\n/g, '<br>');
    }
    // Show the card fading-in and scroll to view the new message.
    setTimeout(function() {
        div.classList.add('visible')
    }, 1);
}
