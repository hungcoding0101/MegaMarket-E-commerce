import axios from 'axios';
import Qs from 'qs'

export const getAllProducts = async () => {
    return await axios.get("/product/getAllProducts",/* {timeout:5000} */);
}


export const getProduct = async (id) => {
  return await axios.get(`/product/getProduct/${id}`);
};

export const getProducts = async (ids) => {
       return await axios.post(`/product/getProducts`, ids);
}

export const search_products_by_name = async (name, sort, filters, offset, limit) => {
  return await axios.post(
    `/product/search/name`,
    {
      filters,
    },
    {
      params: {
        keyWords: name,
        offset: offset,
        limit: limit,
        sort: sort,
      },
    }
  );
};

export const search_products_by_category = async (category, sort, filters, offset, limit) => {
  return await axios.post(
    `/product/search/category`,
    {
      filters,
    },
    {
      params: {
        keyWords: category,
        offset: offset,
        limit: limit,
        sort: sort,
      },
    }
  );
};

export const search_product = async (searchType, keyWords, sort, filters, offset, limit) => {
  return await axios.post(
    `/product/search/${searchType}`,
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