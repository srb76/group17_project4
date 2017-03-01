var gameModel;
var selectedID = null;
var selectedFireClass = null;
var ships_placed = false;
$( document ).ready(getModel());

function getModel () {
  // Handler for .ready() called.
  $.getJSON("model", function( json ) {
  gameModel = json;
  displayGameState(gameModel);
  disableButton('scanButton');
  disableButton('fireButton');
  disableButton('placeShipButton');
    //console.log( "JSON Data: " + json );
    displayMessage("Please place all of your ships by selecting the cell you would like to place the ship at and selecting the orientation of the ship. Then click the place button.");
   });
 }

 //resets the game when the user presses the reset button by reloading the page.
 function resetGame(){
    location.reload();
 }


function placeShip() {
    for(var i=1; i < 11; i++){
                for(var j=1; j < 11; j++){
                   var changeID = i +"_"+j;
                   document.getElementById(changeID).style.border = "1px solid black";

                }
    }
   disableButton('placeShipButton');
   var rowid = document.getElementById('selectedRow').innerHTML;
   var colid = document.getElementById('selectedCol').innerHTML;

   var myID = rowid + "_" + colid;
   document.getElementById(myID).style.border = "1px solid black";
   selectedID = null;

    var $radio = $('input[name="ship"]:checked');
    var selected_ship = $radio.val();
    var radioID = $radio.attr('id');
    var messageToDisplay = "Placed " + selected_ship + " at (" + rowid + ", " + colid + ").";
    displayMessage(messageToDisplay);

   var selected_orientation = document.querySelector('input[name="orientation"]:checked').value;
   var selected_row = document.getElementById('selectedRow').innerHTML;
   var selected_col = document.getElementById('selectedCol').innerHTML;

   //var menuId = $( "ul.nav" ).first().attr( "id" );
   var request = $.ajax({
     url: "/placeShip/"+selected_ship+"/"+selected_row+"/"+selected_col+"/"+selected_orientation,
     method: "post",
     data: JSON.stringify(gameModel),
     contentType: "application/json; charset=utf-8",
     dataType: "json"
   });

   request.done(function( currModel ) {
     document.querySelector('input[name="ship"]:checked').disabled = true;
     document.getElementById(radioID).parentNode.style.color = "grey";
     var id = getNextButton(radioID);
     if(id == "NONE"){
        disableButton('placeShipButton');
        document.getElementById('horizontalRadio').disabled = true;
        document.getElementById('verticalRadio').disabled = true;
        document.getElementById('horizontalRadio').checked = false;
        document.getElementById('verticalRadio').checked = false;
        document.getElementById('verticalRadio').parentNode.style.color = "grey";
        document.getElementById('horizontalRadio').parentNode.style.color = "grey";
        ships_placed = true;


        document.getElementById(radioID).checked = false;
        displayMessage("You have placed all your ships! You may now fire on the enemy by selecting the cell you would like to fire at and then click the fire button. You may also scan for enemy ships my selecting the cell you would like to scan. Scan will tell you if it found a ship in the cell you selected and any adjacent cell.");
     }
     else{
        document.getElementById(id).checked = true;
     }

     displayGameState(currModel);
     gameModel = currModel;

   });

   request.fail(function( jqXHR, textStatus ) {
     var message = "Illegal Move: " + jqXHR.responseText + ". Please Try Again.";
     displayMessage(message);
   });
}

function getNextButton(id){
    var myRadioButtons = document.getElementsByClassName('shipRadio');
    for(i = 0; i < 5; i++){
        if(myRadioButtons[i].disabled == false){
            return myRadioButtons[i].id;
        }
    }
    return "NONE";
}



function scan(){
if(selectedID != null)
        document.getElementById(selectedID).style.border = "1px solid black";
var selected_row = parseInt(document.getElementById('fireRowLabel').innerHTML);
var selected_col = parseInt(document.getElementById('fireColLabel').innerHTML);

var request = $.ajax({
     url: "/scan/"+selected_row+"/"+selected_col,
     method: "post",
     data: JSON.stringify(gameModel),
     contentType: "application/json; charset=utf-8",
     dataType: "json"
   });
    request.done(function( currModel ) {
        if(currModel.scanResult)
            var message = "Scan Found a Ship!";
        else
            var message = "Scan Found Nothing.";

     displayGameState(currModel);
     gameModel = currModel;
     displayMessage(message);
   });




}


