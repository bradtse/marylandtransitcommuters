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
WHERE route_type = 3;";

$directionsQuery = "
SELECT trip_headsign, direction_id
FROM
trips
WHERE route_id = :routeid 
GROUP BY direction_id;";

$startStopsQuery = "
SELECT T3.stop_id, T2.stop_sequence, T3.stop_name
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

$finalStopsQuery = "
SELECT T4.stop_id, T4.stop_name
FROM 
stop_times AS T2 
JOIN
trips as T1
 ON T1.trip_id = T2.trip_id AND T1.route_id = :routeid AND 
    T2.stop_id = :startstopid AND T1.direction_id = :directionid
JOIN
stop_times as T3
 ON T2.trip_id = T3.trip_id AND T3.stop_sequence > :startstopseq
JOIN
stops as T4
 ON T4.stop_id = T3.stop_id
GROUP BY T4.stop_id
ORDER BY T3.stop_sequence;";

$timesQuery = "
SELECT T3.arrival_time, T3.arrival_time_seconds
FROM 
stop_times AS T2 
JOIN
trips as T1
 ON T1.trip_id = T2.trip_id AND T2.stop_id = :finalstopid AND T1.route_id = :routeid
JOIN
stop_times as T3
 ON T2.trip_id = T3.trip_id AND T3.stop_id = :startstopid
JOIN
stops as T4
 ON T4.stop_id = T3.stop_id
ORDER BY T3.arrival_time_seconds;";

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

