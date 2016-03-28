var messagesList = [];
var username;
var counter = 0;

var LOCAL_STORAGE_USERNAME = "chat username";
var LOCAL_STORAGE_MESSAGE_LIST = "message list";

function run(){
    var appContainer = document.getElementsByClassName('container')[0];

    appContainer.addEventListener('click', delegateEvent);
    appContainer.addEventListener('change', delegateEvent);

    username = localStorage.getItem(LOCAL_STORAGE_USERNAME);
    editLogin(username);

    messagesList = restoreMessages() || [];
    render(messagesList);

}

function delegateEvent(evtObj){
    if(evtObj.type == 'click' && evtObj.target.classList.contains('add-button')){
        addMessage();
    }
    if(evtObj.type == 'click' && evtObj.target.classList.contains('edit-name-button')){
        editUserName();
    }
    
}

function addMessage(){
    var textMessage = document.getElementById("textMessage");
    var newmessage = newMessage(username, textMessage.value, true);
    messagesList.push(newmessage);
    saveMessages();
    renderMessage(newmessage);
    textMessage.value = "";

}

function render(messages){
    for(var i = 0; i < messages.length; i++){
        renderMessage(messages[i]);
    }
}

function renderMessage(message){
    var messages = document.getElementsByClassName("messages")[0];
    var temp = elementFromTemplate();
    renderMessageState(temp, message);
    messages.appendChild(temp);
    counter++;
}

function renderMessageState(template, message){
    template.setAttribute("data-task-id", counter.toString());

    var name = template.getElementsByClassName("name")[0];
    console.log(message.name);
    name.textContent = message.name;

    var text = template.getElementsByClassName("text")[0];
    text.innerHTML = message.text;

    if(!message.me){
        template.className = "";
        var deleteAndEdit = template.getElementsByClassName("delete-and-edit")[0];
        deleteAndEdit.innerHTML = "<span class='msg-time'>5:00 pm</span>";
    }else{
        template.className = "me";
        var deleteAndEdit = template.getElementsByClassName("delete-and-edit")[0];
        deleteAndEdit.innerHTML = "<span onclick='deleteMessage(this)' class='glyphicon glyphicon-trash'></span>" +
            "<span onclick='editMessageInput(this)' class='glyphicon glyphicon-pencil'></span>" +
            "<span class='msg-time'>5:00 pm</span>";
    }

    template.setAttribute("style", "");
}

function elementFromTemplate(){
    var template = document.getElementById("template");
    return template.cloneNode(true);
}

function editUserNameInput(text){
    var nameuser = document.getElementsByClassName("me-list")[0];
    nameuser.innerHTML = '<input type="text" id="editUserNameText">' +
        '<input type="button" class="edit-name-button" value="edit">';
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

function newMessage(name, text, me){
    return{
        name: name,
        text: text,
        me: !!me,
        id: counter
    };
}

function deleteMessage(element){
    var message = element.parentNode.parentNode;
    var index = message.getAttribute("data-task-id");
    messagesList.splice(index, 1);
    message.parentElement.removeChild(message);

}

function loginInput(){
    username = document.getElementById("input_name").value;
    if((username != '') && (username != null)){
        saveName(username);
    }else{
        saveName("default");
    }

    location.href = 'homepage.html';
}

function saveName(login){
    localStorage.setItem(LOCAL_STORAGE_USERNAME, login);
}

function saveMessages(){
    localStorage.setItem(LOCAL_STORAGE_MESSAGE_LIST, JSON.stringify(messagesList));
}

function restoreMessages(){
    var item = localStorage.getItem(LOCAL_STORAGE_MESSAGE_LIST);
    return item && JSON.parse(item);
}

function editMessageInput(element){
    var message = element.parentNode.parentNode;

    var input = message.getElementsByClassName("message")[0];
    input.innerHTML = '<input type="text" class = "input_new_message" id="editMessageText">' +
        '<input type="button" class="edit-message-button" onclick="editMessage(this)" value="edit">';

}

function editMessage(element){
    var newTextMessage = document.getElementById('editMessageText');
    var text = element.parentNode;
    text.innerHTML = '<p class="text">'+newTextMessage.value+'</p>';

    var message = text.parentNode;
    console.log(message);

    var index = message.getAttribute("data-task-id");

    for(var i = 0; i < messagesList.length; i++){
        if(messagesList[i].id == index){
            messagesList[i].text = newTextMessage.value;
        }
    }
}
