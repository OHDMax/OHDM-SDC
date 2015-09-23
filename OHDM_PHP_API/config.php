<?php

//set timezone
date_default_timezone_set('Europe/Berlin');

//site address
define('DIR','https://studi.f4.htw-berlin.de/~s0544210/OHDM_PHP_API/');
define('DOCROOT', dirname(__FILE__));

// Credentials for the local server
// define('DB_TYPE','mysql');
// define('DB_HOST','localhost');
// define('DB_NAME','products');
// define('DB_USER','root');
// define('DB_PASS','root');

// Credentials for the HTW server
define('DB_TYPE','pgsql');
define('DB_HOST','xxxx');
define('DB_NAME','xxxx');
define('DB_USER','xxxx');
define('DB_PASS','xxxx');

//set prefix for sessions
define('SESSION_PREFIX','api_');

//optionall create a constant for the name of the site
define('SITETITLE','API');

define('TEMPLATE','static');
