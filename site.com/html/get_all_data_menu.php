<?php

$response = array();

require 'db_config.php';
$con = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

$result = mysqli_query($con, "SELECT m.*, c.parent FROM menu_site AS m LEFT JOIN connections AS c ON m.id = c.child") or die(mysqli_error());

if (mysqli_num_rows($result) > 0) {
    $response["menu_site"] = array();

    while ($row = mysqli_fetch_array($result)) {
        $menu_site = array();
        $menu_site["id"] = $row["id"];
        $menu_site["name"] = $row["name"];
        $menu_site["alias"] = $row["alias"];
	$menu_site["parent"] = $row["parent"];

        array_push($response["menu_site"], $menu_site);
    }
    $response["success"] = 1;

    echo json_encode($response);
} else {
    $response["success"] = 0;
    $response["message"] = "No data menu found";

    echo json_encode($response);
}
?>
