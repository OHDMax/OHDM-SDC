<?php

class SDC extends Controller {

    function index() {
        $data["content"] = $this->_model->getReferenceObjects();
        $this->_view->render("SDC/json", $data);
    }

    function create() {
        if ($_SERVER['REQUEST_METHOD'] == 'PUT') {
            $input = file_get_contents("php://input");
            $data['content'] = $this->_model->sendDataToAPI($input);
            $this->_view->render("SDC/json", $data);
        } else {
            $this->_view->render("SDC/error");
        }
    }

    function get($lat, $lon, $radius, $substance_filter = false) {
        //Sanatization
        $input = $this->sanatizeInput($lat, $lon, $radius, $substance_filter);
        //Proccessing
        $data["content"] = $this->_model->getDataFromAPI($input);
        //Output
        $this->_view->render("SDC/json", $data);
    }

    private function sanatizeInput($lat, $lon, $radius, $substance_filter) {
        $input["lat"] = Sanatization::sanatizeNumbers($lat);
        $input["lon"] = Sanatization::sanatizeNumbers($lon);
        $input["radius"] = Sanatization::sanatizeNumbers($radius);
        $input["substance"] = Sanatization::sanatizeFieldInput($substance_filter);
        return $input;
    }

}
