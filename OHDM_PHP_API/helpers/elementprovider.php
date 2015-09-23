<?php

class ElementProvider {

    public static function getMainElements($type) {
        $db = new Database();
        if ($type === "boards") {
            return $db->select('SELECT id, ownerid, catid, name, picture, updated'
                            . ' FROM boards ORDER BY id DESC LIMIT 3');
        } else if ($type === "products") {
            return $db->select('SELECT id, ownerid, catid, name, picture, price, source'
                            . ' FROM products ORDER BY id DESC LIMIT 3');
        }
    }

}
