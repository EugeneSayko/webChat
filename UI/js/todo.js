var messagesList = [];
var username = "Eugene Sayko";
var counter = 0;

function run(){
    var appContainer = document.getElementsByClassName('container')[0];

    appContainer.addEventListener('click', delegateEvent);
    appContainer.addEventListener('change', delegateEvent);
    appContainer.addEventListener('dblclick', delegateEvent);

    messagesList = [
        newMessage("Eugene Sayko", "hello world", true),
        newMessage("user1", "hello!", false)
    ];
    render(messagesList);

}

function delegateEvent(evtObj){
    if(evtObj.type == 'click' && evtObj.target.classList.contains('add-button')){
        addMessage();
    }
    if(evtObj.type == 'click' && evtObj.target.classList.contains('edit-name-button')){
        editUserName();
    }
    if(evtObj.type === 'onclick' && evtObj.target.classList.contains('me')){
        checkTypeClick(evtObj.target.parentElement);
    }
}

function addMessage(){
    var textMessage = document.getElementById("textMessage");
    renderMessage(newMessage(username, textMessage.value, true));
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
            "<span class='glyphicon glyphicon-pencil'></span>" +
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
    var a = document.getElementsByClassName("me-list")[0];
    a.innerHTML = '</span><span>'+username+'</span><a  class="editUserName" id="editUserName" onclick="editUserNameInput(this)"><span class="glyphicon glyphicon-pencil"></span></a>';
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
    console.log(message.getAttribute("data-task-id"));
    message.parentElement.removeChild(message);
}
