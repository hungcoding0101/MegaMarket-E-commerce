import { useMutation, useQueryClient } from "react-query";
import { addToCart, deleteCart, updateAddresses } from "../actions/UserAction";
import { customer_key, user_keys } from "../QueryKeys/User_keys";

export const useAddToCart = () => {
  const queryClient = useQueryClient();
  return useMutation(customer_key.add_to_cart, addToCart, {
    onSuccess: (data) => {
      queryClient.invalidateQueries(user_keys.current_user);
    },
  });
};

export const useDeleteCart = () => {
  const queryClient = useQueryClient();
  return useMutation(customer_key.delete_cart, deleteCart, {
    onSuccess: (data) => {
      queryClient.invalidateQueries(user_keys.current_user);
    },
  });
};

export const useUpdateAddresses = () => {
  const queryClient = useQueryClient();
  return useMutation(updateAddresses, {
    onSuccess: (data) => {
      queryClient.invalidateQueries(user_keys.current_user);
    },
  });
};