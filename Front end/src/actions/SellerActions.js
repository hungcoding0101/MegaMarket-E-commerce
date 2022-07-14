import axios from "axios";

export const uploadProduct = async (product) => {
  return await axios.post("/seller/upload/product", product, {
    headers: {
      contain_token: "yes",
    },
  });
};

export const getOwnedProducts = async () => {
     return await axios.get("/seller/getOwnedProducts", {
       headers: {
         contain_token: "yes",
       },
     });
}

export const updateOrders = async (newStates) => {
  return await axios.patch("/seller/update/orders", newStates, {
    headers: {
      contain_token: "yes",
    },
  });
};