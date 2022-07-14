
export const product_keys = {
  all: ["all"],

  search: (searchType, keyWords, sort, filters) => [
    ...product_keys.all,
    searchType,
    keyWords,
    sort,
    filters,
  ],

  search_pagination: (searchType, keyWords, sort, filters, offset, limit) => [
    ...product_keys.search(searchType, keyWords, sort, filters), offset, limit
  ],

  id: (id) => [...product_keys.all, id],
  ids: (ids) => [...product_keys.all, ids],
  ids_pagination: (ids, offset, limit) => [
    ...product_keys.ids(ids),
    offset,
    limit,
  ],
};

export const product_category_keys = {
  all: ["all"],
  tree: ["tree"],
  suggettions: (keyWords, offset, limit) => [...product_category_keys.all ,keyWords, offset, limit],
  depth: (depth, offset, limit) => [...product_category_keys.all, depth, offset, limit],
};