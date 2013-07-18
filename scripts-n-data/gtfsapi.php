<?php

// ini_set('display_errors', 'On');

/*
 * The interface to the gtfs database
 *
 * Author: Bradley Tse
 */
    
include './gtfshelper.php';

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

// These should be checked or whitelisted 
$route_id = $json['route_id'];
$direction_id = $json['direction_id'];
$stop_id = $json['stop_id'];

if ($type == "routes") {
    queryDB($routesQuery);
} else if ($type == "directions") {
    $keys = array(
                    ":routeid" => $route_id
                    );
    queryDB($directionsQuery, $keys);
} else if ($type == "stops") {
    $keys = array(
                    ":routeid" => $route_id,
                    ":directionid" => $direction_id
                    );
    queryDB($stopsQuery, $keys);
} else if ($type == "times") {
    $keys = array(
                    ":routeid" => $route_id,
                    ":directionid" => $direction_id,
                    ":stopid" => $stop_id 
                    );
    queryDB($timesQuery, $keys);
} else {
    echo "YOU DONT BELONG HERE!!";
}

?>
