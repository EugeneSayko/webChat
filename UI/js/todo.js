var username = 'Eugene Sayko';

function run(){
    var appContainer = document.getElementsByClassName('container')[0];

    appContainer.addEventListener('click', delegateEvent);
    appContainer.addEventListener('change', delegateEvent);

}

function delegateEvent(evtobj) {

    if(evtobj.type == 'click' && evtobj.target.classList.contains('add-button')){
        onAddButtonClick(evtobj);
    }
    if(evtobj.type == 'change' && evtobj.target.nodeName == 'INPUT'){
        var label = evtobj.target.parentElement;

        onToggleItem(label);
    }

    if(evtobj.type == 'click' && evtobj.target.classList.contains('edit-name-button')){
        edituserName();
    }

}


function onAddButtonClick(){
    var text = document.getElementById('textMessage');

    add(text.value);
    text.value = '';

}

function onToggleItem(label){

}

function add(value) {
    if(!value){
        return;
    }

    var message = createMessage(value);

    var messages = document.getElementsByClassName('messages')[0];

    messages.appendChild(message);
}

function createMessage(text){
    var divLi = document.createElement('li');
    divLi.classList.add('me');
    divLi.innerHTML = '<div class="name">'
        +'<span >'+username+'</span>'
        +'</div>'
        +'<div class="message">'
        +'<p>'+text+'</p>'
        +'</div>'
        +'<div class="delete-and-edit">'
        +'<span class="glyphicon glyphicon-trash"></span>'
        +'<span class="glyphicon glyphicon-pencil"></span>'
        +'<span class="msg-time">5:00 pm</span>'
        +'</div>';

    return divLi;

}

function editUserNameInput(elem){
    var a = document.getElementsByClassName('me-list')[0];
    a.innerHTML = '<input type="text" id="editUserNameText">' +
        '<input type="button" class="edit-name-button" value="edit">';


}

function edituserName(){

    var newname = document.getElementById('editUserNameText');
    console.log(newname.value);

    if(!newname.value){
        return;
    }

    username = newname.value;

    var a = document.getElementsByClassName('me-list')[0];
    a.innerHTML = ' <li class="me-list"></span><span>'+username+'</span><a  class="editUserName" id="editUserName" onclick="editUserNameInput(this)"><span class="glyphicon glyphicon-pencil"></span></a></li>';
}

function getUsername(){
    return username.value;
}