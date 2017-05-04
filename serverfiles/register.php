<?php
   include('database.php');
   

   if(isset($_POST["email"]) and isset($_POST["password"])){
      $email = $_POST["email"];
      $password = $_POST["password"];     

      $hash = password_hash($password, PASSWORD_BCRYPT);
         
      check_duplicate($email, $hash);

   }

?>
