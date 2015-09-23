<?php

class SDC_Model extends Model {

    function getReferenceObjects() {
        $url = "http://ohsm.f4.htw-berlin.de:8080/OhdmApi/geographicObject/nearObjects/data=true/1/13.41053/52.52437";
        $connection_options = $this->buildConnection();
        $arr["response"] = file_get_contents($url, false, $connection_options);
        $arr["status"] = substr($http_response_header[0], 9, 3);
        //return the answer
        return $arr;
    }

    function getDataFromAPI($data) {
        //extract necessary data from $data
        $data["radius"] = round($data['radius'] * 1.10);
        
        if ($data["radius"] > 1000) {
            $data["radius"] = 1000;
        }
        
        if ($data["substance"]){
            $tags ="substance=" . $data['substance'];
        } else {
            $tags = "data=true";
        }
        $url="http://ohsm.f4.htw-berlin.de:8080/OhdmApi/geographicObject/nearObjects/". $tags . "/" . $data["radius"] . "/" . $data["lat"] . "/" . $data["lon"] . "/";
        //echo $url;
        //Place an API call and return its contents
        $connection_options = $this->buildConnection();
        $arr["response"] = file_get_contents($url, false, $connection_options);
        $arr["status"] = substr($http_response_header[0], 9, 3);
        //return the answer
        return $arr;
    }

    function sendDataToAPI($data) {
        //get JSON from Source and parse it
        $arr = json_decode($data, true);
        $date = DateTime::createFromFormat("U", $arr["timestamp"], timezone_open("Europe/Berlin"));
        $date = date_format($date, 'Y-m-d');
        //Make a new JSON in the format described on page X of the API Documentation
        $json = '{
                    "originalId":"11111",
                    "attributes":{
                    },
                    "externalSourceId":36,
                    "geoBlobDates":[
                    {
                    "valid":{
                    "since":"' . $date . '",
                    "until":"3000-01-01"
                    }
                    },
                    {
                    "valid":{
                    "since":"' . $date . '",
                    "until":"3000-01-01"
                    }
                    }
                    ],
                    "tagDates":[
                    {
                    "tags":{
                    "data":"true",
                    "substance":"' . $arr["substance"] . '",
                    "value":"' . $arr["value"] . '",
                    "unit":"' . $arr["unit"] . '",
                    "timestamp":' . $arr["timestamp"] . '
                    },
                    "valid":{
                    "since":"' . $date . '",
                    "until":"3000-01-01"
                    }
                    }
                    ],
                    "geometricObjects":[
                    {
                    "valid":{
                    "since":"' . $date . '",
                    "until":"' . $date . '"
                    },
                    "multipoint":"MULTIPOINT(' . $arr["lon"] . ' ' . $arr["lat"] . ' 0)"
                    }
                    ]
                  }';
        //send this
        $url = "http://ohsm.f4.htw-berlin.de:8080/OhdmApi/geographicObject/";
        $connection_options = $this->buildConnection("PUT", $json);
        $arr["response"] = file_get_contents($url, false, $connection_options);
        $arr["status"] = substr($http_response_header[0], 9, 3);
        //return the answer
        return $arr;
    }

    private function buildConnection($method = "GET", $data = '', $token = "74dc2f347d73ef4fbec38de0429b1357fcb5687f1879495c6e2bf289f6d3411eb391bf4bd2608176749040478bdac6fb5566543cedc9b963114a3b4cc98a032f") {
        $options = array(
            'http' => array(
                'method' => "$method",
                'header' => "Authorization: Bearer " . $token . "\r\n" .
                "Content-Type: application/json; charset=utf-8 \r\n",
                'content' => "$data"
            )
        );
        return $connection_options = stream_context_create($options);
    }

}
