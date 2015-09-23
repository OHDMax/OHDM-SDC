<?php

class Database extends PDO {

    function __construct() {
        try {
            parent::__construct(DB_TYPE . ':host=' . DB_HOST . ";port = 5432" . ';dbname=' . DB_NAME . ";user=" . DB_USER . ";password=" . DB_PASS); //';charset=utf8'
            $this->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $this->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);
        } catch (PDOException $e) {
            Logger::newMessage($e);
            logger::customErrorMsg();
        }
    }

    public function select($sql, $array = array(), $fetchMode = PDO::FETCH_ASSOC) {

        $stmt = $this->prepare($sql);
        foreach ($array as $key => $value) {
            $stmt->bindValue("$key", $value);
        }

        $stmt->execute();
        return $stmt->fetchAll($fetchMode);
    }

    public function insert($table, $data) {

        ksort($data);

        $fieldNames = implode(',', array_keys($data));
        $fieldValues = ':' . implode(', :', array_keys($data));

        $stmt = $this->prepare("INSERT INTO $table ($fieldNames) VALUES ($fieldValues)");

        foreach ($data as $key => $value) {
            $stmt->bindValue(":$key", $value);
        }

        $stmt->execute();
    }

    public function update($table, $data, $where) {

        ksort($data);

        $fieldDetails = NULL;
        foreach ($data as $key => $value) {
            $fieldDetails .= "$key = :$key,";
        }
        $fieldDetails = rtrim($fieldDetails, ',');

        $stmt = $this->prepare("UPDATE $table SET $fieldDetails WHERE $where");

        foreach ($data as $key => $value) {
            $stmt->bindValue(":$key", $value);
        }

        $stmt->execute();
    }

    public function delete($table, $where, $limit = 1) {
        return $this->exec("DELETE FROM $table WHERE $where LIMIT $limit");
    }
    
    public function psqldelete($table, $where) {
        return $this->exec("DELETE FROM $table WHERE $where");
    }

}
