<?php

class URL {

   public static function redirect($url = null, $status) {
      header('Location: ' . DIR . $url, true, $status);
      exit;
   }

   public static function halt($status = 404, $message = 'Something went wrong.') {
      if (ob_get_level() !== 0) {
          ob_clean();
      }

      http_response_code($status);
      $data['status'] = $status;
      $data['message'] = $message;

      if (!file_exists("views/error/$status.php")) {
         $status = 'default';
      }
      require "views/error/$status.php";

      exit;
   }

   public static function STYLES($filename = false, $path = 'static/css/') {
      if ($filename) {
         $path .= "$filename.css";
      }
      return DIR . $path;
   }
   
   public static function PRO_IMG($filename = false, $path = 'static/img/products/') {
        if ($filename) {
         $path .= "$filename.jpg";
      }
      return DIR . $path;
   }
   
    public static function BOR_IMG($filename = false, $path = 'static/img/boards/') {
        if ($filename) {
         $path .= "$filename.jpg";
      }
      return DIR . $path;
   }
}