function fire(){
if(selectedID != null)
        document.getElementById(selectedID).style.border = "1px solid black";
 var selected_row = document.getElementById('fireRowLabel').innerHTML;
 var selected_col = document.getElementById('fireColLabel').innerHTML;
 var message = "You Fired at (" + selected_row + ", " + selected_col + ")";
 displayMessage(message);
 //document.getElementsByClassName(selectedFireClass)[1].style.border = "1px solid black";

//var menuId = $( "ul.nav" ).first().attr( "id" );
   var request = $.ajax({
     url: "/fire/"+selected_row+"/"+selected_col,
     method: "post",
     data: JSON.stringify(gameModel),
     contentType: "application/json; charset=utf-8",
     dataType: "json"
   });
   request.done(function( currModel ) {
     displayGameState(currModel);
     gameModel = currModel;
     parseGameModel(gameModel);
   });
    function parseGameModel(gameModel){
    document.getElementById("playerScore").innerHTML = gameModel.computerHits.length;
    document.getElementById("computerScore").innerHTML = gameModel.playerHits.length;

    //Check for player victory
    if(gameModel.computerHits.length == 14)
    {
        document.getElementById("endGame").style.display = "block";
        document.getElementById("victory").style.display = "block";
    }

    //Check for AI victory
    if(gameModel.playerHits.length == 14)
    {
        document.getElementById("endGame").style.display = "block";
        document.getElementById("defeat").style.display = "block";
    }

    }
   request.fail(function( jqXHR, textStatus ) {
     var message = "Ilegal move: " + jqXHR.responseText + ". Please Try Again.";
     displayMessage(message);
   });

    //selectedFireClass = null;
}

function log(logContents){
    console.log(logContents);
}

function disableButton(id){
document.getElementById(id).disabled = true;
$(id).css("cursor", "default !important");
document.getElementById(id).style.backgroundColor = "grey";
document.getElementById(id).style.border = "2px solid grey";
document.getElementById(id).style.color = "#D3D3D3";
document.getElementById(id).style.textShadow = "0px 1px 0px black";

}

function enableButton(id){
document.getElementById(id).disabled = false;
$(id).css("cursor", "pointer !important");
if(id == 'scanButton'){
document.getElementById(id).style.backgroundColor = "#008000";
document.getElementById(id).style.border = "2px solid #008000";
document.getElementById(id).style.color = "black";
document.getElementById(id).style.textShadow = "0px 1px 0px #008000";

}
else if(id == 'placeShipButton'){
document.getElementById(id).style.backgroundColor = "#fae500";
document.getElementById(id).style.border = "2px solid #fae500";
document.getElementById(id).style.color = "#ffffff";
document.getElementById(id).style.textShadow = "0px 1px 0px #fae500";
}
else{
document.getElementById(id).style.backgroundColor = "#DC143C";
document.getElementById(id).style.border = "2px solid #DC143C";
document.getElementById(id).style.color = "black";
document.getElementById(id).style.textShadow = "0px 1px 0px #DC143C";

}
}

function displayGameState(gameModel){
$( '#MyBoard td'  ).css("background-color", "blue");
$( '#TheirBoard td'  ).css("background-color", "blue");

disableButton('scanButton');
disableButton('fireButton');


displayShip(gameModel.aircraftCarrier);
displayShip(gameModel.battleship);
displayShip(gameModel.cruiser);
displayShip(gameModel.destroyer);
displayShip(gameModel.submarine);

/*
displayEnemyShip(gameModel.computer_aircraftCarrier);
displayEnemyShip(gameModel.computer_battleship);
displayEnemyShip(gameModel.computer_cruiser);
displayEnemyShip(gameModel.computer_destroyer);
displayEnemyShip(gameModel.computer_submarine);
*/

//Now checks element ending with "_ai"
for (var i = 0; i < gameModel.computerMisses.length; i++) {
   $( '#TheirBoard #' + gameModel.computerMisses[i].Across + '_' + gameModel.computerMisses[i].Down + "_ai" ).css("background-color", "green");
}
for (var i = 0; i < gameModel.computerHits.length; i++) {
   $( '#TheirBoard #' + gameModel.computerHits[i].Across + '_' + gameModel.computerHits[i].Down + "_ai" ).css("background-color", "red");
}

for (var i = 0; i < gameModel.playerMisses.length; i++) {
   $( '#MyBoard #' + gameModel.playerMisses[i].Across + '_' + gameModel.playerMisses[i].Down ).css("background-color", "green");
}
for (var i = 0; i < gameModel.playerHits.length; i++) {
   $( '#MyBoard #' + gameModel.playerHits[i].Across + '_' + gameModel.playerHits[i].Down ).css("background-color", "red");
}



}

