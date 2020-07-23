<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);

	require 'db_config.php';
        $mysqli = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

	echo "Menu-site";
	$result = mysqli_query($mysqli, "SELECT * FROM menu_site");

	while($row = mysqli_fetch_array($result)){
    		$id=$row['id'];
    		$name=$row['name'];
    		$alias=$row['alias'];
    		echo "<p>$id - $name - $alias</p>";
   	 }
	echo "Connections";

	$result2 = mysqli_query($mysqli, "SELECT * FROM connections");

        while($row = mysqli_fetch_array($result2)){
                $id1=$row['id'];
                $parent=$row['parent'];
                $child=$row['child'];
                echo "<p>$id1 - $parent - $child</p>";
         }
?>
