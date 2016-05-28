<!DOCTYPE html>
<html>

<head>

    <meta charset="UTF-8">

    <title>Home</title>

    <link rel='stylesheet prefetch' href='http://fonts.googleapis.com/css?family=Open+Sans'>
    <link rel='stylesheet prefetch' href='http://cdnjs.cloudflare.com/ajax/libs/jScrollPane/2.0.14/jquery.jscrollpane.min.css'>
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">

    <style>
        <%@include file="css/styles.css"%>
    </style>

    <script src="js/scrypt.js" ></script>

</head>

<body onload="run();">
<div class="header">

</div>

<div class="container">
    <div class="window-wrapper">

        <div class="window-area">

            <div class="chat-area">
                <div class="title">
                    <b>Messages</b>
                    <p style="display: none" class="error">
                        <span class="glyphicon glyphicon-warning-sign"></span>
                        Error Server
                    </p>
                    <div class="search">
                        <input type="text" name="search-text" id="search-text" placeholder="Enter text to search ...">
                        <span class="glyphicon glyphicon-search" onclick="searchClick()"></span>
                    </div>
                </div>
                <div class="chat-list">
                    <ul class="messages">



                    </ul>
                </div>
                <div class="input-area">
                    <div class="input-wrapper">
                        <input type="text" value="" id="textMessage" placeholder="Enter your message...">
                    </div>
                    <input type="button" value="Send" class="add-button">
                </div>
            </div>
            <div class="right-tabs">
                <h2>List of users</h2>
                <ul class="tabs-container">
                    <li class="active">
                        <ul class="member-list">
                            <li class="me-list">

                            </li>

                        </ul>
                    </li>
                </ul>

            </div>
        </div>

    </div>
</div>

<div class="footer">
    <p>By Eugene Sayko</p>
</div>

<div id="template">

    <li style="display: none" data-message-id="id">
        <div class="name">
            <span class="name">nameuser</span>
        </div>
        <div class="message">
            <p class="text">message</p>
        </div>
        <div class="delete-and-edit">

        </div>
    </li>

</div>

</body>

</html>