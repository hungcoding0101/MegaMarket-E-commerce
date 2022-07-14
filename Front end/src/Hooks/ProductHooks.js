import { useQuery, useQueryClient } from 'react-query'
import { getAllProducts, getProduct, search_product } from "../actions/ProductAction";
import {product_keys} from '../QueryKeys/Product_Keys'


export const useAllProducts = (onSuccess, onError, enabled, staleTime) => {
  return useQuery(product_keys.all, getAllProducts, {
    onError: (error) => (onError ? onError(error) : {}),
    onSuccess: (data) => (onSuccess ? onSuccess(data) : {}),
    enabled: enabled,
    staleTime: staleTime ? staleTime : 9000,
    notifyOnChangeProps: ["data", "error"],
    isDataEqual: (oldData, newData) =>
      JSON.stringify(oldData) === JSON.stringify(newData), 
  });
}

export const useOneProduct = (product_id, onSuccess, onError, enabled) => {
    const queryClient = useQueryClient();
    return useQuery(product_keys.id(product_id), () => getProduct(product_id), {
      onError: (error) => (onError ? onError(error) : {}),
      onSuccess: (data) => {
        if (onSuccess) {
          onSuccess(data);
        }
      },
      
      enabled: enabled,
      initialData: () => {
        const product = queryClient
          .getQueryData(product_keys.all)
          ?.data?.find((product) => product.id === product_id);

        if (product != null) {
          return { data: product };
        } else {
          return undefined;
        }
      },

      staleTime: 60000,

      initialDataUpdatedAt: () =>
        queryClient.getQueryState(product_keys.all)?.dataUpdatedAt,

      notifyOnChangeProps: 'tracked',
    });
}

export const useSearch_product = (
  searchType,
  keyWords,
  sort,
  filters,
  offset,
  limit,
  onSuccess,
  onError,
  enabled
) => {
  return useQuery(
    product_keys.search_pagination(
      searchType,
      keyWords,
      sort,
      filters,
      offset,
      limit
    ),
    () => search_product(searchType, keyWords, sort, filters, offset, limit),
    {
      enabled: enabled,
      onError: (error) => (onError ? onError(error) : {}),
      onSuccess: (data) => (onSuccess ? onSuccess(data) : {}),
      staleTime: 60000,

    }
  );
};

/* 
export const useProductsById = (ids, onSuccess, onError, enabled) => {
  return useQuery(product_keys.ids(ids), () => getProducts(ids), {
    enabled: enabled,
    onError: (error) => (onError ? onError(error) : {}),
    onSuccess: (data) => (onSuccess ? onSuccess(data) : {}),
  });
}; */

