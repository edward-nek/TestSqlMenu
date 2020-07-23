<?php
$response = array();

echo 'id = ';
echo $_POST['id'];
echo ', name = ';
echo $_POST['name'];
echo ', alias = ';
echo $_POST['alias'];
echo ', parent = ';
echo $_POST['parent'];

if (isset($_POST['id']) && isset($_POST['name']) && isset($_POST['alias']) &&
 isset($_POST['parent'])) {

    $id = $_POST['id'];
    $name = $_POST['name'];
    $alias = $_POST['alias'];
    $parent = $_POST['parent'];

    require 'db_config.php';
    $con = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

    $result = mysqli_query($con, "INSERT INTO menu_site(id, name, alias) VALUES ('$id', '$name', '$alias')");

    if ($result) {
	$result2 = mysqli_query($con, "INSERT INTO connections(parent, child) VALUES ('$parent', '$id')");
    }

    if ($result && $result2) {
        $response["success"] = 1;
        $response["message"] = "Data successfully created.";

        echo json_encode($response);
    } else {
	if ($result){
		$result = mysqli($con, "DELETE FROM menu_site WHERE id = '$id'");
	}

        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";

        echo json_encode($response);
    }
} else {
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    echo json_encode($response);
}
?>
