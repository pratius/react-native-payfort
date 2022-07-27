import React, { useState } from "react";
import {
  View,
  Text,
  TouchableOpacity,

} from "react-native";
import PayFort from "./PayFortSDK";


const PlaceOrder = (props) => {

  const onPay = async () => {
    var currentDate = new Date();
    var month = currentDate.getMonth() + 1;
    var date = currentDate.getUTCDate();
    var year = currentDate.getFullYear();
    var time = currentDate.getTime();

    const uniqueId = date + "" + month + "" + year + "" + time;

    await PayFort.RNPayFort({
      command: 'PURCHASE',
      access_code: 'access_code',
      merchant_identifier: 'merchant_identifier',
      sha_request_phrase: 'sha_request_phrase]',
      amount: (2 * 100).toString(), //If the transaction value is 500 AED; according to ISO 4217, you should multiply the value with 100 (to accommodate 2 decimal points). You will therefore send an AED 500 purchase amount as a value of 50000.
      currencyType: 'AED',
      language: 'en',
      email: 'pratius.dubey@gmail.com',
      testing: true,
      merchant_reference: uniqueId,
    })
      .then((response) => {
        console.log("payment response", response);
        if (response?.response_message === "Success") {
          console.log("Payfort response==", response);
        } else {
          console.log("payment failed==", response);
        }
      })
      .catch(async (error) => {
        console.log("error==", error);

      });
  };

  return (
    <View style={{ flex: 1, justifyContent: 'center' }}>

      <TouchableOpacity
        style={{ width: 200, height: 110,
           backgroundColor: 'black', 
           alignSelf: 'center', 
           justifyContent: 'center', 
           borderRadius: 10 }}
        onPress={onPay}
      >
        <Text style={{ color: '#fff', fontSize: 18, alignSelf: 'center' }}>
          Make a Payment
        </Text>
      </TouchableOpacity>
    </View>
  );
};

export default PlaceOrder;
