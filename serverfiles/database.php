<?php
   $host = "localhost";
   $user = "root";
   $pass = "8ter9&in";
   $dbname = "phoenix_radio";

   function connect_db(){
      $conn = mysqli_connect($GLOBALS['host'], $GLOBALS['user'], $GLOBALS['pass']
                           , $GLOBALS['dbname']);

      if(mysqli_connect()){
         echo "Failed to connect to MySQL:" . mysqli_connect_error();
      }
      return $conn;
   }

   function register($email_val, $password_val){
      $conn = connect_db();
            
      $stmt = $conn->prepare("insert into users(email, password) values (?, ?)");

      $stmt->bind_param("ss", $email, $password);

      $email = $email_val;
      $password = $password_val;

      $stmt->execute();

      echo "Successfully registered!";
      $stmt->close();
   }

   function check_duplicate($email_val, $password){
      $conn = connect_db();

      $stmt = $conn->prepare("select email from users where email=?");

      $stmt->bind_param("s", $email);

      $email = $email_val;

      $stmt->execute();

      $stmt->bind_result($email_check);

      $result = array();

      if($stmt->fetch()){
         array_push($result,array("success"=>"0"));
         echo json_encode(array("result"=>$result));
         return;
      } else {
         register($email_val, $password);
      }

      $stmt->close();
   }

   function login($email_val, $password_val){
      $conn = connect_db();

      $stmt = $conn->prepare("select email, password from users where email=?");

      $stmt->bind_param("s", $email);

      $email = $email_val;

      $stmt->execute();

      $stmt->bind_result($email_check, $hash);

      $result = array();

      if($stmt->fetch()){
         if(password_verify($password_val, $hash)){
            array_push($result,array("success"=>"1"));
            echo json_encode(array("result"=>$result));
         } else {
            array_push($result,array("success"=>"0"));
            echo json_encode(array("result"=>$result));
         }
      } else {
            array_push($result,array("success"=>"2"));
            echo json_encode(array("result"=>$result));
      }

      $stmt->close();
      
   }

   function rating($rating_val, $cur_song_val){
      $conn = connect_db();
  
      if(!($stmt = $conn->prepare("select song_id from songs where song_title=?"))){
         echo "Prepare failed: (" . $conn->errno . ") " . $conn->error;
}

      $stmt->bind_param("s", $cur_song);

      $cur_song = $cur_song_val;

      $stmt->execute();

      $stmt->bind_result($cur_song_id);
      if($stmt->fetch()){
         $song_id = $cur_song_id;
      }
      $stmt->close();
      
      $stmt = $conn->prepare("insert into user_rating (value, song_id) values (?, ?)");

      $stmt->bind_param("di", $rating, $song_id);

      $rating = $rating_val;
      $cur_song = $cur_song_val;

      $stmt->execute();

      echo "Rating saved!";
      
      $stmt->close();
   }

?>
