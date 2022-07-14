import { useQuery } from "react-query";
import {
  getAllProductCategories,
  getCategoriesByDepth,
  getSuggestions,
  listProductCategories,
} from "../actions/ProductCategoryAction";
import { product_category_keys } from "../QueryKeys/Product_Keys";


export const useProductCategoryTree = (onSuccess, onError, enabled) => {
  return useQuery(product_category_keys.tree, () => listProductCategories(), {
    onError: (error) => (onError ? onError(error) : {}),
    onSuccess: (data) => (onSuccess ? onSuccess(data) : {}),
    enabled: enabled,
    staleTime: Infinity,
    cacheTime: Infinity,
  });
};

export const useAllProductCategory = (onSuccess, onError, enabled) => {
  return useQuery(product_category_keys.all, getAllProductCategories, {
    onError: (error) => (onError ? onError(error) : {}),
    onSuccess: (data) => (onSuccess ? onSuccess(data) : {}),
    enabled: enabled,
    staleTime: Infinity,
    cacheTime: Infinity,
  });
};

export const useFetchCategoryByDepth = (depth, offset, limit, onSuccess, onError, enabled) => {
  return useQuery(
    product_category_keys.depth(depth, offset, limit),
    () => getCategoriesByDepth(depth, offset, limit),
    {
      onError: (error) => (onError ? onError(error) : {}),
      onSuccess: (data) => (onSuccess ? onSuccess(data) : {}),
      enabled: enabled,
      staleTime: 60000,
    }
  );
};

export const useFetchSuggestions = (
  keyWords,
  offset,
  limit,
  onSuccess,
  onError,
  enabled
) => {
  console.log("SUGGETS HOOK CALLED");
  return useQuery(
    product_category_keys.suggettions(keyWords, offset, limit),
    () => getSuggestions(keyWords, offset, limit),
    {
      onError: (error) => (onError ? onError(error) : {}),
      onSuccess: (data) => (onSuccess ? onSuccess(data) : {}),
      enabled: enabled,
      staleTime: 60000,
    }
  );
};