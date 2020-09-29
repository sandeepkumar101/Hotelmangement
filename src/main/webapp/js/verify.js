/** Copyright (C) HotelManagement  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by HotelManagement  Innovations <htms.india@gmail.com>, October 2015
 */

javaRest.verify = {}

/**
 * Sends an email to user for verification
 */
javaRest.verify.request_email = function (email, callback) {
  javaRest.post(
    'verify/tokens',
    {
      'emailAddress' : email
    },
    function (response) {
      console.log(response)
      callback()
    },
    function(jqXHR, textStatus) {
      callback(jqXHR)
    })
}

/**
 * Validate an email address.
 */
javaRest.verify.verify = function (token, callback) {
  javaRest.post(
    'verify/tokens/' + token,
    {},
    function (response) {
      callback()
    },
    function(jqXHR, textStatus) {
      callback(jqXHR)
    })
}

