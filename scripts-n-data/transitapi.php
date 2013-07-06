<?php
    
include './sqlhelper.php';

// Gets the php's input stream, which should contain the json string. Then
// use json_decode to convert it to an associative array. 
$json_string = file_get_contents('php://input');
$json = json_decode($json_string, true); 

// If value key has been set
if ($json['select']) {
    getQuery($json['select']);
} else {
    echo "Not post";
    $my_file = "test2.txt";
    $handle = fopen($my_file, 'w');
    fwrite($handle, print_r($json_string));
    fclose($handle);
}

?>
