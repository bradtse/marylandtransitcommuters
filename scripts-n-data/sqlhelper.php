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
routes;";

$stopsQuery = "
SELECT T3.stop_id, T2.trip_id, T2.stop_sequence, T3.stop_name 
FROM 
trips AS T1
JOIN
stop_times AS T2
    ON T1.trip_id = T2.trip_id AND T1.route_id = :routeid AND T1.direction_id = :directionid 
JOIN 
stops AS T3
    ON T2.stop_id = T3.stop_id
GROUP BY T3.stop_id
ORDER BY T2.stop_sequence;";

$timesQuery = "
SELECT T2.arrival_time, T2.departure_time 
FROM 
trips as T1
JOIN
stop_times as T2
    on T1.trip_id = T2.trip_id AND T1.route_id = :routeid AND T1.direction_id = :directionid
JOIN
stops as T3
    on T2.stop_id = T3.stop_id and T2.stop_id = :stopid
ORDER BY T2.arrival_time;";

/* 
 * Helper functions 
 */
function connect() {
    $host = "db478411744.db.1and1.com";
    $user = "dbo478411744";
    $pass = "bradley";
    $database = "db478411744";
    try {
        $dbh = new PDO("mysql:host=$host;dbname=$database", $user, $pass);
        $dbh->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);
        $dbh->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION); 
    } catch (PDOException $e) {
        print "Error!: " . $e->getMessage() . "<br/>";
        die();
    }

    return $dbh;
}

function queryDB($query, $params = NULL) {
    $dbh = connect();

    $stmt = $dbh->prepare($query);

    if ($stmt) {
        $stmt->execute($params);
    } else {
        echo "mysqli prepare failed";
    }

    $result = $stmt->fetchAll(PDO::FETCH_ASSOC);

    // print '<pre>';
    // var_dump($result);
    // print '</pre>';

    $dbh = NULL;

    echo json_encode($result);

/*        ob_start();
    var_dump($_SERVER);
    $data = ob_get_clean();*/
    // $handle = fopen("serverinfo.txt", 'w');
    // fwrite($handle, "hi");
    // fclose($handle);
}

