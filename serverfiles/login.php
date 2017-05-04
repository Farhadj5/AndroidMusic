<?php
   include('database.php');

   if(isset($_POST["email"]) and isset($_POST["password"])){
      $email = $_POST["email"];
      $password = $_POST["password"];
   
      login($email, $password);

//   $result = array();
  // array_push($result,array("success"=>"1", "email"=>$email, "password"=>$password));

  // echo json_encode(array("result"=>$result));
}
?>
