<?php

class Authentication {
    /*
     * Tries to authenticate the User either via Remember Me Cookie or Session
     * @return: TRUE or FALSE  
     */

    public static function isAuthenticated() {
        //TODO
        //look if Token is present in $_Session
        Session::get('UserID') ? $auth = 'TRUE' : $auth = 'FALSE';
        if ($auth === 'TRUE') {
            return TRUE;
        } else if (isset($_COOKIE["RemembeR"])) {
            $db = new Database();
            //Get TOKEN from $_COOKIE
            //hash(Token) and compare with hashed Tokens in the Database
            $userdata['tokenhash'] = hash('sha1', $_COOKIE["RemembeR"]);
            echo $userdata['tokenhash'];
            $arr = $db->select("SELECT id, tokenhash FROM users WHERE tokenhash = :tokenhash LIMIT 1", $userdata);
            var_dump($arr);
            if (sizeof($arr[0]['id']) !== 0) {
                //Set 'UserID' in Session
                Session::set('UserID', $arr[0]['id']);
                //Renew Cookie
                $token = hash("md5", $arr[0]['id'] . Session::get('ID'));
                $userdata['id'] = $arr[0]['id'];
                $userdata['tokenhash'] = hash("sha1", $token);
                $db->update("users", $userdata, "id = :id");
                setcookie("RemembeR", $token, time() + 259200, "/", "studi.f4.htw-berlin.de", false, true);
                return TRUE;
            }
        }
        return FALSE;
    }

}
