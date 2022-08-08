import axios from "axios"

export const listProductCategories = async () => {
    return await axios.get(`/products/categories/tree`, {
      headers: {
        Accept: "application/json",
      },
    });
}

export const getAllProductCategories = async () => {
  return await axios.get(`/products/categories/all`, {
    headers: {
      Accept: "application/json",
    },
  });
};

export const getCategoriesByDepth = async (depth, offset, limit) => {
  const response = await axios.get(`/products/categories`,
    {
      params: {
          depth: depth,
          offset: offset,
          limit: limit,
      },
    }
  );
  return response;
};

export const getSuggestions = async (keyWords, offset, limit) => {
  console.log('SUGGETS ACTION CALLED')
  return await axios.get(`/products/suggestions`, {
    params: {
      keyWords: keyWords,
      offset: offset,
      limit: limit,
    },
  });

};

