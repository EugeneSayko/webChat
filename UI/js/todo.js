var application = {
    mainURL: "http://127.0.1.1:4554/chat",
    token: "TN11EN",
    messageList: []
};

var username;

var LOCAL_STORAGE_USERNAME = "chat username";

function newMessage(name, text, time, id){
    return{
        author: name,
        text: text,
        timestamp: time,
        id: id
    };
}

function run(){
    var appContainer = document.getElementsByClassName('container')[0];

    appContainer.addEventListener('click', delegateEvent);
    appContainer.addEventListener('change', delegateEvent);

    username = localStorage.getItem(LOCAL_STORAGE_USERNAME);
    editLogin(username);


    loadMessages(function () {
        render(application);
    });

}

function delegateEvent(evtObj){
    if(evtObj.type == 'click' && evtObj.target.classList.contains('add-button')){
        onAddMessageButtonClick();
    }
    if(evtObj.type == 'click' && evtObj.target.classList.contains('edit-name-button')){
        editUserName();
    }
    
}

function onAddMessageButtonClick(){
    var messageText = messageTextValue();

    addMessage(messageText, function () {
        render(application);
    })
}

function loadMessages(done){
    var url = application.mainURL + "?token=" + application.token;

    ajax("GET", url, null, function (responseText) {
        var response = JSON.parse(responseText);

        application.messageList = response.messages;
        application.token = response.token;

        done();
    });
}

function addMessage(text, done){

    if(text == '' || text == null){
        return;
    }

    var message = newMessage(username, text, new Date().getMilliseconds(), uniqueId());

    ajax('POST', application.mainURL, JSON.stringify(message), function () {
        application.messageList.push(message);
        done();
    })

}

function deleteMessage(id, done){
    
}

function onDeleteClick(element){
    var id = idFromElement(element.parentNode.parentNode);

    deleteMessage(id, function () {
        render(application);
    })
}

function indexById(list, id){
    for(var i = 0; i < list.length; i++){
        if(list[i].id == id){
            return i;
        }
    }

    return -1;
}

function render(root){

    var messages = document.getElementsByClassName('messages')[0];

    var messageMap = root.messageList.reduce(function (accumulator, message) {
        accumulator[message.id] = message;

        return accumulator;
    }, {});

    console.log(messages);

    var notFound = updateListMessages(messages, messageMap);
    removeFromList(messages, notFound);
    appendToList(messages, root.messageList, messageMap);
}

function idFromElement(element){
    return element.attributes['data-message-id'].value;
}

function messageTextValue(){
    var messageInputElement = document.getElementById('textMessage');

    var textInput = messageInputElement.value;

    messageInputElement.value = "";

    return textInput;
}

function renderMessageState(template, message){
    template.setAttribute("data-message-id", message.id);

    var name = template.getElementsByClassName("name")[0];
    name.textContent = message.author;

    var text = template.getElementsByClassName("text")[0];
    text.innerHTML = message.text;

    /*if(!message.me){
        template.className = "";
        var deleteAndEdit = template.getElementsByClassName("delete-and-edit")[0];
        deleteAndEdit.innerHTML = "<span class='msg-time'>5:00 pm</span>";
    }else{*/
        template.className = "me";
        var deleteAndEdit = template.getElementsByClassName("delete-and-edit")[0];
        deleteAndEdit.innerHTML = "<span onclick='onDeleteClick(this)' class='glyphicon glyphicon-trash'></span>" +
            "<span onclick='editMessageInput(this)' class='glyphicon glyphicon-pencil'></span>" +
            "<span class='msg-time'>5:00 pm</span>";


    template.setAttribute("style", "");
}

function updateListMessages(element, itemMap){

    var children = element.children;
    var notFound = [];

    for(var i = 0; i < children.length; i++){
        var child = children[i];

        var id = child.attributes['data-message-id'].value;

        var item = itemMap[id];

        if(item == null){
            notFound.push(child);
            continue;
        }
        renderMessageState(child, item);
        itemMap[i] = null;
    }

    return notFound;
}

function appendToList(element, items, itemMap){

    removeFromList(element.children, items);

    /*for(var i = 0; i < items.length; i++){

        var child = children[i];

        console.log(child);

        if(child.attributes['data-message-id'].value == items[i]){
            element.removeChild(child);
        }

    }*/


    for(var i = 0; i < items.length; i++){
        var item = items[i];

        if(itemMap[item.id] == null){

            continue;
        }

        var child = elementFromTemplate();

        renderMessageState(child, item);
        element.appendChild(child);
    }
}

function removeFromList(element, children) {

    for(var i = 0; i < children.length; i++){
        element.removeChild(children[i]);
    }
}

function elementFromTemplate(){
    var template = document.getElementById("template");

    return template.firstElementChild.cloneNode(true);
}

function editLogin(login){
    var a = document.getElementsByClassName("me-list")[0];
    a.innerHTML = '</span><span>'+login+'</span><a  class="editUserName" id="editUserName" onclick="editUserNameInput(this)">' +
        '<span class="glyphicon glyphicon-pencil"></span></a>';
    saveName(login);
}

function loginInput(){
    username = document.getElementById("input_name").value;
    if((username != '') && (username != null)){
        saveName(username);
        location.href = 'homepage.html';

    }else{
        saveName("default");
    }

    location.href = 'homepage.html';
}

function saveName(login){
    localStorage.setItem(LOCAL_STORAGE_USERNAME, login);
}

function defaultErrorHandle(message){
    //console.error(message);
    outputError();
}

function isError(text){
    if(text == ''){
        return false;
    }

    try {
        var obj = JSON.parse(text);
    }catch (ex){
        return true;
    }

    return !!obj.error;
}

function outputError(){
    var elementError = document.getElementsByClassName('error')[0];

    elementError.setAttribute('style', '');

}

function ajax(method, url, data, continueWith, continueWithError){

    var xhr = new XMLHttpRequest();

    continueWithError = continueWithError || defaultErrorHandle;

    xhr.open(method || 'GET', url, true);

    xhr.onload = function () {

        if(xhr.readyState !== 4){
            return;
        }

        if(xhr.status != 200){
            continueWithError("error on the server side, response " + xhr.status);
            return;
        }

        if(isError(xhr.responseText)){
            continueWithError("Error on the server side, response " + xhr.status);
            return;
        }

        continueWith(xhr.responseText);

    };

    xhr.ontimeout = function () {
        continueWithError("Server timeout!");

    };

    xhr.onerror = function (e) {
        var errorMessage = 'Server connection error !\n'+
            '\n' +
            'Check if \n'+
            '- server is active\n'+
            '- server sends header "Access-Control-Allow-Origin:*"\n'+
            '- server sends header "Access-Control-Allow-Methods: PUT, DELETE, POST, GET, OPTIONS"\n';

        continueWithError(errorMessage);

    };

    xhr.send(data);

}

function uniqueId(){
    var date = Date.now();
    var random = Math.random()*Math.random();

    return Math.floor(date*random);
}
