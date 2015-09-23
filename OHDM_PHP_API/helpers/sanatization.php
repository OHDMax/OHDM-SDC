<?php

class Sanatization {
    
    /* Taken From: http://www.w3schools.com/php/php_form_validation.asp */
    public static function sanatizeFieldInput($data) {
        $data = trim($data);
        $data = stripslashes($data);
        $data = htmlspecialchars($data);
        return $data;
    }
    
    public static function sanatizeArrayInput($data) {
        foreach ($data as $key => $value) {
            $nk = Sanatization::sanatizeFieldInput($key);
            $arr[$nk] = Sanatization::sanatizeFieldInput($value);
        }
        return $arr;
    }

    /* Taken from my last project */
    public static function sanatizeSQLInput($data) {
        $badwords = array(";", "DROP", "TABLES", "ALTER", "UPDATE", "DELETE", " ");
        $san = str_replace($badwords, "", $data);
        $sanatized = filter_var($san, FILTER_SANITIZE_STRING);
        return $sanatized;
    }

    public static function sanatizeNumbers($data) {
        if (is_numeric($data)) {
            $sanatized = $data;
        } else {
            $sanatized = filter_var($data, FILTER_SANITIZE_NUMBER_INT);
        }
        return $sanatized;
    }

}
