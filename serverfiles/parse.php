<?php
   include('simple_html_dom.php');

   $text = file_get_html('http://162.243.192.229:8080/status.xsl')->plaintext;
   

   preg_match('/g:([^\s]+).[ -][\W](.*)[\W][S]/', $text, $matches, PREG_OFFSET_CAPTURE);
   $info = array();
  // $info = array_values($info);
   
   array_push($info,array("artist"=>$matches[1][0]));
   array_push($info,array("title"=>$matches[2][0]));
   echo json_encode(array("info"=>$info));
?>