function cellPlaceClick(id){
    if(ships_placed == false){
        enableButton('placeShipButton');
        if(selectedID != null){
                for(var i=1; i < 11; i++){
                    for(var j=1; j < 11; j++){
                       var changeID = i +"_"+j;
                       document.getElementById(changeID).style.border = "1px solid black";

                    }
                }
        }

        if(id != null)
            selectedID = id;

        var nums = selectedID.split("_");
        var row = parseInt(nums[0]);
        var col = parseInt(nums[1]);
        document.getElementById('selectedRow').innerHTML = row;
        document.getElementById('selectedCol').innerHTML = col;
        var length = getShipLength();
        var orientation = getOrientation();
        if(orientation == "vertical"){
            for(var i = 0; i < length; i++){
                var newID = (row+i) + "_" + col;
                if((row + length - 1) < 11){
                    document.getElementById(selectedID).style.border = "1px solid #7CFC00";
                    document.getElementById(newID).style.border = "1px solid #7CFC00";
                }
                else{
                        document.getElementById(selectedID).style.border = "1px solid red";
                        document.getElementById(newID).style.border = "1px solid red";

                }

            }
        }
        else{
                for(var i = 0; i < length; i++){
                    var newID = (row) + "_" + (col + i);
                    if((col + length - 1) < 11){
                        document.getElementById(selectedID).style.border = "1px solid #7CFC00";
                        document.getElementById(newID).style.border = "1px solid #7CFC00";
                    }
                    else{
                        document.getElementById(selectedID).style.border = "1px solid red";
                        document.getElementById(newID).style.border = "1px solid red";

                    }
                }


        }
    }


}

function cellFireClick(id){


    //Duplicate of cellPlaceClick but modifies fireRowLabel and fireColLabel
    //Could be merged with cellPlaceClick using another function parameter
    if(ships_placed)
    {
    enableButton('scanButton');
    enableButton('fireButton');


    if(selectedID != null)
        document.getElementById(selectedID).style.border = "1px solid black";

    selectedID = id;
        document.getElementById(selectedID).style.border = "1px solid red";


        var nums = selectedID.split("_");
        var row = nums[0];
        var col = nums[1];
        document.getElementById('fireRowLabel').innerHTML = row;
        document.getElementById('fireColLabel').innerHTML = col;
        document.getElementById('fireColLabel').innerHTML = col;displayShip
     }

}


function displayMessage(toDisplay){
    var destination = document.getElementById('messageBox');
    destination.innerHTML = toDisplay;
 }

function getShipLength(){
    var ship = document.querySelector('input[name="ship"]:checked').value;
    if(ship == "aircraftCarrier")
        return 5;
    else if(ship == "battleship")
        return 4;
    else if(ship == "cruiser")
        return 3;
    else
        return 2;

}
function getOrientation(){
    var orientation = document.querySelector('input[name="orientation"]:checked').value;
    if(orientation == "horizontal")
        return "horizontal"
    else
        return "vertical"
}

function displayShip(ship){
 startCoordAcross = ship.start.Across;
 startCoordDown = ship.start.Down;
 endCoordAcross = ship.end.Across;
 endCoordDown = ship.end.Down;

 if(startCoordAcross > 0){
    if(startCoordAcross == endCoordAcross){
        for (i = startCoordDown; i <= endCoordDown; i++) {
            $( '#MyBoard #'+startCoordAcross+'_'+i  ).css("background-color", "yellow");
        }
    } else {
        for (i = startCoordAcross; i <= endCoordAcross; i++) {
            $( '#MyBoard #'+i+'_'+startCoordDown  ).css("background-color", "yellow");
        }
    }
 }
 }

function displayEnemyShip(ship){

 startCoordAcross = ship.start.Across;
 startCoordDown = ship.start.Down;
 endCoordAcross = ship.end.Across;
 endCoordDown = ship.end.Down;

 if(startCoordAcross > 0){
    if(startCoordAcross == endCoordAcross){
        for (i = startCoordDown; i <= endCoordDown; i++) {
            $( '#TheirBoard #'+startCoordAcross+'_'+i+'_ai'  ).css("background-color", "pink");
        }
    } else {
        for (i = startCoordAcross; i <= endCoordAcross; i++) {
            $( '#TheirBoard #'+i+'_'+startCoordDown+'_ai'  ).css("background-color", "pink");
        }
    }
 }
 }




