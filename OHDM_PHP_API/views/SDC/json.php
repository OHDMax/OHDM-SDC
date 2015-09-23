<?php
header('Content-type: application/json');
header('Cache-Control: no-cache');
http_response_code($data["content"]["status"]);

if ($data["content"]["response"] == '') {
    echo '[]';
} else {
    echo $data["content"]["response"];
}