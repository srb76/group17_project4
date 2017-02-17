var gameModel;
var selectedID = null;
var selectedFireClass = null;

$( document ).ready(function() {
  // Handler for .ready() called.
  $.getJSON("model", function( json ) {
  gameModel = json;
    //console.log( "JSON Data: " + json );
   });
});

function placeShip() {

   var rowid = document.getElementById('selectedRow').innerHTML;
   var colid = document.getElementById('selectedCol').innerHTML;

   var myID = rowid + "_" + colid;
   document.getElementById(myID).style.border = "1px solid black";
   selectedID = null;


   var selected_ship = document.querySelector('input[name="ship"]:checked').value;
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
     displayGameState(currModel);
     gameModel = currModel;

   });

   request.fail(function( jqXHR, textStatus ) {
     alert( "Illegal Move: " + jqXHR.responseText);
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

function displayGameState(gameModel){
$( '#MyBoard td'  ).css("background-color", "blue");
$( '#TheirBoard td'  ).css("background-color", "blue");

displayShip(gameModel.aircraftCarrier);
displayShip(gameModel.battleship);
displayShip(gameModel.cruiser);
displayShip(gameModel.destroyer);
displayShip(gameModel.submarine);

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

    if(selectedID != null)
        document.getElementById(selectedID).style.border = "1px solid black";

    selectedID = id;
        document.getElementById(selectedID).style.border = "2px solid red";


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
