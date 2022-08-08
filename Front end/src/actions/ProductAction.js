import axios from 'axios';
import Qs from 'qs'

export const getAllProducts = async () => {
    return await axios.get("/products/all",/* {timeout:5000} */);
}


export const getProduct = async (id) => {
  return await axios.get(`/products/${id}`);
};

export const getProducts = async (ids) => {
       return await axios.post(`/products/list`, ids);
}

export const search_product = async (searchType, keyWords, sort, filters, offset, limit) => {
  return await axios.post(
    `/products/search/${searchType}`,
    {
      ...filters,
    },
    {
      params: {
        keyWords: keyWords,
        offset: offset,
        limit: limit,
        sort: sort,
        SELLER: filters.SELLER,
        BRAND: filters.BRAND,
        RATING_SCORE: filters.RATING_SCORE,
      },
     
      paramsSerializer: params => Qs.stringify(params, {arrayFormat: 'comma'})
    }
  );
};