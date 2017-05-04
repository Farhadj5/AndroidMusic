<?php
   include('database.php');
   include('simple_html_dom.php');
   
   $text = file_get_html('http://162.243.192.229:8080/status.xsl')->plaintext;

   preg_match('/g:([^\s]+).[ -][\W](.*)[\W][S]/', $text, $matches, PREG_OFFSET_CAPTURE);


   $cur_song = $matches[2][0];
   $cur_song = trim($cur_song);
   

   if(isset($_POST["rating"])){
      $rating = $_POST["rating"];
      rating($rating, $cur_song);
   }
?>
