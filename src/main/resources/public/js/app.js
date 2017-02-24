var gameModel;
var selectedID = null;
var selectedFireClass = null;

$( document ).ready(function() {
  // Handler for .ready() called.
  $.getJSON("model", function( json ) {
  gameModel = json;
  disableButton('scanButton');
  disableButton('fireButton');
  disableButton('placeShipButton');
    //console.log( "JSON Data: " + json );
   });
});

function placeShip() {
   disableButton('placeShipButton');
   var rowid = document.getElementById('selectedRow').innerHTML;
   var colid = document.getElementById('selectedCol').innerHTML;

   var myID = rowid + "_" + colid;
   document.getElementById(myID).style.border = "1px solid black";
   selectedID = null;

    var $radio = $('input[name="ship"]:checked');
    var selected_ship = $radio.val();
    var radioID = $radio.attr('id');

   console.log(radioID);
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

        document.getElementById(radioID).checked = false;
     }
     else{
        document.getElementById(id).checked = true;
     }

     displayGameState(currModel);
     gameModel = currModel;

   });

   request.fail(function( jqXHR, textStatus ) {
     alert( "Illegal Move: " + jqXHR.responseText);
   });
}

function getNextButton(id){
    var myRadioButtons = document.getElementsByClassName('shipRadio');
    console.log(myRadioButtons);

    for(i = 0; i < 5; i++){
        if(myRadioButtons[i].disabled == false){
            return myRadioButtons[i].id;
        }
    }
    return "NONE";
}



function scan(){
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
            document.getElementById('scanResult').innerHTML = "Scan Found a Ship!";
        else
            document.getElementById('scanResult').innerHTML = "Scan Found Nothing.";
     displayGameState(currModel);
     gameModel = currModel;

   });




}


function fire(){

 var selected_row = document.getElementById('fireRowLabel').innerHTML;
 var selected_col = document.getElementById('fireColLabel').innerHTML;

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


    }
   request.fail(function( jqXHR, textStatus ) {
     alert( "Ilegal move: " + jqXHR.responseText);
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
    enableButton('placeShipButton');

    if(selectedID != null)
        document.getElementById(selectedID).style.border = "1px solid black";


    selectedID = id;
    document.getElementById(selectedID).style.border = "1px solid red";


    var nums = selectedID.split("_");
    var row = nums[0];
    var col = nums[1];
    document.getElementById('selectedRow').innerHTML = row;
    document.getElementById('selectedCol').innerHTML = col;

}

function cellFireClick(id){

    //Duplicate of cellPlaceClick but modifies fireRowLabel and fireColLabel
    //Could be merged with cellPlaceClick using another function parameter
    enableButton('scanButton');
    enableButton('fireButton');

    document.getElementById('scanResult').innerHTML = "";
    if(selectedID != null)
        document.getElementById(selectedID).style.border = "1px solid black";

    selectedID = id;
        document.getElementById(selectedID).style.border = "1px solid red";


        var nums = selectedID.split("_");
        var row = nums[0];
        var col = nums[1];
        document.getElementById('fireRowLabel').innerHTML = row;
        document.getElementById('fireColLabel').innerHTML = col;
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




