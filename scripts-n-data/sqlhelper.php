<?php
// Contains all of the predefined functions to access the Transit database
    function connect() {
        $con = mysqli_connect("db477087528.db.1and1.com", "dbo477087528", 
                              "bradley", "db477087528");

        if (mysqli_connect_errno($con)) {
            die("Failed to connect to MySQL " . mysqli_connect_error());
        } else {
            
        }
        return $con;
    }

    function getQuery($query) {
        $connection = connect();

        $result = mysqli_query($connection, $query) or die("Failed to query server");

        $json = array();
        $temp = array();
        while($row = mysqli_fetch_object($result)) {
            $temp = $row;
            array_push($json, $temp);
        }
        echo json_encode($json);
/*        ob_start();
        var_dump($_SERVER);
        $data = ob_get_clean();*/
        $handle = fopen("serverinfo.txt", 'w');
        fwrite($handle, "hi");
        fclose($handle);
    }

