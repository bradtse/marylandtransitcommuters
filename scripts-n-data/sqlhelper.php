<?php

/*
 * This is the file containing all of the helper functions that the gtfsapi
 * utilizes.
 */

/*
 * Predefined database queries 
 */

$routesQuery = "
SELECT route_id, route_short_name, route_long_name  
FROM 
routes 
ORDER BY route_short_name;";

$stopsQueryShell = "
SELECT T3.stop_id, T2.trip_id, T2.stop_sequence, T3.stop_name 
FROM 
trips AS T1
JOIN
stop_times AS T2
    ON T1.trip_id = T2.trip_id AND T1.route_id = %s AND T1.direction_id = %s 
JOIN 
stops AS T3
    ON T2.stop_id = T3.stop_id
GROUP BY T3.stop_id
ORDER BY T2.stop_sequence;";

$timesQueryShell = "
SELECT T2.arrival_time, T2.departure_time 
FROM 
trips as T1
JOIN
stop_times as T2
    on T1.trip_id = T2.trip_id AND T1.route_id = %s AND T1.direction_id = %s 
JOIN
stops as T3
    on T2.stop_id = T3.stop_id and T2.stop_id = %s
ORDER BY T2.arrival_time;";

/* 
 * Helper functions 
 */
function connect() {
    $host = "db478411744.db.1and1.com";
    $user = "dbo478411744";
    $pass = "bradley";
    $database = "db478411744";
    $con = new mysqli($host, $user, $pass, $database);

    if (mysqli_connect_errno()) {
        die("Failed to connect to MySQL " . mysqli_connect_error());
    } 

    return $con;
}

function queryDB($query, $params = NULL, $types = NULL) {
    $connection = connect();

    $stmt = mysqli_prepare($connection, $query);

    if ($stmt) {
        if ($params) {
            call_user_func_array('mysqli_stmt_bind_param', array_merge(array($stmt, $types), $params));
        }
        mysqli_stmt_execute($stmt);
    } else {
        echo "mysqli prepare failed";
    }

    // call_user_func_array('mysqli_stmt_bind_result', array_merge($stmt, $results));
    $result = mysqli_stmt_get_result($stmt);

    while ($row = mysqli_fetch_array($result)) {
        print $row + "\n";
    }

    mysqli_stmt_close($stmt);
    mysqli_close($connection);

    // $json = array();
    // $temp = array();
    // while($row = mysqli_fetch_object($result)) {
    //     $temp = $row;
    //     array_push($json, $temp);
    // }

    // echo json_encode($json);

/*        ob_start();
    var_dump($_SERVER);
    $data = ob_get_clean();*/
    // $handle = fopen("serverinfo.txt", 'w');
    // fwrite($handle, "hi");
    // fclose($handle);
}

