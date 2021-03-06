var application={
    mainURL: "http://127.0.1.1:4554/chat",
    token: "TN11EN",
    messageList: []
};

var username;

var LOCAL_STORAGE_USERNAME = "chat username";

function newMessage(name, text, time){
    return{
      author: name,
      text: text,
      timestamp: time,
      id: uniqueId()
    };
}

function uniqueId(){
    var date = Date.now();

    var random = Math.random()*Math.random();

    return Math.floor(date*random);
}

function run(){
    var appContainer = document.getElementsByClassName('container')[0];
    
    appContainer.addEventListener("click", delegateEvent);

    username = localStorage.getItem(LOCAL_STORAGE_USERNAME);

    editLogin(username);

    loadMessage(function () {
        render(application);
    });
}

function ajax(method, url, data, continueWith, continueWithError) {
    var xhr = new XMLHttpRequest();
    
    continueWithError = continueWithError || defaultErrorHandle;
    
    xhr.open(method || 'GET', url, true);
    
    xhr.onload = function(){
        if(xhr.readyState !== 4){
            return;
        }
        
        if(xhr.status != 200){
            continueWithError("Error on the server side, response " + xhr.status);
            return;
        }
        
        if(isError(xhr.responseText)){
            continueWithError("Error on the server side, response " + xhr.status);
            return;
        }
        
        continueWith(xhr.responseText);
    };
    
    xhr.ontimeout = function () {
        continueWithError("server timeout");
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

function defaultErrorHandle(message){
    console.log(message);
    outputError();
}

function outputError(){
    var error = document.getElementsByClassName('error')[0];
    error.setAttribute('style', '');
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

function loadMessage(done){
    var url = application.mainURL+"?token="+application.token;

    ajax("GET", url, null, function(responseText){
        var response = JSON.parse(responseText);

        application.messageList = response.messages;

        application.token = response.token;

        done();
    });
}

function delegateEvent(evtObj){
    if(evtObj.type == "click" && evtObj.target.classList.contains('add-button')){
        onAddMessageButtonClick();
    }
}

function onAddMessageButtonClick(){
    var messageText = messageTextValue();

    addMessage(messageText, function(){
        render(application);
    });
}

function messageTextValue(){
    var messageInputElement = document.getElementById('textMessage');

    var textInput = messageInputElement.value;

    messageInputElement.value = "";

    return textInput;
}

function addMessage(text, done){
    if(text == '' || text == null){
        return;
    }

    var message = newMessage(username, text, Date.now());

    ajax("POST", application.mainURL, JSON.stringify(message), function(){
        application.messageList.push(message);
        done();
    });
}

function render(root){
    var messages = document.getElementsByClassName('messages')[0];

    var messageMap = root.messageList.reduce(function(accumulator, message){
        accumulator[message.id] = message;

        return accumulator;
    }, {});

    var notFound = updateListMessages(messages, messageMap);

    removeFromList(messages, notFound);
    appendToList(messages, root.messageList, messageMap);
}

function updateListMessages(element, itemMap){
    var children = element.children;

    var notFound = [];

    for(var i = 0; i < children.length; i++){

        var child =children[i];

        var id = child.attributes['data-message-id'].value;

        var item = itemMap[id];

        if(item == null){
            notFound.push(child);
            continue;
        }

        renderMessageState(child, item);
        itemMap[id] = null;
    }

    return notFound;
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

function removeFromList(element, children){
    for(var i = 0; i < children.length; i++){
        element.removeChild(children[i]);
    }
}

function appendToList(element, items, itemMap){

    for(var i = 0; i < items.length; i++){
        var item = items[i];

        if(itemMap[item.id] == null){
            continue;
        }

        itemMap[item.id] = null;

        var child = elementFromTemplate();

        renderMessageState(child, item);
        element.appendChild(child);
    }
}

function elementFromTemplate(){
    var template = document.getElementById('template');

    return template.firstElementChild.cloneNode(true);
}

function onDeleteClick(element){
    var id = idFromElement(element.parentNode.parentNode);

    deleteMessage(id, function(){
        render(application);
    });

}

function idFromElement(element){
    return element.attributes['data-message-id'].value;
}

function deleteMessage(id, done){
    var index = indexById(application.messageList, id);
    var message = application.messageList[index];

    var messageToDelete = {
        id: message.id
    };

    ajax('DELETE', application.mainURL, JSON.stringify(messageToDelete), function () {
        application.messageList.splice(index, 1);
        done();
    });
}

function indexById(list, id){
    for(var i = 0; i < list.length; i++){
        if(list[i].id == id){
            return i;
        }
    }

    return -1;
}

function editMessageInput(element){

    var deleteAndEdit = element.parentNode.parentNode;

    deleteAndEdit.innerHTML = '<div class="edit-text"><input class="edit-text-input" type="text" id="editTextMessage">' +
        '<input type="button" class="edit-button-input" value="edit" onclick="editMessageOnClick(this)"></div>';


}

function editMessageOnClick(element){

    var editText = document.getElementById("editTextMessage");

    var id = idFromElement(element.parentNode.parentNode);

    editMessage(element.parentNode.parentNode, editText, id, function(){
        render(application);
    });
}

function editMessage(element, editText, id, done){
    var index = indexById(application.messageList, id);

    var message = application.messageList[index];

    var messageToPut = {
        author: message.author,
        text: editText.value,
        timestamp: message.timestamp,
        id:message.id
    };

    ajax('PUT', application.mainURL, JSON.stringify(messageToPut), function () {
        var temp = elementFromTemplate();

        renderMessageState(temp, messageToPut);

        element.innerHTML = temp.innerHTML;
        message.text = editText.value;
        done();
    });
}

function loginInput(){
    username = document.getElementById("input_name").value;

    if(username != '' && username != null){
        saveName(username);

    }else{
        saveName('default');
    }

    location.href = 'homepage.html';
}

function saveName(name){
    localStorage.setItem(LOCAL_STORAGE_USERNAME, name);
}

function editUserNameInput(text){
    var nameuser = document.getElementsByClassName("me-list")[0];
    nameuser.innerHTML = '<input type="text" id="editUserNameText">' +
        '<input type="button" onclick="editUserName()" class="edit-name-button" value="edit">';
}

function editUserName(){

    var newname = document.getElementById('editUserNameText');
    username = newname.value;
    editLogin(username);
}

function editLogin(login){
    var a = document.getElementsByClassName("me-list")[0];
    a.innerHTML = '</span><span>'+login+'</span><a  class="editUserName" id="editUserName" onclick="editUserNameInput(this)">' +
        '<span class="glyphicon glyphicon-pencil"></span></a>';
    saveName(login);
}