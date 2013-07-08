<?php

ini_set('display_errors', 'On');

/*
 * The interface to the gtfs database
 *
 * Author: Bradley Tse
 */
    
include './sqlhelper.php';

// Gets the php's input stream, which should contain the json string. Then
// use json_decode to convert it to an associative array. 
$json_string = file_get_contents('php://input');
$json = json_decode($json_string, true); 
$type = $json['type'];

/*
 * The three types of queries that this api allows:
 *      routes - get a list of routes
 *      stops - get the list of stops for a route
 *      times - get the times for a stop
 *
 * Choosing one of these types calls a pre-defined query to the database.
 * This prevents any potential SQL injections
 *
 * NOTE: Make sure to check the validity of a parameter provided
 */

$type = "routes";

if ($type == "routes") {
    queryDB($routesQuery);
} else if ($type == "stops") {

} else if ($type == "times") {

} else {
    echo "YOU DONT BELONG HERE!!";
}

?>
