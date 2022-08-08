import axios from "axios";

export const placeOrder = async (orders) => {
      return await axios.post(`/order/add`, orders, {
        headers: {
          contain_token: "yes",
        },
      });
};

export const cancelOrder = async (id) => {
     return await axios.patch(`/order/cancel?id=${id}`, {
       headers: {
         contain_token: "yes",
       },
     });     
}