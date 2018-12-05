// Global Variables
var db;
var messages;
var MESSAGE_TEMPLATE =
    '<div class="message-container" style="margin: 20px 0px; margin: 20px 0px;">' +
      '<div class="message" style="background-color: #70acef; color: #fff; padding: 15px 20px; max-width: 400px; border-radius: 10px;"></div>' +
      '<div class="name" style="padding: 5px 10px 0px 5px;"></div>' +
    '</div>';

document.addEventListener("DOMContentLoaded", function(e){
    db = firebase.firestore();

    firebase.auth().onAuthStateChanged(authStateObserver);
    messages = document.getElementById('chat-messages');
    document.getElementById("message-form").addEventListener("submit", sendMessage);

    $("#google-login").on("click", function(e) {
        var provider = new firebase.auth.GoogleAuthProvider();
        firebase.auth().signInWithPopup(provider);
    })

    $("#logout").on("click", function(e) {
        firebase.auth().signOut();
    })
});

// TODO: Update UI
function authStateObserver(user) {
    if (user) {
        console.log("Logged In");

        $("#profile-pic").attr("src", firebase.auth().currentUser.photoURL)

        $.each($(".login"), function() {
            $(this).css("display", "none");
        })

        $.each($(".protected"), function() {
            $(this).css("display", "block");
        })

        loadMessages();
    } else {
        console.log("Signed Out");

        $("#profile-pic").attr("src", "../images/profile_placeholder.png")


        $.each($(".login"), function() {
            $(this).css("display", "block");
        })

        $.each($(".protected"), function() {
            $(this).css("display", "none");
        })
    }
}

// TODO: Post Document to Collection
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

// TODO: Load Sorted Collection
function loadMessages() {
    db.collection("intro-to-firebase")
        .orderBy("time")
        .onSnapshot(function(snap) {
            snap.forEach(function(msg) {
                console.log(msg.id, msg.data())
                displayMessage(msg.id, msg.data().name, msg.data().message, msg.data().uid);
            })
        })
}

// TODO: Display Message to User
function displayMessage(key, name, text, uid) {
    var div = document.getElementById(key)
    if(!div){
        var container = document.createElement("div");
        container.innerHTML = MESSAGE_TEMPLATE;
        div = container.firstChild;
        div.setAttribute("id", key);
        messages.appendChild(div);
    }
    div.querySelector(".name").textContent = "-- " + name;
    var messageElement = div.querySelector(".message");
    if(text){
        messageElement.textContent = text;
        messageElement.innerHTML = messageElement.innerHTML.replace(/\n/g, '<br>')
    }
    if(uid == firebase.auth().currentUser.uid){
        div.classList.add("myMessage");
    }
}
