<?php

class Category {

    public static function getAllCategories() {
        $db = new Database();
        $arr = $db->select("SELECT name, id FROM category");
        $data = array();
        foreach ($arr as $position => $category) {
            $data[$category['id']] = $category['name'];
        }
        return $data;
    }

    public static function checkCategory($category) {
        $categoryarray = Category::getAllCategories();
        if (in_array($category, $categoryarray)) {
            //get Key from array
            $id = array_search($category, $categoryarray);
            return $id;
        } else {
            return FALSE;
        }
    }
    
        public static function getCategory($categoryid) {
        $categoryarray = Category::getAllCategories();
        if (array_key_exists($categoryid, $categoryarray)) {
            //get Key from array
            $name = $categoryarray[$categoryid];
            return $name;
        } else {
            return FALSE;
        }
    }

    public static function insertCategory($category) {
        $db = new Database();
        //Kategorie anlegen
        $catdata['name'] = $category;
        $db->insert("category", $catdata);
        //Kategorie ID holen
        $catarr = $db->select("SELECT name, id FROM category WHERE name = :name LIMIT 1", $catdata);

        return $catarr[0]['id'];
    }

}
